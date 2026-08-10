package org.jmlspecs.openjmltest.testsuites;

import org.jmlspecs.openjmltest.EscBase;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openjml.runners.ParameterizedWithNames;

@org.junit.FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
@RunWith(ParameterizedWithNames.class)
public class escstrings extends EscBase {

    /** This String declaration and assignment */
    @Test
    public void testSimpleString() {
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {
                  public TestJava t;
                  public int a;
                  public static int b;
                  public void m1(String s) {
                       String ss = s;
                       //@ assert s != null;
                       //@ assert s == ss;
                  }
                  //@ public normal_behavior
                  //@   ensures t != null;
                 public TestJava() { t = new TestJava(); }
                }
                """
                );
    }

    /** Tests String equality  */
    @Test
    public void testStringEquals() {
        addOptions("--esc-max-warnings=1");
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {
                  public TestJava t;
                  public int a;
                  public static int b;
                  public void m(String s) {
                       String ss = s;
                       /*@ nullable */ String sss = null;
                       //@ assert s.equals(ss);
                       //@ assert !s.equals(sss);
                       //@ assert !sss.equals(ss);
                  }
                  //@ public normal_behavior
                  //@   ensures t != null;
                  public TestJava() { t = new TestJava(); }
                }
                """
                ,"/tt/TestJava.java:12: verify: The prover cannot establish an assertion (UndefinedNullDeReference) in method m",23

                );
    }

    /** Tests String concatenation - whether the result in Java is non-null. */
    @Test
    public void testStringConcat1() {
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {
                  public TestJava t;
                  public int a;
                  public static int b;
                  public void m(String s, String ss) {
                       //@ assume s.length() + ss.length() <= Integer.MAX_VALUE;
                       String sss =  (s + ss);
                       //@ assert sss != null;
                  }
                  //@ public normal_behavior
                  //@   ensures t != null;
                  public TestJava() { t = new TestJava(); }
                }
                """
                );
    }

    /** Tests String concatenation - whether the result, computed in JML, is non-null*/
    @Test
    public void testStringConcat1a() {
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {
                  public TestJava t;
                  public int a;
                  public static int b;
                  public void m(String s, String ss) {
                       //@ assume s.length() + ss.length() <= Integer.MAX_VALUE;
                       //@ assert (s + ss) != null;
                  }
                  //@ public normal_behavior
                  //@   ensures t != null;
                 public TestJava() { t = new TestJava(); }
                }
                """
                );
    }

    /** Tests String concatenation */
    @Test
    public void testStringConcat2() {
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {
                  public void m(String s, String ss) {
                       //@ assume s.length() + ss.length() <= Integer.MAX_VALUE;
                       String sss = s + ss;
                       String s4 = s + ss;
                       //@ assert sss.equals(s4);
                  }
                }
                """
                );
    }

    /** Tests String concatenation */
    @Test
    public void testStringConcat2a() {
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {
                  public void m(String s, String ss) {
                       //@ assume s.length() + ss.length() <= Integer.MAX_VALUE;
                       // @ assert s.concat(ss).equals(s.concat(ss));
                       //@ assert (s+ss).equals(s+ss);
                  }
                }
                """
                );
    }

    /** Tests String concatenation */
    @Test
    public void testStringConcat3() {
        addOptions("-escMaxWarnings=1");
        addOptions("-method=m");
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {

                  public int a;
                  public static int b;
                  public void m(String s, String ss) {
                       //@ assume s.length() + ss.length() <= Integer.MAX_VALUE;
                       boolean b = (s + ss) == (s + ss); //@ assert b;
                  }
                }
                """
                ,"/tt/TestJava.java:9: verify: The prover cannot establish an assertion (Assert) in method m",46
                );
    }

    /** Tests String concatenation */
    @Test
    public void testStringConcat3a() {
        addOptions("-escMaxWarnings=1");
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {
                  public TestJava t;
                  public int a;
                  public static int b;
                  public void m(String s, String ss) {
                       //@ assume s.length() + ss.length() <= Integer.MAX_VALUE;
                       //@ assert (s + ss) == (s + ss);
                  }
                  //@ public normal_behavior
                  //@   ensures t != null;
                  public TestJava() { t = new TestJava(); }
                }
                """
                ,"/tt/TestJava.java:9: verify: The prover cannot establish an assertion (Assert) in method m",12
                );
    }

    /** Tests String charAt operation */
    @Test
    public void testStringCharAt1q() {
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {
                  //@ requires s.length() > 0;
                  public void m(String s) {
                       //@ assert s.charAt(0) == s.charAt(0);
                  }
                }
                """
                );
    }

    /** Tests String charAt operation */
    @Test
    public void testStringCharAt1() {
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {
                  public TestJava t;
                  public int a;
                  public static int b;
                  public void m(String s) {
                       //@ assert s.charAt(0) == s.charAt(0);
                  }
                  //@ public normal_behavior
                  //@   ensures t != null;
                  public TestJava() { t = new TestJava(); }
                }
                """
                ,"/tt/TestJava.java:8: verify: The prover cannot establish an assertion (UndefinedCalledMethodPrecondition) in method m",27
                ,"$SPECS/java/lang/String.jml:298: verify: Associated declaration",46
                ,optional(seq("$SPECS/java/lang/CharSequence.jml:65: verify: Precondition conjunct is false: 0 <= index < chars.length",34))
                );
    }

    /** Tests String charAt operation */
    @Test
    public void testStringCharAt2() {
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {
                  public TestJava t;
                  public int a;
                  public static int b;
                  //@ requires s.length() > 0;
                  public void m(String s) {
                       String ss = s;
                       //@ assert s.charAt(0) == ss.charAt(0);
                  }
                  //@ public normal_behavior
                  //@   ensures t != null;
                  public TestJava() { t = new TestJava(); }
                }
                """
                );
    }

    /** Tests String charAt operation */
    @Test
    public void testStringCharAt3() {
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {
                  //@ requires s.length() > 0;
                  public void m(String s, String ss) {
                       //@ assert s.charAt(0) == ss.charAt(0);
                  }
                }
                """
                ,anyorder(
                        seq("/tt/TestJava.java:6: verify: The prover cannot establish an assertion (Assert) in method m",12)
                        ,seq("/tt/TestJava.java:6: verify: The prover cannot establish an assertion (UndefinedCalledMethodPrecondition) in method m",43
                             ,"$SPECS/java/lang/String.jml:298: verify: Associated declaration",46
                             ,optional("$SPECS/java/lang/CharSequence.jml:62: verify: Precondition conjunct is false: 0 <= index < chars.length",34)
                                 // FIXME - why does the above sometime occur and sometimes not
                                )
                        )
                );
    }

    /** Tests String charAt operation */
    @Test
    public void testStringCharAt3a() {
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {
                  public TestJava t;
                  public int a;
                  public static int b;
                  //@ requires s.length() > 0 && ss.length() > 0;
                  public void m(String s, String ss) {
                       //@ assert s.charAt(0) == ss.charAt(0);
                  }
                  //@ public normal_behavior
                  //@   ensures t != null;
                  public TestJava() { t = new TestJava(); }
                }
                """
                ,"/tt/TestJava.java:9: verify: The prover cannot establish an assertion (Assert) in method m",12
                );
    }

    /** Tests String length operation */
    @Test
    public void testStringLength1() {
    	addOptions("-method=m");
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {
                  public int a;
                  public static int b;
                  public void m(String s) {
                       boolean b = s.length() >= 0; //@ assert b;
                  }
                }
                """
                );
    }

    /** Tests String length operation */
    @Test
    public void testStringLength1a() {
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {
                  public int a;
                  public static int b;
                  public void m(String s) {
                       //@ assert s.length() >= 0;
                  }
                }
                """
                );
    }

    /** Tests String length operation */
    @Test
    public void testStringLength2() {
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {
                  public int a;
                  public static int b;
                  public void m(String s) {
                       String ss = s;
                       boolean b = s.length() == ss.length(); //@ assert b;
                  }
                }
                """
                );
    }

    /** Tests String length operation */
    @Test
    public void testStringLength2a() {
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {
                  public int a;
                  public static int b;
                  public void m(String s) {
                       String ss = s;
                       //@ assert s.length() == ss.length();
                  }
                }
                """
                );
    }

    /** Tests String length operation */
    @Test
    public void testStringLength3() {
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {
                  public int a;
                  public static int b;
                  public void m(String s, String ss) {
                       boolean b = s.length() == ss.length(); //@ assert b;
                  }
                }
                """
                ,"/tt/TestJava.java:7: verify: The prover cannot establish an assertion (Assert) in method m",51
                );
    }

    /** Tests String length operation */
    @Test
    public void testStringLength3a() {
        helpEsc("tt.TestJava",
                """
                package tt;
                 import org.jmlspecs.annotation.*;
                @NonNullByDefault public class TestJava {
                  public int a;
                  public static int b;
                  public void m(String s, String ss) {
                       //@ assert s.length() == ss.length();
                  }
                }
                """
                ,"/tt/TestJava.java:7: verify: The prover cannot establish an assertion (Assert) in method m",12
                );
    }

    /** String + int — lhs is String, rhs is not; exercises valueOf branch then concat.
     *  In dev-21, findStaticMember("concat") returns null (concat is not static),
     *  emitting an internal error "Could not find the concat method". */
    @Test
    public void testStringConcatNonStringRhs() {
        helpEsc("tt.A", """
                package tt;
                import org.jmlspecs.annotation.*;
                public class A {
                    //@ ensures \\result != null;
                    public static /*@ pure */ String label(/*@ non_null */ String prefix, int n) {
                        return prefix + n;
                    }
                }
                """);
    }

    /** int + String — lhs is not String; exercises valueOf branch for lhs. */
    @Test
    public void testStringConcatNonStringLhs() {
        helpEsc("tt.A", """
                package tt;
                import org.jmlspecs.annotation.*;
                public class A {
                    //@ ensures \\result != null;
                    public static /*@ pure */ String numbered(int n, /*@ non_null */ String suffix) {
                        return n + suffix;
                    }
                }
                """);
    }

    /** String + String literal — simplest possible concat; checks no internal error. */
    @Test
    public void testStringConcatBothStrings() {
        helpEsc("tt.A", """
                package tt;
                import org.jmlspecs.annotation.*;
                public class A {
                    //@ ensures \\result != null;
                    public static /*@ pure */ String greet(/*@ non_null */ String name) {
                        return "Hello, " + name;
                    }
                }
                """);
    }

    /** Nullable String + nullable String — result type is unannotated String,
     *  so that.type.equals(syms.stringType) is true and the concat encoding path
     *  is entered. In dev-21 findStaticMember("concat") returns null (concat is
     *  not static), emitting an internal error. */
    @Test
    public void testStringConcatNullableInputs() {
        helpEsc("tt.A", """
                package tt;
                import org.jmlspecs.annotation.*;
                public class A {
                    public static String concat(/*@ nullable */ String a, /*@ nullable */ String b) {
                        return a + b;
                    }
                }
                """);
    }

    /** Nullable String + int — lhs is nullable, result type is unannotated String. */
    @Test
    public void testStringConcatNullableStringPlusInt() {
        helpEsc("tt.A", """
                package tt;
                import org.jmlspecs.annotation.*;
                public class A {
                    public static String label(/*@ nullable */ String prefix, int n) {
                        return prefix + n;
                    }
                }
                """);
    }

    // FIXME - also test interning

    /** Patch I-A (ISSUE-1415) POSITIVE: with String.length() bounded above,
     *  index-plus-small-constant arithmetic over a String index is provably
     *  non-overflowing.  Both shapes are the ones FinModel's
     *  DialectNormalizationRegistry hits (:307 and :592/:851). */
    @Test
    public void testStringLengthUpperBound() {
        helpEsc("tt.A", """
                package tt;
                public class A {
                    //@ requires 0 <= i && i <= s.length();
                    //@ ensures \\result == (i + 6 > s.length());
                    public static /*@ pure */ boolean m(/*@ non_null */ String s, int i) {
                        return i + 6 > s.length();
                    }
                    //@ requires 0 <= start && start < s.length();
                    public static /*@ pure */ int openParen(/*@ non_null */ String s, int start) {
                        return start + "OFFSET".length();
                    }
                }
                """);
    }

    /** Patch I-A (ISSUE-1415) NEGATIVE CONTROL: the added bound must not become a
     *  rubber stamp.  An absurd ceiling, and a ceiling one unit tighter than the
     *  declared one, must both still be rejected. */
    @Test
    public void testStringLengthUpperBoundIsNotVacuous() {
        helpEsc("tt.A", """
                package tt;
                public class A {
                    public static void m(/*@ non_null */ String s) {
                        //@ assert s.length() <= 1000;
                    }
                    public static void mTight(/*@ non_null */ String s) {
                        //@ assert s.length() <= Integer.MAX_VALUE - 9;
                    }
                }
                """
                ,"/tt/A.java:4: verify: The prover cannot establish an assertion (Assert) in method m",13
                ,"/tt/A.java:7: verify: The prover cannot establish an assertion (Assert) in method mTight",13
                );
    }

    /** Patch I-B (ISSUE-1416) POSITIVE: a loop-carried StringBuilder accumulator
     *  reset with setLength(0) is admissible under a named object-level loop frame.
     *  Fails with `Assignable: ... \\everything` without the patch. */
    @Test
    public void testStringBuilderSetLengthLoopFrame() {
        helpEsc("tt.A", """
                package tt;
                public class A {
                    public static /*@ non_null */ String m() {
                        StringBuilder cur = new StringBuilder();
                        //@ loop_writes cur.objectState;
                        //@ loop_invariant 0 <= i;
                        for (int i = 0; i < 3; i++) {
                            cur.append('x');
                            cur.setLength(0);
                        }
                        return cur.toString();
                    }
                }
                """);
    }

    /** Patch I-B (ISSUE-1416) NEGATIVE CONTROL: the new frame must be genuinely
     *  scoped, not merely non-empty.  setLength must NOT havoc an unrelated object.
     *  This method fails without the patch (setLength defaults to \\everything),
     *  which is precisely what makes it a control rather than a tautology. */
    @Test
    public void testStringBuilderSetLengthFrameIsScoped() {
        helpEsc("tt.A", """
                package tt;
                public class A {
                    public static class Box { public int v; }
                    public static void m(/*@ non_null */ StringBuilder a,
                                         /*@ non_null */ Box b) {
                        b.v = 5;
                        a.setLength(0);
                        //@ assert b.v == 5;
                    }
                }
                """);
    }

}
