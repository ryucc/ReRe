/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.data.methods;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;


class SignatureTest {

    private static class PrivateDice1 {
        public int roll(int b, int c) {
            return 0;
        }
        public int roll1() {
            return 0;
        }
    }

    private static class PrivateDice2 {
        public int roll(int a, int asd) {
            return 0;
        }
    }
    @Test
    public void testEqual() throws Exception{
        Method a = PrivateDice1.class.getMethod("roll", int.class, int.class);
        Method b = PrivateDice1.class.getMethod("roll", int.class, int.class);
        Signature lhs = new Signature(a);
        Signature rhs = new Signature(b);
        assertThat(lhs).isEqualTo(rhs);
    }

    @Test
    public void testDifferentMethod() throws Exception{
        Method a = PrivateDice1.class.getMethod("roll", int.class, int.class);
        Method b = PrivateDice1.class.getMethod("roll1");
        Signature lhs = new Signature(a);
        Signature rhs = new Signature(b);
        assertThat(lhs).isNotEqualTo(rhs);
    }

    @Test
    public void testMethodFromDifferentClass() throws Exception{
        Method a = PrivateDice1.class.getMethod("roll", int.class, int.class);
        Method b = PrivateDice2.class.getMethod("roll", int.class, int.class);
        Signature lhs = new Signature(a);
        Signature rhs = new Signature(b);
        assertThat(lhs).isEqualTo(rhs);
    }

    @Test
    public void testDifferent() throws Exception{
        Method a = PrivateDice1.class.getMethod("roll", int.class, int.class);
        Object o = 1;
        Signature lhs = new Signature(a);
        assertThat(lhs).isNotEqualTo(o);
    }
}