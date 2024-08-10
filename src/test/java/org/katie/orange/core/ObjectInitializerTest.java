package org.katie.orange.core;


import org.junit.jupiter.api.Test;
import org.katie.orange.core.listener.ObjectInitializer;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectInitializerTest {

    @Test
    public void test() throws Exception {
        assertThat(ObjectInitializer.create(Integer.class)).isInstanceOf(Integer.class);
    }

    @Test
    public void testString() throws Exception {
        assertThat(ObjectInitializer.create(String.class)).isInstanceOf(String.class);
    }

    @Test
    public void testInnerClass() throws Exception {
        assertThat(ObjectInitializer.create(TestClass.class)).isInstanceOf(TestClass.class);
    }

    @Test
    public void testConstructorWithFields() throws Exception {
        assertThat(ObjectInitializer.create(TestClassWithFields.class)).isInstanceOf(TestClassWithFields.class);
    }

    public class TestClass {
    }

    public class TestClassWithFields {
        private final int a;

        public TestClassWithFields(int a) {
            this.a = a;
        }

    }

}