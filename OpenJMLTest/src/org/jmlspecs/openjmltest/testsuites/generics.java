package org.jmlspecs.openjmltest.testsuites;

import org.jmlspecs.openjmltest.TCBase;
import org.junit.Test;

@org.junit.FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
public class generics extends TCBase {

    /** Test something very simple with no errors*/
    @Test
    public void testSimpleGeneric() {
        addMockFile("$A/A.jml","public class A<T> { /*@ non_null*/ T t; /*@ non_null pure*/ T item(); }");
        helpTCText("A.java","public class A<T> { T t; T item() { return t; }}");
    }

    /** Test with a binary class*/
    @Test
    public void testBinaryGeneric() {
        addOptions("--specs-path", "$A:$B:$CP");
        addMockFile("$A/java/util/ListIterator.jml","package java.util; public interface ListIterator<E> extends java.util.Iterator<E> { /*@ also public behavior requires true; */ @Override public boolean hasNext(); }");
        helpTCText("A.java","public class A<X> { java.util.ListIterator<X> t() { return null; }}");
    }

    /** Test mismatched type parameters*/
    @Test
    public void testSimpleGeneric1() {
        addMockFile("$A/A.jml","public class A {  }");
        helpTCText("A.java","public class A<T> { T t; T item() { return t; }}"
                ,"/$A/A.jml:1: error: The type A in the specification matches a Java type A<T> with a different number of type arguments",8
                ,"/A.java:1: error: Associated declaration: /$A/A.jml:1:",8
                );
    }

    /** Test with a binary class*/
    @Test
    public void testBinaryGeneric2() {
        addOptions("--specs-path", "$A:$B:$CP");
        addMockFile("$A/java/util/ListIterator.jml","package java.util;\npublic interface ListIterator extends java.lang.Iterator { /*@ public behavior requires true; */ public boolean hasNext(); }");
        helpTCText("A.java","public class A<X> { java.util.ListIterator<X> t; }"
                ,"/$A/java/util/ListIterator.jml:2: error: The type ListIterator in the specification matches a Java type java.util.ListIterator<E> with a different number of type arguments",8
                );
    }

    /** Test with a binary class*/ // OK
    @Test
    public void testBinaryGeneric3() {
        addOptions("--specs-path", "$A:$B:$CP");
        addMockFile("$A/java/util/ListIterator.jml","package java.util;\npublic interface ListIterator<E> extends java.util.Iterator<E> {  }");
        helpTCText("A.java","public class A<X> { java.util.ListIterator<X> t; }"
                );
    }

    /** Test with a binary class - type name not matching*/ // FIXME -- with Z not found, the model field \seq<Z> still shows symbols without errors, but isJmlType() is false
    @Test
    public void testBinaryGeneric3c() {
        addOptions("--specs-path", "$A:$B:$CP");
        addMockFile("$A/java/util/ListIterator.jml","package java.util;\npublic interface ListIterator<E> extends java.util.Iterator<Z> {  }");
        helpTCText("A.java","public class A<X> { java.util.ListIterator<X> t; }"
                ,"/$A/java/util/ListIterator.jml:2: error: cannot find symbol\n  symbol: class Z",61
                );
    }

    /** Test with a binary class - mismatched names*/
    @Test
    public void testBinaryGeneric3b() {
        addOptions("--specs-path", "$A:$B:$CP");
        addMockFile("$A/java/util/ListIterator.jml","package java.util;\npublic interface ListIterator<Z> extends java.util.Iterator<Z> {  }");
        helpTCText("A.java","public class A<X> { java.util.ListIterator<X> t; }"
                ,"/$A/java/util/ListIterator.jml:2: error: The specification type named ListIterator (java.util.ListIterator) has a type parameter named Z but the Java declaration has that type parameter named E",31
                );
    }

    /** Test with a binary class -- wrong package*/
    @Test
    public void testBinaryGeneric3a() {
        addOptions("--specs-path", "$A:$B:$CP");
        addMockFile("$A/java/util/ListIterator.jml","public interface ListIterator<Z> extends java.util.Iterator<Z> {  }");
        helpTCText("A.java","public class A<X> { java.util.ListIterator<X> t; }"
                ,"/$A/java/util/ListIterator.jml:1: error: Specification package does not match Java package: unnamed package vs. java.util",2
                );
    }

    /** Test with a binary class*/
    @Test
    public void testBinaryGeneric4() {
        addOptions("--specs-path", "$A:$B:$CP");
        addMockFile("$A/java/util/ListIterator.jml","package java.util;\npublic interface ListIterator<E,Z> extends java.util.Iterator<E> {  }");
        helpTCText("A.java","public class A<X> { java.util.ListIterator<X> t; }"
                ,"/$A/java/util/ListIterator.jml:2: error: The type ListIterator<E,Z> in the specification matches a Java type java.util.ListIterator<E> with a different number of type arguments",8
                );
    }
    
