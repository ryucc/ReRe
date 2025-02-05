package org.rere.core.synthesizer;


import org.junit.jupiter.api.Test;
import org.rere.core.listener.ObjectInitializer;
import org.rere.core.listener.exceptions.InitializationException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ObjectInitializerTest {

    @Test
    public void test() throws Exception {
        assertThat(ObjectInitializer.create(Integer.class)).isInstanceOf(Integer.class);
    }

    @Test
    public void testNoConstructors() throws Exception {
        assertThat(ObjectInitializer.create(PrivateConstructorClass.class))
                .isInstanceOf(PrivateConstructorClass.class);
    }
    @Test
    public void testString() throws Exception {
        assertThat(ObjectInitializer.create(String.class)).isInstanceOf(String.class);
    }

    @Test
    public void testRecord() throws Exception {
        int someInt = 10;
        TestClass testClass = new TestClass();
        List<Object> params = List.of(someInt,testClass);
        TestRecord c = ObjectInitializer.initRecord(TestRecord.class, params);
        assertThat(c.a()).isEqualTo(someInt);
        assertThat(c.b()).isEqualTo(testClass);
    }

    @Test
    public void testRecordWithWrongParamLength() {
        List<Object> params = List.of();
        assertThatThrownBy(() -> ObjectInitializer.initRecord(TestRecord.class, params))
                .isInstanceOf(InitializationException.class);
    }

    @Test
    public void testRecordWithWrongParamType() {
        List<Object> params = List.of(10, 10);
        assertThatThrownBy(() -> ObjectInitializer.initRecord(TestRecord.class, params))
                .isInstanceOf(InitializationException.class);
    }

    @Test
    public void testInnerClass() throws Exception {
        assertThat(ObjectInitializer.create(TestClass.class)).isInstanceOf(TestClass.class);
    }

    @Test
    public void testConstructorWithFields() throws Exception {
        assertThat(ObjectInitializer.create(TestClassWithFields.class)).isInstanceOf(TestClassWithFields.class);
    }

    public record TestRecord(int a, TestClass b){}

    public class TestClass {
    }
    public static class PrivateConstructorClass {
        int someInt;
        private PrivateConstructorClass(int n){
            someInt = n;
        };
        public static PrivateConstructorClass create() {
            return new PrivateConstructorClass(10);
        }
    }

    public class TestClassWithFields {
        private final int a;

        public TestClassWithFields(int a) {
            this.a = a;
        }

    }

}