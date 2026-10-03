package org.jmlspecs.openjmltest.testsuites;

import org.jmlspecs.openjmltest.EscBase;
import org.junit.*;

/** FinModel patch T5: ESC with concurrent solver sessions (--esc-threads) must produce exactly the
 * output of a sequential run (same diagnostics, same order), including failing proofs, an
 * 'ensures false' probe, local classes and feasibility checks done on the adopted solver session. */
@org.junit.FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
public class escthreads extends EscBase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        addOptions("--nullable-by-default");
    }

    static final String program = """
            package tt;
            public class TestJava {
              //@ requires x < 1000;
              //@ ensures \\result == x + 1;
              public int a(int x) { return x + 1; }

              //@ ensures \\result > 0;
              public int b(int x) { return x; }

              //@ ensures false;
              public void c() { }

              public void d(int[] arr) { arr[0] = 1; }

              //@ requires 0 < i && i < 1000;
              //@ ensures \\result == i;
              public int e(int i) {
                class L { int f(int k) { return k; } }
                return new L().f(i);
              }

              //@ requires x > 0;
              public void g(int x) {
                if (x < 0) {
                  //@ assert false;
                }
                //@ assert x != 0;
              }
            }
            """;

    static final Object[] expected = new Object[] {
             "/tt/TestJava.java:8: verify: The prover cannot establish an assertion (Postcondition) in method b", 25
            ,"/tt/TestJava.java:7: verify: Associated declaration", 7
            ,"/tt/TestJava.java:11: verify: The prover cannot establish an assertion (Postcondition) in method c", 15
            ,"/tt/TestJava.java:10: verify: Associated declaration", 7
            ,"/tt/TestJava.java:13: verify: The prover cannot establish an assertion (PossiblyTooLargeIndex) in method d", 33
            ,"/tt/TestJava.java:13: verify: The prover cannot establish an assertion (PossiblyNullDeReference) in method d", 33
            ,"/tt/TestJava.java:19: verify: The prover cannot establish an assertion (Postcondition) in method e", 5
            ,"/tt/TestJava.java:16: verify: Associated declaration", 7
            };

    @Test
    public void testSequential() {
        addOptions("--esc-threads=1");
        helpEsc("tt.TestJava", program, expected);
    }

    @Test
    public void testConcurrent() {
        addOptions("--esc-threads=4");
        helpEsc("tt.TestJava", program, expected);
    }

    @Test
    public void testConcurrentFeasibility() {
        addOptions("--esc-threads=3", "--check-feasibility=reachable");
        helpEsc("tt.TestJava", program, expected);
    }

    /** --esc-threads is a validated option: a negative value is a command-line error (exit 2)
     * with the pinned message, not a silently ignored setting. */
    @Test
    public void testEscThreadsRejectsNegative() {
        int exitCode = 4;
        collectSystemOutput(true);
        try {
            exitCode = org.jmlspecs.openjml.Main.execute(new String[]{"--esc-threads=-1"});
        } finally {
            collectSystemOutput(false);
        }
        String text = (output() + errorOutput()).replace("\r", "");
        Assert.assertTrue("Missing option-error message; output was: " + text,
                text.contains("Expected a non-negative integer as argument for --esc-threads: -1"));
        Assert.assertEquals("The exit code is wrong", 2, exitCode);
    }
}