    @Test
    public void testMethod() {
        helpTCText("A.java",
                """
                public class A<X> {
                  <T>T doit(T t) { return t; } }
                """
                );

    }

    @Test
    public void testMethod2() {
        addMockFile("$A/java/util/Vector.jml","package java.util;\npublic class Vector<E> extends java.util.AbstractList<E> implements java.util.List<E>, java.util.RandomAccess, java.lang.Cloneable, java.io.Serializable { \npublic <T> T[] toArray(T[] t); }");
        helpTCText("A.java","public class A<X> { java.util.Vector<X> t; }"
// OK in Java8                ,"/$A/java/util/Vector.jml:3: error: The method toArray in the specification matches a Java method <T>toArray(T[]) with different modifiers: synchronized",16
                );
        
    }
    
    @Test
    public void testForEach1() {
        helpTCText("A.java",
                """
                 class A { void m(java.util.List<Integer> list) {
                 //@ loop_invariant o != null; decreasing 6;
                 for (Integer o: list) {}
                }}
                """
                );
    }


    @Test
    public void testForEach2() {
        helpTCText("A.java",
                """
                 class A { void m(Integer[] list) {
                 //@ loop_invariant o != 0; decreasing 6;
                 for (Integer o: list) {}
                }}
                """
                );
    }


    @Test
    public void testForEach3() {
        helpTCText("A.java",
                """
                 class A { void m(java.util.List<Integer> list) {
                 //@ loop_invariant o != 0; decreasing 6;
                 for (int o: list) {}
                }}
                """
                );
    }

    @Test
    public void testForEach4() {
        helpTCText("A.java",
                """
                 class A { void m(Integer[] list) {
                 //@ loop_invariant o != 0; decreasing 6;
                 for (int o: list) {}
                }}
                """
                );
    }

    /** Patch H (OpenJML#367) Case A: source-companion generic method, unbounded, matched arity.
     *  Both .java and its .jml companion declare <T> T id(T x); pre-patch this emitted a
     *  spurious "T vs. T" result-type mismatch (spec-side TypeVar != java-side TypeVar by identity).
     *  Post-patch: verifies with ZERO errors. */
    @Test
    public void testGenericMethodCompanionUnbounded() {
        addMockFile("$A/A.jml","public class A { <T> T id(T x); }");
        helpTCText("A.java","public class A { <T> T id(T x) { return x; } }");
    }

    /** Patch H (OpenJML#367) Case B: source-companion generic method, bounded, matched arity.
     *  Same shape as Case A but with a bounded type parameter <T extends Comparable<T>>.
     *  Post-patch: verifies with ZERO errors. */
    @Test
    public void testGenericMethodCompanionBounded() {
        addMockFile("$A/A.jml","public class A { <T extends Comparable<T>> T id(T x); }");
        helpTCText("A.java","public class A { <T extends Comparable<T>> T id(T x) { return x; } }");
    }

    /** Patch H (OpenJML#367) Case C (reviewer-requested edge): UNEQUAL type-param arity, both sides generic.
     *  The .jml companion declares <T> T id(T x) but the .java method is <T,U> T id(T x) (java has an
     *  extra type parameter U the spec does not declare). This MUST still produce an error post-patch
     *  (never a false PASS) and MUST NOT crash -- proving the substitution guard does not paper over a
     *  genuine arity mismatch. Analysis: matchMethod() resolves with 1 explicit type-arg against a
     *  2-type-param java method, so resolution finds no applicable match and the "no method to match"
     *  branch fires BEFORE the types.subst guard is reached.
     *  NOTE (build-pending): the exact expected-error text + column below were derived by static
     *  analysis of JmlMemberEnter.java line 339 (utils.error ... "There is no method to match this Java
     *  declaration in the specification file: " + sourceDecl.sym + "." + specMethodDecl.sym); they were
     *  NOT captured from a compiler run (patch-apply dispatch does not build). Confirm/adjust the string
     *  and column at build time. If instead this case compiles with ZERO errors, that reveals
     *  resolveMethod tolerating the arity mismatch and the patch would need an explicit
     *  from.length()==to.length() guard -- surface that rather than relaxing this assertion. */
    @Test
    public void testGenericMethodCompanionArityMismatch() {
        addMockFile("$A/A.jml","public class A { <T> T id(T x); }");
        helpTCText("A.java","public class A { <T,U> T id(T x) { return x; } }"
                ,"/$A/A.jml:1: error: There is no method to match this Java declaration in the specification file: A.<T>id(T)",24
                );
    }
}
