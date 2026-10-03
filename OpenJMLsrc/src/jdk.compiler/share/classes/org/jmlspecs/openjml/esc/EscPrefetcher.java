/*
 * FinModel patch T5: concurrent solver sessions for ESC.
 */
package org.jmlspecs.openjml.esc;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.tools.JavaFileObject;

import org.jmlspecs.openjml.JmlOption;
import org.jmlspecs.openjml.JmlTree.JmlMethodDecl;
import org.jmlspecs.openjml.Main;
import org.jmlspecs.openjml.Utils;
import org.openjml.IAPI;
import org.smtlib.IResponse;
import org.smtlib.ISolver;

import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JCDiagnostic;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Log.WriterKind;

/**
 * FinModel patch T5: lets ESC work on several methods of a compilation unit at once.
 * <P>
 * Most ESC time is spent inside the SMT solver, one solver process per method, strictly one
 * after the other. This class runs the solver for upcoming methods ahead of time: for each of
 * the next few methods (in exactly the order JmlEsc will prove them) it translates the method to
 * its SMT script on the compiler thread (translation is not thread-safe) and then, on a worker
 * thread, starts a solver process and executes the script up to and including the first
 * check-sat. When MethodProverSMT reaches that method it adopts the prepared translation and
 * the running solver process, and continues exactly as it would have (counterexample queries,
 * feasibility checks, reporting) on the compiler thread.
 * <P>
 * Results and output are unchanged: the solver process receives exactly the same commands as in
 * a sequential run (so it computes the same answers), and anything the translation or the solver
 * adapter would have printed or reported (diagnostics, notes, progress messages, SMT log output)
 * is recorded when it happens ahead of time and replayed at the point in the sequential order
 * where it would have occurred. Prefetching is only used where that equivalence is easy to
 * guarantee (no verbose/show/trace output, no split option, no @Options annotations in the unit,
 * single-split methods); otherwise ESC proceeds sequentially as before.
 */
public class EscPrefetcher {

    /** A method whose translation (and possibly solver run) has been done ahead of time */
    public class Prepared {
        final JmlMethodDecl decl;
        final String splitkey;
        /** output produced during translation, in order; replayed by replayTranslation */
        final List<Runnable> translationEvents = new ArrayList<>();
        /** output produced by the solver adapter while executing the script (worker thread) */
        final List<Runnable> executionEvents = Collections.synchronizedList(new ArrayList<>());
        MethodProverSMT.Translated translated;
        Throwable thrown;
        Future<MethodProverSMT.Executed> future;
        RecordingSMTListener smtListener;
        boolean adopted = false;
        boolean cancelled = false;
        MethodProverSMT.Executed execution;

        Prepared(JmlMethodDecl decl, String splitkey) {
            this.decl = decl;
            this.splitkey = splitkey;
        }

        /** Replays, on the compiler thread, the output recorded while translating */
        public void replayTranslation() {
            for (Runnable r: translationEvents) r.run();
            translationEvents.clear();
        }

        /** Waits for the solver run and replays anything the solver adapter logged meanwhile;
         * from then on the SMT listener forwards directly to the compiler log */
        public MethodProverSMT.Executed awaitExecution() {
            synchronized (this) { adopted = true; }
            MethodProverSMT.Executed ex;
            try {
                ex = future.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new Main.JmlCanceledException("Interrupted while waiting for the solver");
            } catch (ExecutionException e) {
                // startAndExecute catches exceptions from executing the script; anything else is
                // reported the same way as a sequential run would report it
                sneakyThrow(e.getCause());
                return null; // not reached
            }
            for (Runnable r: new ArrayList<>(executionEvents)) r.run();
            executionEvents.clear();
            smtListener.goLive(new MethodProverSMT.SMTListener(log, translated.smt.smtConfig.defaultPrinter));
            return ex;
        }

        /** Kills the solver of a prepared method that is not going to be used */
        synchronized void discard() {
            if (adopted) return;
            cancelled = true;
            if (execution != null && execution.solver != null) execution.solver.forceExit();
            if (future != null) future.cancel(true);
        }

