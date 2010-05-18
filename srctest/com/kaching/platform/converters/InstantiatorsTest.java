/**
 * Copyright 2009 KaChing Group Inc. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.kaching.platform.converters;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import org.junit.Test;

import com.google.inject.BindingAnnotation;
import com.google.inject.name.Named;

public class InstantiatorsTest {

  @Test
  public void getPublicConstructor() throws Exception {
    Instantiators.getConstructor(A.class);
  }

  @Test
  public void getProtectedConstructor() throws Exception {
    Instantiators.getConstructor(B.class);
  }

  @Test
  public void getDefaultVisibleConstructor() throws Exception {
    Instantiators.getConstructor(C.class);
  }

  @Test
  public void getPrivateConstructor() throws Exception {
    Instantiators.getConstructor(D.class);
  }

  @Test
  public void getNonExistingConstructor() throws Exception {
    try {
      Instantiators.getConstructor(E.class);
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test
  public void getNonUniqueConstructor() throws Exception {
    try {
      Instantiators.getConstructor(F.class);
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test
  public void getConstructorFromSuperclass() throws Exception {
    Instantiators.getConstructor(G.class);
  }

  @Test
  public void getConstructorFromSuperclassWithMultipleConstructors() {
    Instantiators.getConstructor(H.class);
  }

  @Test
  public void getNonUniqueConstructorWithAnnotation1() throws Exception {
    assertNotNull(Instantiators.getConstructor(P.class));
  }

  @Test
  public void getNonUniqueConstructorWithAnnotation2() throws Exception {
    try {
      Instantiators.getConstructor(Q.class);
      fail();
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  static class A {
    public A() {}
  }

  static class B {
    protected B() {}
  }

  static class C {
    C() {}
  }

  static class D {
    private D() {}
  }

  static interface E {
  }

  static class F {
    F() {}
    F(String s) {}
  }

  static class G extends A {}

  static class H extends F {}

  static class I {
    I(String s) {}
  }

  static class J {
    J(List<String> l) {}
  }

  static class K {
    K(@Named("k") String l) {}
  }

  static class L {
    L(@Named("l") @LocalBindingAnnotation String l) {}
  }

  static class M {
    M(@Optional String s) {}
  }

  static class N {
    N(String s, @Optional String t) {}
  }

  static class O {
    O(String s, @Optional("foo") String t, String u) {}
  }

  static class P {
    P() {}
    @Instantiate P(String s) {}
  }

  static class Q {
    @Instantiate Q() {}
    @Instantiate Q(String s) {}
  }

  @Retention(RUNTIME)
  @Target({ ElementType.FIELD, ElementType.PARAMETER })
  @BindingAnnotation
  static @interface LocalBindingAnnotation { }
  
}