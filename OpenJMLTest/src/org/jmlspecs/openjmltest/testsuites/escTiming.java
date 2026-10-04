package org.jmlspecs.openjmltest.testsuites;

import org.jmlspecs.openjmltest.EscBase;
import org.junit.*;

// FIXME- should these report untaken branches?
// FIXME - why are these timing tests?
@org.junit.FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
public class escTiming extends EscBase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        addOptions("--nullable-by-default"); // Because the tests were written this way
    }

    @Test
    public void testTimingIf() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public abstract class TestJava {
                  //@ requires 0<=i && i <10;
                  public void f1(int i) {
                    int sum = 0; int j = 0;
                    if (p(i,j++) == 0) sum+=8; else sum +=7;
                    if (q(i,j++) == 0) sum+=8; else sum +=7;
                    if (p(i,j++) == 0) sum+=3; else sum +=7;
                    if (q(i,j++) == 0) sum+=4; else sum +=7;
                    //@ assert sum < 1000;
                  }
                  //@ requires 0<=i && i <10;
                  public void f1a(int i) {
                    int sum = 0; int j = 0;
                    if (p(i,j++) == 0) sum+=8; else sum +=7;
                    if (q(i,j++) == 0) sum+=8; else sum +=7;
                    if (p(i,j++) == 0) sum+=3; else sum +=7;
                    if (q(i,j++) == 0) sum+=4; else sum +=7;
                    if (p(i,j++) == 0) sum+=5; else sum +=7;
                    if (q(i,j++) == 0) sum+=6; else sum +=7;
                    //@ assert sum < 1000;
                  }
                  //@ requires 0<=i && i <10;
                  public void f2(int i) {
                    int sum = 0; int j = 0;
                    if (p(i,j++) == 0) sum+=8; else sum +=7;
                    if (q(i,j++) == 0) sum+=8; else sum +=7;
                    if (p(i,j++) == 0) sum+=3; else sum +=7;
                    if (q(i,j++) == 0) sum+=4; else sum +=7;
                    if (p(i,j++) == 0) sum+=5; else sum +=7;
                    if (q(i,j++) == 0) sum+=6; else sum +=7;
                    if (p(i,j++) == 0) sum+=9; else sum +=7;
                    if (q(i,j++) == 0) sum+=2; else sum +=7;
                    //@ assert sum < 1000;
                  }
                  //@ requires 0<=i && i <10;
                  public void f2a(int i) {
                    int sum = 0; int j = 0;
                    if (p(i,j++) == 0) sum+=8; else sum +=7;
                    if (q(i,j++) == 0) sum+=8; else sum +=7;
                    if (p(i,j++) == 0) sum+=3; else sum +=7;
                    if (q(i,j++) == 0) sum+=4; else sum +=7;
                    if (p(i,j++) == 0) sum+=5; else sum +=7;
                    if (q(i,j++) == 0) sum+=6; else sum +=7;
                    if (p(i,j++) == 0) sum+=9; else sum +=7;
                    if (q(i,j++) == 0) sum+=2; else sum +=7;
                    if (p(i,j++) == 0) sum+=3; else sum +=7;
                    //@ assert sum < 1000;
                  }
                  //@ requires 0<=i && i <10;
                  public void f3(int i) {
                    int sum = 0; int j = 0;
                    if (p(i,j++) == 0) sum+=8; else sum +=7;
                    if (q(i,j++) == 0) sum+=8; else sum +=7;
                    if (p(i,j++) == 0) sum+=3; else sum +=7;
                    if (q(i,j++) == 0) sum+=4; else sum +=7;
                    if (p(i,j++) == 0) sum+=5; else sum +=7;
                    if (q(i,j++) == 0) sum+=6; else sum +=7;
                    if (p(i,j++) == 0) sum+=9; else sum +=7;
                    if (q(i,j++) == 0) sum+=2; else sum +=7;
                    if (p(i,j++) == 0) sum+=3; else sum +=7;
                    if (q(i,j++) == 0) sum+=8; else sum +=7;
                    //@ assert sum < 1000;
                  }
                  //@ requires 0<=i && i <10;
                  public void f3a(int i) {
                    int sum = 0; int j = 0;
                    if (p(i,j++) == 0) sum+=8; else sum +=7;
                    if (q(i,j++) == 0) sum+=8; else sum +=7;
                    if (p(i,j++) == 0) sum+=3; else sum +=7;
                    if (q(i,j++) == 0) sum+=4; else sum +=7;
                    if (p(i,j++) == 0) sum+=5; else sum +=7;
                    if (q(i,j++) == 0) sum+=6; else sum +=7;
                    if (p(i,j++) == 0) sum+=9; else sum +=7;
                    if (q(i,j++) == 0) sum+=2; else sum +=7;
                    if (p(i,j++) == 0) sum+=3; else sum +=7;
                    if (q(i,j++) == 0) sum+=8; else sum +=7;
                    if (p(i,j++) == 0) sum+=8; else sum +=7;
                    //@ assert sum < 1000;
                  }
                  //@ requires 0<=i && i <10;
                  public void f4(int i) {
                    int sum = 0; int j = 0;
                    if (p(i,j++) == 0) sum+=8; else sum +=7;
                    if (q(i,j++) == 0) sum+=8; else sum +=7;
                    if (p(i,j++) == 0) sum+=3; else sum +=7;
                    if (q(i,j++) == 0) sum+=4; else sum +=7;
                    if (p(i,j++) == 0) sum+=5; else sum +=7;
                    if (q(i,j++) == 0) sum+=6; else sum +=7;
                    if (p(i,j++) == 0) sum+=9; else sum +=7;
                    if (q(i,j++) == 0) sum+=2; else sum +=7;
                    if (p(i,j++) == 0) sum+=3; else sum +=7;
                    if (q(i,j++) == 0) sum+=8; else sum +=7;
                    if (p(i,j++) == 0) sum+=8; else sum +=7;
                    if (q(i,j++) == 0) sum+=8; else sum +=7;
                    //@ assert sum < 1000;
                  }
                  //@ ensures \\result >= 0 && \\result < 10;
                  abstract int p(int i, int j);
                  //@ ensures \\result == 0 ;
                  abstract int q(int i, int j);
                }
                """
//                ,"/tt/TestJava.java:7: warning: else branch apparently never taken in method f1(int)",9
//                ,"/tt/TestJava.java:9: warning: else branch apparently never taken in method f1(int)",9
//                ,"/tt/TestJava.java:16: warning: else branch apparently never taken in method f1a(int)",9
//                ,"/tt/TestJava.java:18: warning: else branch apparently never taken in method f1a(int)",9
//                ,"/tt/TestJava.java:20: warning: else branch apparently never taken in method f1a(int)",9
//                ,"/tt/TestJava.java:27: warning: else branch apparently never taken in method f2(int)",9
//                ,"/tt/TestJava.java:29: warning: else branch apparently never taken in method f2(int)",9
//                ,"/tt/TestJava.java:31: warning: else branch apparently never taken in method f2(int)",9
//                ,"/tt/TestJava.java:33: warning: else branch apparently never taken in method f2(int)",9
//                ,"/tt/TestJava.java:40: warning: else branch apparently never taken in method f2a(int)",9
//                ,"/tt/TestJava.java:42: warning: else branch apparently never taken in method f2a(int)",9
//                ,"/tt/TestJava.java:44: warning: else branch apparently never taken in method f2a(int)",9
//                ,"/tt/TestJava.java:46: warning: else branch apparently never taken in method f2a(int)",9
//                ,"/tt/TestJava.java:54: warning: else branch apparently never taken in method f3(int)",9
//                ,"/tt/TestJava.java:56: warning: else branch apparently never taken in method f3(int)",9
//                ,"/tt/TestJava.java:58: warning: else branch apparently never taken in method f3(int)",9
//                ,"/tt/TestJava.java:60: warning: else branch apparently never taken in method f3(int)",9
//                ,"/tt/TestJava.java:62: warning: else branch apparently never taken in method f3(int)",9
//                ,"/tt/TestJava.java:69: warning: else branch apparently never taken in method f3a(int)",9
//                ,"/tt/TestJava.java:71: warning: else branch apparently never taken in method f3a(int)",9
//                ,"/tt/TestJava.java:73: warning: else branch apparently never taken in method f3a(int)",9
//                ,"/tt/TestJava.java:75: warning: else branch apparently never taken in method f3a(int)",9
//                ,"/tt/TestJava.java:77: warning: else branch apparently never taken in method f3a(int)",9
//                ,"/tt/TestJava.java:85: warning: else branch apparently never taken in method f4(int)",9
//                ,"/tt/TestJava.java:87: warning: else branch apparently never taken in method f4(int)",9
//                ,"/tt/TestJava.java:89: warning: else branch apparently never taken in method f4(int)",9
//                ,"/tt/TestJava.java:91: warning: else branch apparently never taken in method f4(int)",9
//                ,"/tt/TestJava.java:93: warning: else branch apparently never taken in method f4(int)",9
//                ,"/tt/TestJava.java:95: warning: else branch apparently never taken in method f4(int)",9
                );
    }

    @Test
    public void testTimingSwitch() {
        helpEsc("tt.TestJava",
                """
                package tt;
                public abstract class TestJava {
                  //@ requires 0<=i && i <10;
                  public void m1(int i) {
                    int sum = 0;
                    switch (i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 100; break;
                     }
                  //@ assume i < 10;
                    //@ assert sum < 100;
                  }
                  //@ requires 0<=i && i <10;
                  public void m2(int i) {
                    int sum = 0;
                    switch (i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 100; break;
                     }
                  //@ assume i < 10;
                    switch (p(i,1)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    //@ assert sum < 100;
                  }
                  //@ requires 0<=i && i <10;
                  public void m3(int i) {
                    int sum = 0;
                    switch (i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 100; break;
                     }
                  //@ assume i < 10;
                    switch (p(i,1)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (p(i,2)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    //@ assert sum < 100;
                  }
                  //@ requires 0<=i && i <10;
                  public void m4(int i) {
                    int sum = 0;
                    switch (i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 100; break;
                     }
                  //@ assume i < 10;
                    switch (p(i,1)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (p(i,2)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (10-i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    //@ assert sum < 100;
                  }
                  //@ requires 0<=i && i <10;
                  public void m4a(int i) {
                    int sum = 0;
                    switch (i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 100; break;
                     }
                  //@ assume i < 10;
                    switch (p(i,1)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (p(i,2)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (10-i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (p(i,3)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      default: sum += 0; break;
                     }
                    //@ assert sum < 100;
                   }
                  //@ requires 0<=i && i <10;
                  public void m4b(int i) {
                    int sum = 0;
                    switch (i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 100; break;
                     }
                  //@ assume i < 10;
                    switch (p(i,1)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (p(i,2)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (10-i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (p(i,3)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      default: sum += 0; break;
                     }
                    //@ assert sum < 100;
                   }
                  //@ requires 0<=i && i <10;
                  public void m4c(int i) {
                    int sum = 0;
                    switch (i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 100; break;
                     }
                  //@ assume i < 10;
                    switch (p(i,1)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (p(i,2)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (10-i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (p(i,3)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      default: sum += 0; break;
                     }
                    //@ assert sum < 100;
                   }
                  //@ requires 0<=i && i <10;
                  public void m4d(int i) {
                    int sum = 0;
                    switch (i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 100; break;
                     }
                  //@ assume i < 10;
                    switch (p(i,1)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (p(i,2)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (10-i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (p(i,3)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      default: sum += 0; break;
                     }
                    //@ assert sum < 100;
                  }
                  //@ requires 0<=i && i <10;
                  public void m5(int i) {
                    int sum = 0;
                    switch (i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 100; break;
                     }
                  //@ assume i < 10;
                    switch (p(i,1)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (p(i,2)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (10-i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (p(i,3)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    //@ assert sum < 100;
                  }
                  //@ requires 0<=i && i <10;
                  public void m5a(int i) {
                    int sum = 0;
                    switch (i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 100; break;
                     }
                  //@ assume i < 10;
                    switch (p(i,1)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (p(i,2)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (10-i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (p(i,3)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (10-p(i,4)) {
                      case 0: sum += 8; break;
                      default: sum += 0; break;
                     }
                    //@ assert sum < 100;
                  }
                  //@ requires 0<=i && i <10;
                  public void m5b(int i) {
                    int sum = 0;
                    switch (i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 100; break;
                     }
                  //@ assume i < 10;
                    switch (p(i,1)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (p(i,2)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (10-i) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (p(i,3)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      case 2: sum += 1; break;
                      case 3: sum += 4; break;
                      case 4: sum += 2; break;
                      case 5: sum += 9; break;
                      case 6: sum += 0; break;
                      case 7: sum += 7; break;
                      case 8: sum += 3; break;
                      case 9: sum += 5; break;
                      case 10: sum += 5; break;
                      default: sum += 0; break;
                     }
                    switch (10-p(i,4)) {
                      case 0: sum += 8; break;
                      case 1: sum += 6; break;
                      default: sum += 0; break;
                     }
                    //@ assert sum < 100;
                  }
                  //@ ensures \\result >= 0 && \\result < 10;
                  abstract int p(int i, int j);
                }
                """
//                ,"/tt/TestJava.java:17: warning: Switch case apparently never taken in method m1(int)",7
//                ,"/tt/TestJava.java:18: warning: Switch case apparently never taken in method m1(int)",7
//                ,"/tt/TestJava.java:37: warning: Switch case apparently never taken in method m2(int)",7
//                ,"/tt/TestJava.java:38: warning: Switch case apparently never taken in method m2(int)",7
//                ,"/tt/TestJava.java:52: warning: Switch case apparently never taken in method m2(int)",7
//                ,"/tt/TestJava.java:53: warning: Switch case apparently never taken in method m2(int)",7
//                ,"/tt/TestJava.java:71: warning: Switch case apparently never taken in method m3(int)",7
//                ,"/tt/TestJava.java:72: warning: Switch case apparently never taken in method m3(int)",7
//                ,"/tt/TestJava.java:86: warning: Switch case apparently never taken in method m3(int)",7
//                ,"/tt/TestJava.java:87: warning: Switch case apparently never taken in method m3(int)",7
//                ,"/tt/TestJava.java:100: warning: Switch case apparently never taken in method m3(int)",7
//                ,"/tt/TestJava.java:101: warning: Switch case apparently never taken in method m3(int)",7
//                ,"/tt/TestJava.java:119: warning: Switch case apparently never taken in method m4(int)",7
//                ,"/tt/TestJava.java:120: warning: Switch case apparently never taken in method m4(int)",7
//                ,"/tt/TestJava.java:134: warning: Switch case apparently never taken in method m4(int)",7
//                ,"/tt/TestJava.java:135: warning: Switch case apparently never taken in method m4(int)",7
//                ,"/tt/TestJava.java:148: warning: Switch case apparently never taken in method m4(int)",7
//                ,"/tt/TestJava.java:149: warning: Switch case apparently never taken in method m4(int)",7
//                ,"/tt/TestJava.java:152: warning: Switch case apparently never taken in method m4(int)",7
//                ,"/tt/TestJava.java:163: warning: Switch case apparently never taken in method m4(int)",7
//                ,"/tt/TestJava.java:181: warning: Switch case apparently never taken in method m4a(int)",7
//                ,"/tt/TestJava.java:182: warning: Switch case apparently never taken in method m4a(int)",7
//                ,"/tt/TestJava.java:196: warning: Switch case apparently never taken in method m4a(int)",7
//                ,"/tt/TestJava.java:197: warning: Switch case apparently never taken in method m4a(int)",7
//                ,"/tt/TestJava.java:210: warning: Switch case apparently never taken in method m4a(int)",7
//                ,"/tt/TestJava.java:211: warning: Switch case apparently never taken in method m4a(int)",7
//                ,"/tt/TestJava.java:214: warning: Switch case apparently never taken in method m4a(int)",7
//                ,"/tt/TestJava.java:225: warning: Switch case apparently never taken in method m4a(int)",7
//                ,"/tt/TestJava.java:248: warning: Switch case apparently never taken in method m4b(int)",7
//                ,"/tt/TestJava.java:249: warning: Switch case apparently never taken in method m4b(int)",7
//                ,"/tt/TestJava.java:263: warning: Switch case apparently never taken in method m4b(int)",7
//                ,"/tt/TestJava.java:264: warning: Switch case apparently never taken in method m4b(int)",7
//                ,"/tt/TestJava.java:277: warning: Switch case apparently never taken in method m4b(int)",7
//                ,"/tt/TestJava.java:278: warning: Switch case apparently never taken in method m4b(int)",7
//                ,"/tt/TestJava.java:281: warning: Switch case apparently never taken in method m4b(int)",7
//                ,"/tt/TestJava.java:292: warning: Switch case apparently never taken in method m4b(int)",7
//                ,"/tt/TestJava.java:318: warning: Switch case apparently never taken in method m4c(int)",7
//                ,"/tt/TestJava.java:319: warning: Switch case apparently never taken in method m4c(int)",7
//                ,"/tt/TestJava.java:333: warning: Switch case apparently never taken in method m4c(int)",7
//                ,"/tt/TestJava.java:334: warning: Switch case apparently never taken in method m4c(int)",7
//                ,"/tt/TestJava.java:347: warning: Switch case apparently never taken in method m4c(int)",7
//                ,"/tt/TestJava.java:348: warning: Switch case apparently never taken in method m4c(int)",7
//                ,"/tt/TestJava.java:351: warning: Switch case apparently never taken in method m4c(int)",7
//                ,"/tt/TestJava.java:362: warning: Switch case apparently never taken in method m4c(int)",7
//                ,"/tt/TestJava.java:390: warning: Switch case apparently never taken in method m4d(int)",7
//                ,"/tt/TestJava.java:391: warning: Switch case apparently never taken in method m4d(int)",7
//                ,"/tt/TestJava.java:405: warning: Switch case apparently never taken in method m4d(int)",7
//                ,"/tt/TestJava.java:406: warning: Switch case apparently never taken in method m4d(int)",7
//                ,"/tt/TestJava.java:419: warning: Switch case apparently never taken in method m4d(int)",7
//                ,"/tt/TestJava.java:420: warning: Switch case apparently never taken in method m4d(int)",7
//                ,"/tt/TestJava.java:423: warning: Switch case apparently never taken in method m4d(int)",7
//                ,"/tt/TestJava.java:434: warning: Switch case apparently never taken in method m4d(int)",7
//                ,"/tt/TestJava.java:464: warning: Switch case apparently never taken in method m5(int)",7
//                ,"/tt/TestJava.java:465: warning: Switch case apparently never taken in method m5(int)",7
//                ,"/tt/TestJava.java:479: warning: Switch case apparently never taken in method m5(int)",7
//                ,"/tt/TestJava.java:480: warning: Switch case apparently never taken in method m5(int)",7
//                ,"/tt/TestJava.java:493: warning: Switch case apparently never taken in method m5(int)",7
//                ,"/tt/TestJava.java:494: warning: Switch case apparently never taken in method m5(int)",7
//                ,"/tt/TestJava.java:497: warning: Switch case apparently never taken in method m5(int)",7
//                ,"/tt/TestJava.java:508: warning: Switch case apparently never taken in method m5(int)",7
//                ,"/tt/TestJava.java:521: warning: Switch case apparently never taken in method m5(int)",7
//                ,"/tt/TestJava.java:522: warning: Switch case apparently never taken in method m5(int)",7
//                ,"/tt/TestJava.java:540: warning: Switch case apparently never taken in method m5a(int)",7
//                ,"/tt/TestJava.java:541: warning: Switch case apparently never taken in method m5a(int)",7
//                ,"/tt/TestJava.java:555: warning: Switch case apparently never taken in method m5a(int)",7
//                ,"/tt/TestJava.java:556: warning: Switch case apparently never taken in method m5a(int)",7
//                ,"/tt/TestJava.java:569: warning: Switch case apparently never taken in method m5a(int)",7
//                ,"/tt/TestJava.java:570: warning: Switch case apparently never taken in method m5a(int)",7
//                ,"/tt/TestJava.java:573: warning: Switch case apparently never taken in method m5a(int)",7
//                ,"/tt/TestJava.java:584: warning: Switch case apparently never taken in method m5a(int)",7
//                ,"/tt/TestJava.java:597: warning: Switch case apparently never taken in method m5a(int)",7
//                ,"/tt/TestJava.java:598: warning: Switch case apparently never taken in method m5a(int)",7
//                ,"/tt/TestJava.java:601: warning: Switch case apparently never taken in method m5a(int)",7
//                ,"/tt/TestJava.java:620: warning: Switch case apparently never taken in method m5b(int)",7
//                ,"/tt/TestJava.java:621: warning: Switch case apparently never taken in method m5b(int)",7
//                ,"/tt/TestJava.java:635: warning: Switch case apparently never taken in method m5b(int)",7
//                ,"/tt/TestJava.java:636: warning: Switch case apparently never taken in method m5b(int)",7
//                ,"/tt/TestJava.java:649: warning: Switch case apparently never taken in method m5b(int)",7
//                ,"/tt/TestJava.java:650: warning: Switch case apparently never taken in method m5b(int)",7
//                ,"/tt/TestJava.java:653: warning: Switch case apparently never taken in method m5b(int)",7
//                ,"/tt/TestJava.java:664: warning: Switch case apparently never taken in method m5b(int)",7
//                ,"/tt/TestJava.java:677: warning: Switch case apparently never taken in method m5b(int)",7
//                ,"/tt/TestJava.java:678: warning: Switch case apparently never taken in method m5b(int)",7
//                ,"/tt/TestJava.java:681: warning: Switch case apparently never taken in method m5b(int)",7
//
                );
    }

    // A proof that reaches the time limit is reported as a time-limit abort (a timeout), never as
    // a missing model.
    @Test
    public void testTimeoutCanceledIsTimeout() {
        // Java arithmetic keeps the range-check diagnostics (which depend on timing) out of the output.
        addOptions("--timeout=1", "--code-math=java", "--spec-math=java");
        helpEsc("tt.Hard",
                """
                package tt;
                public class Hard {
                  //@ requires 1 < x && x < 100000 && 1 < y && y < 100000 && 1 < z && z < 100000;
                  //@ ensures \\result != 0;
                  public long g(long x, long y, long z) { return x*x*x*y*y*z - 3*x*y*z*z + 7; }
                }
                """
                ,"/tt/Hard.java:5: verify: Validity is unknown - time or memory limit reached: : Aborted proof: \"canceled\"",15
                );
    }

    // Bound guards against a fixed per-command solver wait (~1 ms each): measured ~54 s unfixed, ~17 s fixed (2026-10-04, VM 202); the bound is their geometric mean.
    @Test
    public void testPerCommandSolverOverhead() {
        final int N = 10000;
        final long BOUND_MS = 30000L;
        addOptions("--esc-threads=1");
        StringBuilder sb = new StringBuilder();
        sb.append("package tt;\npublic class Many {\n  //@ requires 0 <= x && x < 1000;\n  public void m(int x) {\n");
        for (int k = 0; k < N; k++) sb.append("    //@ assert x + ").append(k).append(" >= ").append(k).append(";\n");
        sb.append("  }\n}\n");
        long t0 = System.nanoTime();
        helpEsc("tt.Many", sb.toString());
        long ms = (System.nanoTime() - t0) / 1000000L;
        System.out.println("PER-COMMAND-OVERHEAD helpEsc wall ms = " + ms);
        Assert.assertTrue("trivial " + N + "-assert proof took " + ms + " ms, bound " + BOUND_MS + " ms", ms < BOUND_MS);
    }
}