        /** The worker-thread part: start the solver and execute the script (unless discarded) */
        MethodProverSMT.Executed run() {
            MethodProverSMT.Executed ex = new MethodProverSMT.Executed();
            synchronized (this) {
                if (cancelled) return ex;
                execution = ex;
                ex.start = new java.util.Date();
                // Same solver selection as MethodProverSMT.prove; a null exec lets jSMTLIB resolve it from the solver name
                ex.solver = translated.smt.startSolver(translated.smt.smtConfig,MethodProverSMT.smtSolverName(proverToUse),exec);
            }
            if (ex.solver == null) return ex;
            try {
                ex.response = translated.script.execute(ex.solver); // Note - the solver knows the smt configuration
            } catch (Exception e) {
                ex.exception = e;
            }
            ex.duration = (System.currentTimeMillis() - ex.start.getTime())/1000.0;
            return ex;
        }
    }

    /** An SMT log listener that records (into a sink) until it is told where to forward */
    static class RecordingSMTListener implements org.smtlib.Log.IListener {
        volatile List<Runnable> sink;
        volatile org.smtlib.Log.IListener live;
        RecordingSMTListener(List<Runnable> sink) { this.sink = sink; }
        void goLive(org.smtlib.Log.IListener l) { live = l; }
        private void add(java.util.function.Consumer<org.smtlib.Log.IListener> c) {
            org.smtlib.Log.IListener l = live;
            if (l != null) c.accept(l);
            else sink.add(() -> c.accept(live));
        }
        @Override public void logOut(String msg) { add(l -> l.logOut(msg)); }
        @Override public void logOutNoln(String msg) { add(l -> l.logOutNoln(msg)); }
        @Override public void logOut(IResponse result) { add(l -> l.logOut(result)); }
        @Override public void logError(String msg) { add(l -> l.logError(msg)); }
        @Override public void logError(IResponse.IError result) { add(l -> l.logError(result)); }
        @Override public void logDiag(String msg) { add(l -> l.logDiag(msg)); }
        @Override public void indent(String chars) { add(l -> l.indent(chars)); }
    }

    /** Records diagnostics instead of reporting them */
    static class RecordingDiagnosticHandler extends Log.DiagnosticHandler {
        final List<Runnable> events;
        final Log log;
        RecordingDiagnosticHandler(Log log, List<Runnable> events) {
            this.log = log;
            this.events = events;
            install(log);
        }
        @Override
        public void report(JCDiagnostic diag) {
            events.add(() -> log.report(diag));
        }
    }

    /** Captures, in order, everything written to the log's writers, reported to the log, or sent
     * to the progress listener, between install() and uninstall() */
    class Recorder {
        final List<Runnable> events;
        final Map<WriterKind,PrintWriter> saved = new EnumMap<>(WriterKind.class);
        RecordingDiagnosticHandler handler;
        IAPI.IProgressListener savedProgress;

        Recorder(List<Runnable> events) { this.events = events; }

        void install() {
            for (WriterKind k: WriterKind.values()) {
                PrintWriter real = log.getWriter(k);
                saved.put(k, real);
                log.setWriter(k, new PrintWriter(new Writer() {
                    @Override public void write(char[] cbuf, int off, int len) {
                        String s = new String(cbuf, off, len);
                        events.add(() -> log.getWriter(k).write(s));
                    }
                    @Override public void flush() { events.add(() -> log.getWriter(k).flush()); }
                    @Override public void close() {}
                }));
            }
            handler = new RecordingDiagnosticHandler(log, events);
            Main main = Main.instance(context);
            savedProgress = main.progressListener;
            final IAPI.IProgressListener real = savedProgress;
            main.progressListener = new IAPI.IProgressListener() {
                @Override public void setVerbose(int verbosity) { real.setVerbose(verbosity); }
                @Override public boolean report(int level, String message) {
                    events.add(() -> {
                        if (Main.instance(context).progressListener.report(level, message)) {
                            throw new com.sun.tools.javac.util.PropagatedException(new Main.JmlCanceledException("ESC operation cancelled"));
                        }
                    });
                    return false;
                }
                @Override public void worked(int ticks) { events.add(() -> Main.instance(context).progressListener.worked(ticks)); }
            };
        }

        void uninstall() {
            Main.instance(context).progressListener = savedProgress;
            log.popDiagnosticHandler(handler);
            for (var e: saved.entrySet()) {
                log.getWriter(e.getKey()).flush();
                log.setWriter(e.getKey(), e.getValue());
            }
        }
    }

    final JmlEsc jmlesc;
    final Context context;
    final Log log;
    final Utils utils;
    final String proverToUse;
    final String exec;
    final int window;
    final ExecutorService pool;
    final List<JmlMethodDecl> plan;
    final Map<JmlMethodDecl,Integer> index = new IdentityHashMap<>();
    final Map<JmlMethodDecl,Prepared> prepared = new IdentityHashMap<>();
    int nextToPrepare = 0;

