/*
 * FinModel patch U: no per-command error-stream wait in jSMTLIB solver I/O.
 */
package org.jmlspecs.openjml.esc;

import java.lang.reflect.Field;

import org.smtlib.IResponse;
import org.smtlib.ISolver;
import org.smtlib.SMT;
import org.smtlib.SolverProcess;

/**
 * Starts SMT solvers for ESC with {@code SolverProcess.errorSettleMillis == 0}.
 * <P>
 * jSMTLIB's SolverProcess.listen() blocks on the solver's error stream for errorSettleMillis (default 1 ms)
 * after every command; 0 restores the non-blocking check. The field is public but the adapter's
 * SolverProcess is protected, so it is set by reflection between createSolver and start (the first command).
 */
public final class SolverStarter {

    private SolverStarter() {}

    private static final Field SOLVER_PROCESS = lookup();

    private static Field lookup() {
        try {
            Field f = org.smtlib.AbstractSolver.class.getDeclaredField("solverProcess");
            f.setAccessible(true);
            return f;
        } catch (Throwable t) {
            return null; // keep jSMTLIB's default wait
        }
    }

    /** Same contract as SMT.startSolver, but the solver's process waits 0 ms on its error stream per command. */
    public static ISolver startSolver(SMT smt, SMT.Configuration config, String solverName, String exec) {
        if (SOLVER_PROCESS != null) {
            ISolver solver = null;
            try {
                solver = config.createSolver(solverName, exec);
                Object p = SOLVER_PROCESS.get(solver);
                if (p instanceof SolverProcess sp) {
                    sp.errorSettleMillis = 0;
                    IResponse r = solver.start();
                    if (!r.isError()) return solver;
                }
            } catch (Throwable t) {
                // fall through to the stock path, which reports the failure
            }
            if (solver != null) {
                try { solver.exit(); } catch (Throwable t) { /* best effort */ }
            }
        }
        return smt.startSolver(config, solverName, exec);
    }
}
