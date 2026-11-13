package org.jmlspecs.openjmltest.testsuites;

import java.util.Collection;

import org.jmlspecs.openjmltest.EscBase;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.openjml.runners.ParameterizedWithNames;

/** This class of JUnit tests checks various uses of generic types.
 * @author David R. Cok
 *
 */
@org.junit.FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
@RunWith(ParameterizedWithNames.class)
public class escgeneric extends EscBase {

    @Override
    public void setUp() throws Exception {
        //print = true;
        //noCollectDiagnostics = true;
        super.setUp();
        addOptions("--nullable-by-default"); // Because the tests were written this way
        //JmlEsc.escdebug = false;
        addOptions("--timeout=30");
    }
    
    @Test
    public void testConstructor() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  public void mx(Integer i) {
                    Object oo = new TestG<Integer>(i);
                  }
                  public void ma(Object o) {
                    Object oo = new TestG<Object>(o);
                  }
                }
                class TestG<E> {
                  //@ requires \\type(E) != \\type(Integer) ;
                  //@ pure
                  public TestG(E i) {}
                }
                """
                ,"/tt/TestJava.java:4: verify: The prover cannot establish an assertion (Precondition) in method mx",17
                ,"/tt/TestJava.java:13: verify: Associated declaration",10
                ,"/tt/TestJava.java:11: verify: Precondition conjunct is false: \\type(E) != \\type(Integer)",25
                );
    }
    
    /** Tests that we can reason about the result of \\typeof */
    @Test
    public void testTypeOf() {
    	addOptions("--check-feasibility=all");  // Part of test
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  public void m(Integer i) {
                    //@ assert \\typeof(this) <:= \\type(TestJava);
                  }
                  public void ma(Object o) {
                    //@ assume \\typeof(this) == \\type(Object);
                    //@ assert false;
                  }
                  public void mb(Object o) {
                    //@ assert \\typeof(this) == \\type(Object);
                  }
                }
                """
                ,"/tt/TestJava.java:7: verify: There is no feasible path to program point after explicit assume statement in method tt.TestJava.ma(java.lang.Object)",9
                ,"/tt/TestJava.java:8: verify: There is no feasible path to program point before explicit assert statement in method tt.TestJava.ma(java.lang.Object)",9
                // Feasibility points as currently emitted (cf. feasibility.java): an 'at implicit return'
                // check, and 'at program exit' located at the method's closing brace
                ,"/tt/TestJava.java:9: verify: There is no feasible path to program point at implicit return in method tt.TestJava.ma(java.lang.Object)",3
                ,"/tt/TestJava.java:9: verify: There is no feasible path to program point at program exit in method tt.TestJava.ma(java.lang.Object)",3
                ,"/tt/TestJava.java:11: verify: The prover cannot establish an assertion (Assert) in method mb",9
                );
    }

    @Test
    public void testGenericType() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava<T> extends B<T> {
                  public void ma(T i) {
                  }
                }
                class A<T> extends TestJava<B<T>> {
                  public void mb(T i) {
                  }
                }
                class B<E> {}
                class C<F> extends java.util.LinkedList<B<F>> {}
                """
        );
    }
    
    @Test
    public void testGenericType2() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava<T extends B> {
                  public void m(T i) {
                    //@ assume i != null;
                    //@ check  i instanceof Object;
                    //@ check  \\typeof(i) <:= \\type(Object);
                    //@ check  \\erasure(\\typeof(i)) <:= \\erasure(\\type(Object));
                    //@ check  \\typeof(i) <:= \\type(T);
                    //@ check  i instanceof B;
                    //@ check  \\erasure(\\typeof(i)) <:= \\erasure(\\type(B));
                    //@ check  \\typeof(i) <:= \\type(B);
                    //@ check  \\erasure(\\typeof(i)) <:= \\erasure(\\type(C));
                  }
                  /*@ public normal_behavior ensures true; pure */ public TestJava() {}
                }
                class B {}
                class C extends TestJava<B> {}
                """
                ,"/tt/TestJava.java:12: verify: The prover cannot establish an assertion (Assert) in method m",9
        );
    }
    
    @Test
    public void testGenericType2a() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava<T extends B> {
                  public void m(T i) {
                    //@ assume i != null;
                    //@ assert i instanceof Object;
                    //@ assert \\typeof(i) <:= \\type(C);
                  }
                  /*@ public normal_behavior ensures true; pure */ public TestJava() {}
                }
                class B {}
                class C extends TestJava<B> {}
                """
                ,"/tt/TestJava.java:6: verify: The prover cannot establish an assertion (Assert) in method m",9
        );
    }
    
    @Test
    public void testGenericType2b() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava<T extends B> {
                  public void m(T i) {
                    //@ assume i != null;
                    //@ assert i instanceof Object;
                    //@ assert \\type(T) <:= \\type(B);
                    //@ assert \\type(T) <:= \\type(C);
                  }
                  /*@ public normal_behavior ensures true; pure */ public TestJava() {}
                }
                class B {}
                class C extends TestJava<B> {}
                """
                ,"/tt/TestJava.java:7: verify: The prover cannot establish an assertion (Assert) in method m",9
        );
    }
    
    @Test
    public void testGenericType1() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava<T extends B> {
                  public void m(Integer i) {
                    //@ assert Object.class == java.lang.Object.class;
                    //@ assert \\type(TestJava<Integer>) != \\type(Object);
                    //@ assert \\type(TestJava<Integer>) != \\type(TestJava<Object>);
                  }
                  public void mz(Object o) {
                    //@ assert Object.class == \\erasure(\\type(T));
                  }
                  public void ma(Object o) {
                    //@ assert \\type(TestJava<Integer>) == \\type(TestJava<T>);
                  }
                  public void mb(Object o) {
                    //@ assert \\typeof(this) == \\type(Object);
                  }
                  public void mc(Object o) {
                    //@ assert \\type(TestJava<Integer>) == \\type(TestJava<Object>);
                  }
                  public void mz1(Object o) {
                    //@ assert Object.class != \\erasure(\\type(T));
                  }
                  public TestJava() {}
                }
                class B {}
                class C {}
                """
                ,"/tt/TestJava.java:9: verify: The prover cannot establish an assertion (Assert) in method mz",9
                ,"/tt/TestJava.java:12: verify: The prover cannot establish an assertion (Assert) in method ma",9
                ,"/tt/TestJava.java:15: verify: The prover cannot establish an assertion (Assert) in method mb",9
                ,"/tt/TestJava.java:18: verify: The prover cannot establish an assertion (Assert) in method mc",9
        );
    }

    @Test
    public void testStatic() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  public void ma(Integer i) {
                    TestG.<Integer>mm(i);
                  }
                  public void mb(Object o) {
                    TestG.<Object>mm(o);
                  }
                }
                class TestG {
                  //@ requires \\type(E) != \\type(Integer) ;
                  //@ pure
                  public static <E> void mm(E t) {}
                }
                """
                ,"/tt/TestJava.java:4: verify: The prover cannot establish an assertion (Precondition) in method ma",22
                ,"/tt/TestJava.java:13: verify: Associated declaration",26
                ,"/tt/TestJava.java:11: verify: Precondition conjunct is false: \\type(E) != \\type(Integer)",25
        );
    }

    @Test
    public void testStaticB() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  public void ma(Integer i) {
                    TestG.mm(i);
                  }
                  public void mb(Object o) {
                    TestG.mm(o);
                  }
                }
                class TestG {
                  //@ requires \\type(E) != \\type(Integer) ;
                  //@ pure
                  public static <E> void mm(E t) {}
                }
                """
                ,"/tt/TestJava.java:4: verify: The prover cannot establish an assertion (Precondition) in method ma",13
                ,"/tt/TestJava.java:13: verify: Associated declaration",26
                ,"/tt/TestJava.java:11: verify: Precondition conjunct is false: \\type(E) != \\type(Integer)",25
        );
    }

    @Test
    public void testStatic2() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  public void ma(Integer i) {
                    TestG.<Integer>mm(i);
                  }
                  public void mb(Object o) {
                    TestG.<Object>mm(o);
                  }
                }
                class TestG {
                  //@ requires \\type(E) == \\type(Integer) ;
                  //@ pure
                  public static <E> void mm(E t) {}
                }
                """
                ,"/tt/TestJava.java:7: verify: The prover cannot establish an assertion (Precondition) in method mb",21
                ,"/tt/TestJava.java:13: verify: Associated declaration",26
                ,"/tt/TestJava.java:11: verify: Precondition conjunct is false: \\type(E) == \\type(Integer)",25
        );
    }

    @Test
    public void testStatic2B() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  public void ma(Integer i) {
                    TestG.mm(i);
                  }
                  public void mb(Object o) {
                    TestG.mm(o);
                  }
                }
                class TestG {
                  //@ requires \\type(E) == \\type(Integer) ;
                  //@ pure
                  public static <E> void mm(E t) {}
                }
                """
                ,"/tt/TestJava.java:7: verify: The prover cannot establish an assertion (Precondition) in method mb",13
                ,"/tt/TestJava.java:13: verify: Associated declaration",26
                ,"/tt/TestJava.java:11: verify: Precondition conjunct is false: \\type(E) == \\type(Integer)",25
        );
    }

    @Test
    public void testTypeParameter() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  public void ma(/*@ non_null*/ TestG<Integer> i, Integer j) {
                    i.mm(j);
                  }
                  public void mb(/*@ non_null*/ TestG<Object> i, Object j) {
                    i.mm(j);
                  }
                }
                class TestG<E> {
                    //@ requires \\type(E) != \\type(Integer);
                    //@ pure
                    public void mm(E t) {}
                }
                """
                ,"/tt/TestJava.java:4: verify: The prover cannot establish an assertion (Precondition) in method ma",9
                ,"/tt/TestJava.java:13: verify: Associated declaration",17
                ,"/tt/TestJava.java:11: verify: Precondition conjunct is false: \\type(E) != \\type(Integer)",27
                );
    }
    
    @Test
    public void testTypeParameter2a() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  public void ma(TestG<Integer>./*@ non_null*/TestH i, Integer j) {
                    i.mm(j);
                  }
                  public void mb(TestG<Object>./*@ non_null*/TestH i, Object j) {
                    i.mm(j);
                  }
                  public void mc(TestG<String>./*@ non_null*/TestH i, String j) {
                    i.mm(j);
                  }
                }
                class TestG<E> {
                  class TestH  {
                    //@ requires \\type(E) != \\type(Integer);
                    //@ pure
                    public void mm(E t) {}
                  }
                }
                """
                ,"/tt/TestJava.java:4: verify: The prover cannot establish an assertion (Precondition) in method ma",9
                ,"/tt/TestJava.java:17: verify: Associated declaration",17
                ,"/tt/TestJava.java:15: verify: Precondition conjunct is false: \\type(E) != \\type(Integer)",27
                );
    }

    @Test
    public void testTypeParameter2b() {
        helpEsc("tt.TestJava",
                """
                package tt;import org.jmlspecs.annotation.*;
                public class TestJava {
                  public void ma(TestG<Integer>.@NonNull TestH i, Integer j) {
                    i.mm(j);
                  }
                  public void mb(TestG<Object>.@NonNull TestH i, Object j) {
                    i.mm(j);
                  }
                  public void mc(TestG<String>.@NonNull TestH i, String j) {
                    i.mm(j);
                  }
                }
                class TestG<E> {
                  class TestH  {
                    //@ requires \\type(E) != \\type(Integer);
                    //@ pure
                    public void mm(E t) {}
                  }
                }
                """
                ,"/tt/TestJava.java:4: verify: The prover cannot establish an assertion (Precondition) in method ma",9
                ,"/tt/TestJava.java:17: verify: Associated declaration",17
                ,"/tt/TestJava.java:15: verify: Precondition conjunct is false: \\type(E) != \\type(Integer)",27
                );
    }
    
    @Test
    public void testTypeParameter2c() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  public void ma(/*@ non_null*/TestG<Integer>.TestH i, Integer j) {
                    i.mm(j);
                  }
                  public void mb(/*@ non_null*/TestG<Object>.TestH i, Object j) {
                    i.mm(j);
                  }
                  public void mc(/*@ non_null*/TestG<String>.TestH i, String j) {
                    i.mm(j);
                  }
                }
                class TestG<E> {
                  class TestH  {
                    //@ requires \\type(E) != \\type(Integer);
                    //@ pure
                    public void mm(E t) {}
                  }
                }
                """
                ,"/tt/TestJava.java:4: verify: The prover cannot establish an assertion (Precondition) in method ma",9
                ,"/tt/TestJava.java:17: verify: Associated declaration",17
                ,"/tt/TestJava.java:15: verify: Precondition conjunct is false: \\type(E) != \\type(Integer)",27
                );
    }

    @Test
    public void testTypeParameter2d() {
        helpEsc("tt.TestJava",
                """
                package tt; import org.jmlspecs.annotation.*;
                public class TestJava {
                  public void ma(@NonNull TestG<Integer>.TestH i, Integer j) {
                    i.mm(j);
                  }
                  public void mb(TestG<Object>.@Nullable TestH i, Object j) {
                    i.mm(j);
                  }
                  public void mc(TestG<String>.@NonNull TestH i, String j) {
                    i.mm(j);
                  }
                }
                class TestG<E> {
                  class TestH  {
                    //@ requires \\type(E) != \\type(Integer);
                    //@ pure
                    public void mm(E t) {}
                  }
                }
                """
                ,"/tt/TestJava.java:4: verify: The prover cannot establish an assertion (Precondition) in method ma",9
                ,"/tt/TestJava.java:17: verify: Associated declaration",17
                ,"/tt/TestJava.java:15: verify: Precondition conjunct is false: \\type(E) != \\type(Integer)",27
                ,"/tt/TestJava.java:7: verify: The prover cannot establish an assertion (PossiblyNullDeReference) in method mb",6
                );
    }
        
    @Test
    public void testTypeParameter2e() {
        expectedExit = 1;
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  public void ma(TestG<Integer>./*@ qqq*/TestH i, Integer j) {
                    i.mm(j);
                  }
                }
                class TestG<E> {
                  class TestH  {
                    //@ requires \\type(E) != \\type(Integer);
                    //@ pure
                    public void mm(E t) {}
                  }
                }
                """
                ,"/tt/TestJava.java:3: error: Expected an identifier, found end of JML comment instead", 40
                ,"/tt/TestJava.java:3: error: Did not expect an identifier following this formal parameter; perhaps a modifier is misspelled and thought to be a type: TestG<Integer>.qqq", 40
                );
    }
    
    @Test
    public void testTypeParameter2f() {
        expectedExit = 1;
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  public void ma(TestG<Integer>./*@ final */TestH i, Integer j) {
                    i.mm(j);
                  }
                }
                class TestG<E> {
                  class TestH  {
                    //@ requires \\type(E) != \\type(Integer);
                    //@ pure
                    public void mm(E t) {}
                  }
                }
                """
                ,"/tt/TestJava.java:3: error: <identifier> expected", 36
                ,"/tt/TestJava.java:3: error: ',', ')', or '[' expected", 42
                );
    }
    
    @Test
    public void testTypeParameter2g() {
        expectedExit = 1;
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  public void ma(TestG<Integer>./*@ pure */TestH i, Integer j) {
                    i.mm(j);
                  }
                }
                class TestG<E> {
                  class TestH  {
                    //@ requires \\type(E) != \\type(Integer);
                    //@ pure
                    public void mm(E t) {}
                  }
                }
                """
                ,"/tt/TestJava.java:3: error: A pure modifier is not allowed where type annotations are expected", 37
                );
    }
    
    @Test
    public void testTypeParameter2h() {
        expectedExit = 1;
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  public void ma(TestG<Integer>./*@ public */TestH i, Integer j) {
                    i.mm(j);
                  }
                }
                class TestG<E> {
                  class TestH  {
                    public void mm(E t) {}
                  }
                }
                """
                ,"/tt/TestJava.java:3: error: <identifier> expected", 36
                ,"/tt/TestJava.java:3: error: ',', ')', or '[' expected", 43
                );
    }
    
    @Test
    public void testTypeParameter2k1() {
        expectedExit = 1;
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  public void ma(/*@ public */Object i) { }
                }
                """
                ,"/tt/TestJava.java:3: error: modifier public not allowed here", 31
                );
    }
    
    @Test
    public void testTypeParameter2k2() {
        expectedExit = 1;
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  public void mb(/*@ pure */Object i) { }
                }
                """
                ,"/tt/TestJava.java:3: error: This JML modifier is not allowed for a formal parameter", 22
                );
    }
    
    @Test
    public void testTypeParameter2k3() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  public void mc(/*@ final */Object i) { }
                  public void md(/*@ non_null */Object i) { }
                }
                """
                );
    }
    
    @Test
    public void testTypeParameter2k4() {
        expectedExit = 1;
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  public void me(/*@ qqq */Object i) { }
                }
                """
                ,"/tt/TestJava.java:3: error: Expected an identifier, found end of JML comment instead", 26
                ,"/tt/TestJava.java:3: error: Did not expect an identifier following this formal parameter; perhaps a modifier is misspelled and thought to be a type: qqq", 26
                );
    }
        
    @Test
    public void testUnboxing() {
        addOptions("--method=m");  // Just test method m
        helpEsc("tt.TestJava",
                """
                 class A { void m(/*@non_null*/ Integer ooo) {
                int sum = 0;
                { /*@ assume ooo >= 0; */ sum += ooo; }
                //@ assert sum >= 0;
                }}
                """
                );
    }

    @Test
    public void testForEach3() {
        helpEsc("tt.TestJava",
                """
                 class A {  /*@ spec_bigint_math */ void m(java.util./*@ non_null*/ List<Integer> list) {
                int sum = 0;
                //@ assert sum == 0;
                //@ loop_invariant sum >= 0;
                for (Integer o: list) { /*@ assume o != null && o >= 0 && sum + o <= Integer.MAX_VALUE; */ sum += o; }
                //@ assert sum >= 0;
                }}
                """
                );
    }

    @Test
    public void testForEach3c() {
    	//addOptions("-show","-method=m");
    	// nullable by default so list might be null
        helpEsc("tt.TestJava",
                """
                 class A {  /*@ spec_bigint_math */ void m(java.util.List<Integer> list) {
                int sum = 0;
                //@ assert sum == 0;
                //@ loop_invariant sum >= 0;
                for (Integer o: list) { /*@ assume o != null && o >= 0 && sum + o <= Integer.MAX_VALUE; */ sum += o; }
                //@ assert sum >= 0;
                }}
                """
                ,"/tt/TestJava.java:5: verify: The prover cannot establish an assertion (UndefinedNullDeReference) in method m",17
                );
    }

    @Test
    public void testForEach3a() {
        helpEsc("tt.TestJava",
                """
                 class A { /*@ code_bigint_math spec_bigint_math */ void m(java.util./*@ non_null*/ List</*@ non_null*/ Integer> list) {
                int sum = 0;
                //@ loop_invariant sum >= 0;
                for (int o: list) { /*@ assume o >= 0; */ sum += o; }
                //@ assert sum >= 0;
                }}
                """
                );
    }

    @Test
    public void testForEach3bad() {
        helpEsc("tt.TestJava",
                """
                 class A { /*@ code_bigint_math spec_bigint_math */ void m(java.util./*@ non_null*/ List<Integer> list) {
                int sum = 0;
                //@ loop_invariant sum >= 0;
                for (int o: list) { /*@ assume o >= 0; */ sum += o; }
                //@ assert sum >= 0;
                }}
                """
                ,"/tt/TestJava.java:4: verify: The prover cannot establish an assertion (PossiblyNullUnbox) in method m",10
                );
    }

    @Test
    public void testElemType() {
        helpEsc("tt.TestJava",
                """
                 class A { void m(char/*@ non_null */ [] a) {
                //@ assert \\elemtype(\\typeof(a)) == \\type(char);
                }}
                """
                );
    }

    @Test
    public void testElemType2() {
        helpEsc("tt.TestJava",
                """
                 class A { void m(char /*@ non_null */ [] a) {
                //@ assert \\elemtype(\\typeof(a)) == \\type(int);
                }}
                """
                ,"/tt/TestJava.java:2: verify: The prover cannot establish an assertion (Assert) in method m",5
                );
    }

    @Test
    public void testElemType3() {
        helpEsc("tt.TestJava",
                """
                 class A { void m(/*@ non_null */ char[] a) {
                //@ assert \\elemtype(\\typeof(a)) == \\type(int);
                }}
                """
                ,"/tt/TestJava.java:1: warning: the annotation /*@ non_null */ is in the wrong position: the element type char cannot take it; it is used as if written on the array type: char /*@ non_null */ []", 23
                ,"/tt/TestJava.java:2: verify: The prover cannot establish an assertion (Assert) in method m",5
                );
    }

    @Test
    public void testElemType4() {
        helpEsc("tt.TestJava",
                """
                 class A { void m(char /*@ non_null */ [] a) {
                //@ assert \\elemtype(\\typeof(a)) == \\type(int);
                }}
                """
                ,"/tt/TestJava.java:2: verify: The prover cannot establish an assertion (Assert) in method m",5
                );
    }

    @Test
    public void testGenericThrow() {
        addOptions("--method=rt"); // Just test method rt
        helpEsc("tt.TestJava",
                """
                public class TestJava {
                 //@ public exceptional_behavior
                 //@   requires true;
                 public static <T extends Throwable> RuntimeException rt(/*@ non_null*/ Throwable t) throws T { throw (T)t; }
                }
                """
                );
    }

    /** FinModel patch C1 (ISSUE-442/1488/1593): conditionals (including nested ones) in
     *  method-argument position are poly expressions attributed speculatively; they used to abort
     *  with 'AssertionError: isSameType/isSubtype DEFERRED'. ESC must run and give the normal,
     *  sound verdicts -- 'bad' is a negative control that must still fail. */
    @Test
    public void testPolyConditionalArgs() {
        helpEsc("tt.TestJava",
                """
                package tt;
                import java.util.List;
                public class TestJava {
                  //@ ensures \\result == o;
                  //@ pure
                  static Object id(Object o) { return o; }
                  //@ ensures \\result == l;
                  //@ pure
                  static List<String> idl(List<String> l) { return l; }
                  //@ ensures \\result == (b ? x : y);
                  public Object g(boolean b, Object x, Object y) { return id(b ? x : y); }
                  //@ ensures \\result == x;
                  public Object bad(boolean b, Object x, Object y) { return id(b ? x : y); }
                  //@ requires c != null;
                  //@ ensures \\result == c;
                  public List<String> h(List<String> c) { return idl(c == null ? List.of() : c); }
                  //@ requires a != null && b != null;
                  //@ ensures \\result == (k == 0 ? a : k == 1 ? b : a);
                  public List<String> n(int k, List<String> a, List<String> b) { return idl(k == 0 ? a : k == 1 ? b : a); }
                }
                """
                ,"/tt/TestJava.java:13: verify: The prover cannot establish an assertion (Postcondition) in method bad",54
                ,"/tt/TestJava.java:12: verify: Associated declaration",7
                );
    }

    @Test
    public void testQualifiedGenericSupertype() {
        // A nested class whose supertype is a class-qualified generic type crashed ESC (EmptyStackException)
        helpEsc("tt.TestJava",
                """
                package tt;
                import java.util.AbstractMap;
                public class TestJava {
                  static class B extends AbstractMap.SimpleEntry<String,String> {
                    B() { super("a", "b"); }
                  }
                }
                """
                );
    }

    /** Patch V (ISSUE-3052), family 1: two allocations of one spec-less generic class with different type arguments must not make the state contradictory. */
    @Test
    public void testGenericTypeArgsDeadStateBox() {
        helpEsc("tt.TestJava",
                """
                package tt;
                class Box<T> {}
                public class TestJava {
                  public static void bad() {
                    Box<Object> a = new Box<Object>();
                    Box<String> b = new Box<String>();
                    //@ assert false;
                  }
                  public static void badInt() {
                    Box<Object> a = new Box<Object>();
                    Box<Integer> b = new Box<Integer>();
                    //@ assert false;
                  }
                }
                """
                ,"/tt/TestJava.java:7: verify: The prover cannot establish an assertion (Assert) in method bad",9
                ,"/tt/TestJava.java:12: verify: The prover cannot establish an assertion (Assert) in method badInt",9
                );
    }

    /** Patch V (ISSUE-3052), family 1: ArrayList with different type arguments (instance, static, diamond). */
    @Test
    public void testGenericTypeArgsDeadStateArrayList() {
        helpEsc("tt.TestJava",
                """
                package tt;
                import java.util.ArrayList;
                import java.util.List;
                public class TestJava {
                  public void s1() {
                    List<Object> a = new ArrayList<Object>();
                    List<String> b = new ArrayList<String>();
                    //@ assert false;
                  }
                  public static void st1() {
                    List<Object> a = new ArrayList<Object>();
                    List<String> b = new ArrayList<String>();
                    //@ assert false;
                  }
                  public static void diamond() {
                    List<Object> a = new ArrayList<Object>();
                    List<String> b = new ArrayList<>();
                    //@ assert false;
                  }
                  public static void strObj() {
                    List<String> a = new ArrayList<String>();
                    List<Object> b = new ArrayList<Object>();
                    //@ assert false;
                  }
                }
                """
                ,"/tt/TestJava.java:8: verify: The prover cannot establish an assertion (Assert) in method s1",9
                ,"/tt/TestJava.java:13: verify: The prover cannot establish an assertion (Assert) in method st1",9
                ,"/tt/TestJava.java:18: verify: The prover cannot establish an assertion (Assert) in method diamond",9
                ,"/tt/TestJava.java:23: verify: The prover cannot establish an assertion (Assert) in method strObj",9
                );
    }

    /** Patch V (ISSUE-3052), family 1: two classes sharing the type-variable name T, different arguments. */
    @Test
    public void testGenericTypeArgsDeadStateSharedTypeVarName() {
        helpEsc("tt.TestJava",
                """
                package tt;
                class BoxT<T> {}
                class BagT<T> {}
                public class TestJava {
                  public static void sameNameBad() {
                    BoxT<Object> a = new BoxT<Object>();
                    BagT<String> b = new BagT<String>();
                    //@ assert false;
                  }
                }
                """
                ,"/tt/TestJava.java:8: verify: The prover cannot establish an assertion (Assert) in method sameNameBad",9
                );
    }

    /** Patch V (ISSUE-3052), family 1: ArrayList and HashSet (both declare E), different arguments. */
    @Test
    public void testGenericTypeArgsDeadStateListSet() {
        helpEsc("tt.TestJava",
                """
                package tt;
                import java.util.ArrayList;
                import java.util.HashSet;
                import java.util.List;
                import java.util.Set;
                public class TestJava {
                  public static void jdkListSet() {
                    List<Object> a = new ArrayList<Object>();
                    Set<String> b = new HashSet<String>();
                    //@ assert false;
                  }
                }
                """
                ,"/tt/TestJava.java:10: verify: The prover cannot establish an assertion (Assert) in method jdkListSet",9
                );
    }

    /** Patch V (ISSUE-3052), family 1: two Optional.of results with different arguments. */
    @Test
    public void testGenericTypeArgsDeadStateOptional() {
        helpEsc("tt.TestJava",
                """
                package tt;
                import java.util.Optional;
                public class TestJava {
                  public static void optional(Object o) {
                    Optional<Object> a = Optional.of(o);
                    Optional<String> b = Optional.of("s");
                    //@ assert false;
                  }
                }
                """
                ,"/tt/TestJava.java:7: verify: The prover cannot establish an assertion (Assert) in method optional",9
                );
    }

    /** Patch V (ISSUE-3052), family 2 control: same type argument twice stays refuted. */
    @Test
    public void testGenericTypeArgsControlSameArgBox() {
        helpEsc("tt.TestJava",
                """
                package tt;
                class Box<T> {}
                public class TestJava {
                  public static void good() {
                    Box<Object> a = new Box<Object>();
                    Box<Object> b = new Box<Object>();
                    //@ assert false;
                  }
                  public static void goodString() {
                    Box<String> a = new Box<String>();
                    Box<String> b = new Box<String>();
                    //@ assert false;
                  }
                }
                """
                ,"/tt/TestJava.java:7: verify: The prover cannot establish an assertion (Assert) in method good",9
                ,"/tt/TestJava.java:12: verify: The prover cannot establish an assertion (Assert) in method goodString",9
                );
    }

    /** Patch V (ISSUE-3052), family 2 control: ArrayList same argument, and a single allocation. */
    @Test
    public void testGenericTypeArgsControlSameArgArrayList() {
        helpEsc("tt.TestJava",
                """
                package tt;
                import java.util.ArrayList;
                import java.util.List;
                public class TestJava {
                  public static void s1c() {
                    List<Object> a = new ArrayList<Object>();
                    List<Object> b = new ArrayList<Object>();
                    //@ assert false;
                  }
                  public static void single() {
                    List<String> b = new ArrayList<String>();
                    //@ assert false;
                  }
                }
                """
                ,"/tt/TestJava.java:8: verify: The prover cannot establish an assertion (Assert) in method s1c",9
                ,"/tt/TestJava.java:12: verify: The prover cannot establish an assertion (Assert) in method single",9
                );
    }

    /** Patch V (ISSUE-3052), family 2 control: shared name same argument; different type-variable name. */
    @Test
    public void testGenericTypeArgsControlSameNameAndOtherName() {
        helpEsc("tt.TestJava",
                """
                package tt;
                class BoxT<T> {}
                class BagT<T> {}
                class BagU<U> {}
                public class TestJava {
                  public static void sameNameCtl() {
                    BoxT<Object> a = new BoxT<Object>();
                    BagT<Object> b = new BagT<Object>();
                    //@ assert false;
                  }
                  public static void otherName() {
                    BoxT<Object> a = new BoxT<Object>();
                    BagU<String> b = new BagU<String>();
                    //@ assert false;
                  }
                }
                """
                ,"/tt/TestJava.java:9: verify: The prover cannot establish an assertion (Assert) in method sameNameCtl",9
                ,"/tt/TestJava.java:14: verify: The prover cannot establish an assertion (Assert) in method otherName",9
                );
    }

    /** Patch V (ISSUE-3052), family 2 control: ArrayList and HashSet, same argument. */
    @Test
    public void testGenericTypeArgsControlListSetSameArg() {
        helpEsc("tt.TestJava",
                """
                package tt;
                import java.util.ArrayList;
                import java.util.HashSet;
                import java.util.List;
                import java.util.Set;
                public class TestJava {
                  public static void jdkListSetCtl() {
                    List<Object> a = new ArrayList<Object>();
                    Set<Object> b = new HashSet<Object>();
                    //@ assert false;
                  }
                }
                """
                ,"/tt/TestJava.java:10: verify: The prover cannot establish an assertion (Assert) in method jdkListSetCtl",9
                );
    }

    /** Patch V (ISSUE-3052), family 2 control: Optional.of twice, same argument. */
    @Test
    public void testGenericTypeArgsControlOptionalSameArg() {
        helpEsc("tt.TestJava",
                """
                package tt;
                import java.util.Optional;
                public class TestJava {
                  public static void optionalCtl(Object o) {
                    Optional<Object> a = Optional.of(o);
                    Optional<Object> b = Optional.of(o);
                    //@ assert false;
                  }
                }
                """
                ,"/tt/TestJava.java:7: verify: The prover cannot establish an assertion (Assert) in method optionalCtl",9
                );
    }

    /** Patch V (ISSUE-3052), family 2 control: one allocation mixed with a parameter, field, method result or cast of the other argument stays live. */
    @Test
    public void testGenericTypeArgsControlMixedSources() {
        helpEsc("tt.TestJava",
                """
                package tt;
                class Box<T> {}
                public class TestJava {
                  static Box<Object> fObj = new Box<Object>();
                  static Box<String> fStr = new Box<String>();
                  //@ ensures true;
                  static Box<Object> mkObj() { return new Box<Object>(); }
                  public static void parParBad(Box<Object> a, Box<String> b) {
                    //@ assert false;
                  }
                  public static void parNewBad(Box<Object> a) {
                    Box<String> b = new Box<String>();
                    //@ assert false;
                  }
                  public static void parNewCtl(Box<Object> a) {
                    Box<Object> b = new Box<Object>();
                    //@ assert false;
                  }
                  public static void fieldFieldBad() {
                    Box<Object> a = fObj;
                    Box<String> b = fStr;
                    //@ assert false;
                  }
                  public static void resNewBad() {
                    Box<Object> a = mkObj();
                    Box<String> b = new Box<String>();
                    //@ assert false;
                  }
                  public static void resNewCtl() {
                    Box<Object> a = mkObj();
                    Box<Object> b = new Box<Object>();
                    //@ assert false;
                  }
                  @SuppressWarnings("unchecked")
                  public static void castNewBad(Object o) {
                    if (o instanceof Box) {
                      Box<Object> a = (Box<Object>) o;
                      Box<String> b = new Box<String>();
                      //@ assert false;
                    }
                  }
                  @SuppressWarnings("unchecked")
                  public static void castNewCtl(Object o) {
                    if (o instanceof Box) {
                      Box<Object> a = (Box<Object>) o;
                      Box<Object> b = new Box<Object>();
                      //@ assert false;
                    }
                  }
                }
                """
                ,"/tt/TestJava.java:9: verify: The prover cannot establish an assertion (Assert) in method parParBad",9
                ,"/tt/TestJava.java:13: verify: The prover cannot establish an assertion (Assert) in method parNewBad",9
                ,"/tt/TestJava.java:17: verify: The prover cannot establish an assertion (Assert) in method parNewCtl",9
                ,"/tt/TestJava.java:22: verify: The prover cannot establish an assertion (Assert) in method fieldFieldBad",9
                ,"/tt/TestJava.java:27: verify: The prover cannot establish an assertion (Assert) in method resNewBad",9
                ,"/tt/TestJava.java:32: verify: The prover cannot establish an assertion (Assert) in method resNewCtl",9
                ,"/tt/TestJava.java:39: verify: The prover cannot establish an assertion (Assert) in method castNewBad",11
                ,"/tt/TestJava.java:47: verify: The prover cannot establish an assertion (Assert) in method castNewCtl",11
                );
    }

    /** Patch V (ISSUE-3052), family 3: true assertions after allocations with different type arguments must still be proved (no warning). */
    @Test
    public void testGenericTypeArgsNonVacuous() {
        helpEsc("tt.TestJava",
                """
                package tt;
                import java.util.ArrayList;
                import java.util.List;
                class Box<T> {}
                public class TestJava {
                  public static void nonVacuous() {
                    Box<Object> a = new Box<Object>();
                    Box<String> b = new Box<String>();
                    //@ assert a != null;
                    //@ assert b != null;
                  }
                  public static void nonVacuousList() {
                    List<Object> a = new ArrayList<Object>();
                    List<String> b = new ArrayList<String>();
                    //@ assert a != null && b != null;
                  }
                }
                """
                );
    }

    /** Patch X (ISSUE-3126): allocating a generic subclass that binds its parent's type argument must not make the state contradictory. */
    @Test
    public void testGenericBoundParentArgDeadStateNew() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  static class Box<T> {}
                  static class S<X> extends Box<String> {}
                  public static void sNew() {
                    S<Integer> s = new S<Integer>();
                    //@ assert false;
                  }
                }
                """
                ,"/tt/TestJava.java:7: verify: The prover cannot establish an assertion (Assert) in method sNew",9
                );
    }

    /** Patch X (ISSUE-3126): parent-typed declaration initialised from the binding subclass, static and instance. */
    @Test
    public void testGenericBoundParentArgDeadStateDecl() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  static class Box<T> {}
                  static class S<X> extends Box<String> {}
                  public static void sDecl() {
                    Box<String> b = new S<Integer>();
                    //@ assert false;
                  }
                  public void iDecl() {
                    Box<String> b = new S<Integer>();
                    //@ assert false;
                  }
                }
                """
                ,"/tt/TestJava.java:7: verify: The prover cannot establish an assertion (Assert) in method sDecl",9
                ,"/tt/TestJava.java:11: verify: The prover cannot establish an assertion (Assert) in method iDecl",9
                );
    }

    /** Patch X (ISSUE-3126) control: same type argument stays refuted. */
    @Test
    public void testGenericBoundParentArgControlSameArg() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  static class Box<T> {}
                  static class S<X> extends Box<String> {}
                  public static void sSame() {
                    S<String> s = new S<String>();
                    //@ assert false;
                  }
                }
                """
                ,"/tt/TestJava.java:7: verify: The prover cannot establish an assertion (Assert) in method sSame",9
                );
    }

    /** Patch X (ISSUE-3126) control: two Box allocations with different arguments (Patch V shape) stay refuted. */
    @Test
    public void testGenericBoundParentArgControlBoxPair() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  static class Box<T> {}
                  static class S<X> extends Box<String> {}
                  public static void boxPair() {
                    Box<Object> a = new Box<Object>();
                    Box<String> b = new Box<String>();
                    //@ assert false;
                  }
                }
                """
                ,"/tt/TestJava.java:8: verify: The prover cannot establish an assertion (Assert) in method boxPair",9
                );
    }

    /** Patch X (ISSUE-3126): true assertions about the binding subclass must be proved (no warning). */
    @Test
    public void testGenericBoundParentArgNonVacuity() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public class TestJava {
                  static class Box<T> {}
                  static class S<X> extends Box<String> {}
                  public static void nonVac() {
                    S<Integer> s = new S<Integer>();
                    Box<String> b = s;
                    //@ assert \\typeof(s) == \\type(S<Integer>);
                    //@ assert \\typeof(s) <: \\type(Box<String>);
                    //@ assert \\typeof(b) <: \\type(Box<String>);
                  }
                }
                """
                );
    }
}