    /** Returns the number of concurrent solver sessions requested */
    public static int threads(Context context) {
        return JmlOption.ESC_THREADS.getInt(context);
    }

    /** Whether the options permit prefetching at all (output must not depend on interleaving) */
    public static boolean allowedByOptions(Context context, JmlEsc jmlesc) {
        Utils utils = Utils.instance(context);
        if (jmlesc.verbose || JmlEsc.escdebug || utils.jmlverbose >= Utils.JMLVERBOSE) return false;
        String show = JmlOption.SHOW.value(context);
        if (show != null && !show.isEmpty()) return false;
        String split = JmlOption.SPLIT.value(context);
        if (split != null && !split.isEmpty()) return false;
        if (JmlOption.SUBEXPRESSIONS.isSet(context) || JmlOption.TRACE.isSet(context) || JmlOption.COUNTEREXAMPLE.isSet(context)) return false;
        return true;
    }

    public EscPrefetcher(JmlEsc jmlesc, List<JmlMethodDecl> plan, int threads, String proverToUse, String exec) {
        this.jmlesc = jmlesc;
        this.context = jmlesc.context;
        this.log = Log.instance(context);
        this.utils = Utils.instance(context);
        this.plan = plan;
        this.proverToUse = proverToUse;
        this.exec = exec;
        this.window = threads;
        for (int i = 0; i < plan.size(); i++) index.putIfAbsent(plan.get(i), i);
        this.pool = Executors.newFixedThreadPool(threads, r -> {
            Thread t = new Thread(r, "esc-solver");
            t.setDaemon(true);
            return t;
        });
    }

    /** Called when methodDecl is about to be proved: makes sure it and the next few planned methods
     * have been translated and have their solver runs started. */
    public void advance(JmlMethodDecl methodDecl) {
        Integer i = index.get(methodDecl);
        if (i == null) return;
        int last = Math.min(plan.size() - 1, i + window);
        if (nextToPrepare < i) nextToPrepare = i; // methods before this one will not be proved any more
        while (nextToPrepare <= last) {
            JmlMethodDecl m = plan.get(nextToPrepare++);
            if (!prepared.containsKey(m)) prepare(m);
        }
    }

    /** Returns (and removes) the prepared state for the given method and split, or null */
    public Prepared take(JmlMethodDecl methodDecl, String splitkey) {
        Prepared p = prepared.remove(methodDecl);
        if (p == null) return null;
        if (!p.splitkey.equals(splitkey)) {
            p.discard();
            return null;
        }
        return p;
    }

    protected void prepare(JmlMethodDecl m) {
        Translations translations = jmlesc.assertionAdder.methodBiMap.getf(m);
        if (translations == null) return;
        java.util.List<String> keys = translations.keys();
        if (keys.size() != 1) return;
        String splitkey = keys.get(0);
        JmlMethodDecl translatedMethod = translations.getTranslation(splitkey);
        if (translatedMethod == null) return;
        JCBlock newblock = translatedMethod.getBody();
        if (newblock == null) return;

        Prepared p = new Prepared(m, splitkey);
        p.smtListener = new RecordingSMTListener(p.translationEvents);
        MethodProverSMT mp = new MethodProverSMT(jmlesc);
        Recorder rec = new Recorder(p.translationEvents);
        JavaFileObject prevSource = log.useSource(m.sourcefile);
        rec.install();
        try {
            jmlesc.assertionAdder.setSplits(translations, splitkey);
            p.translated = mp.translateSplit(m, newblock, utils.getOwner(m), proverToUse, false, false, p.smtListener);
        } catch (Throwable t) {
            p.thrown = t;
        } finally {
            rec.uninstall();
            log.useSource(prevSource);
        }
        prepared.put(m, p);
        if (p.thrown == null && p.translated.early == null) {
            p.smtListener.sink = p.executionEvents; // from now on only the worker thread may log
            p.future = pool.submit(p::run);
        }
    }

    /** Kills any solver processes that were started but not used, and stops the worker threads */
    public void shutdown() {
        for (Prepared p: prepared.values()) p.discard();
        prepared.clear();
        pool.shutdownNow();
        // Solvers that finished starting after discard() are killed when their process ends;
        // make sure none is left running.
    }

    @SuppressWarnings("unchecked")
    static <T extends Throwable> void sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }
}
