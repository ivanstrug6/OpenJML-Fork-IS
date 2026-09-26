package org.jmlspecs.openjmltest.testsuites;

import org.jmlspecs.openjmltest.EscBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openjml.runners.ParameterizedWithNames;

/** FinModel patch C3 (ISSUE-054, ISSUE-1690, ISSUE-2565): 'MISMATCHED BLOCKS-CBX' / 'MISMATCHED BLOCKS'.
 *  An exception thrown part-way through the ESC translation of a method (here: a NullPointerException in
 *  assumeFieldInvariants on a record component field redeclared in a .jml companion, which has no symbol)
 *  unwound past pushBlock calls without their popBlock; the applyHelper/convertBlock finally clauses then
 *  reported a catastrophic MISMATCHED BLOCKS error and ESC of the WHOLE class was abandoned (rc=1).
 *  Post-patch: the construct translates, and the rest of the class is still verified.
 */
@org.junit.FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
@RunWith(ParameterizedWithNames.class)
public class escblockstack extends EscBase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        addOptions("--check-feasibility=none");
    }

    /** The HardenedZipWriter shape: 'return new Rejected(...)' where Rejected is a record whose .jml
     *  companion redeclares it with an invariant. Pre-patch: MISMATCHED BLOCKS-CBX and method 'other'
     *  was never verified. Post-patch: only the genuine assertion failure in 'other' is reported. */
    @Test
    public void testRecordInJmlCompanionNewInReturn() {
        addMockFile("$A/tt/TestJava.jml",
                """
                package tt;
                public class TestJava {
                    public record Rejected(String reason, String detail) {
                        //@ public invariant reason != null && detail != null;
                    }

                    //@ requires s != null;
                    public static Rejected make(String s);

                    public static int other(int i);
                }
                """
                );
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                    public record Rejected(String reason, String detail) {
                        public Rejected {
                            java.util.Objects.requireNonNull(reason, "reason");
                        }
                    }

                    public static Rejected make(String s) {
                        return new Rejected(s, "x");
                    }

                    public static int other(int i) {
                        //@ assert i == 0;
                        return i;
                    }
                }
                """
                ,"/tt/TestJava.java:14: verify: The prover cannot establish an assertion (Assert) in method other",13
                );
    }

    /** The ISSUE-1690 shape: if { for { list.add(new NameEntry(a,b,c,d,e,f)); } } with the record
     *  (with a compact constructor) redeclared in the .jml companion; the loop's frame warnings are
     *  expected (no loop_assigns), the point is that 'build' and 'other' are both verified. Pre-patch: MISMATCHED BLOCKS-CBX from
     *  visitNewClass <- convertArgs <- visitApply <- loopHelperMakeBody <- visitJmlForLoop <- visitIf. */
    @Test
    public void testRecordInJmlCompanionNewInLoopArg() {
        addMockFile("$A/tt/TestJava.jml",
                """
                package tt;
                public class TestJava {
                    public record NameEntry(String a, String b, String c, String d, int e, String f) {
                        //@ public invariant a != null;
                    }

                    public static void build(/*@ nullable */ String[] defs, java.util.List<NameEntry> entries);

                    public static int other(int i);
                }
                """
                );
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                    public record NameEntry(String a, String b, String c, String d, int e, String f) {
                        public NameEntry { java.util.Objects.requireNonNull(a, "a"); }
                    }

                    public static void build(String[] defs, java.util.List<NameEntry> entries) {
                        if (defs != null) {
                            for (int i = 0; i < defs.length; i++) {
                                entries.add(new NameEntry("a", "b", "c", "d", i, "f"));
                            }
                        }
                    }

                    public static int other(int i) {
                        //@ assert i == 0;
                        return i;
                    }
                }
                """
                ,"/tt/TestJava.java:10: verify: The prover cannot establish an assertion (Assignable) in method build: \\everything",29
                ,"/tt/TestJava.java:9: verify: Associated declaration",13
                ,"/tt/TestJava.java:10: verify: The prover cannot establish an assertion (Assignable) in method build: values",28
                ,"/tt/TestJava.java:9: verify: Associated declaration",13
                ,"/tt/TestJava.java:10: verify: The prover cannot establish an assertion (Assignable) in method build: values",28
                ,"/tt/TestJava.java:9: verify: Associated declaration",13
                ,"/tt/TestJava.java:16: verify: The prover cannot establish an assertion (Assert) in method other",13
                );
    }
}
