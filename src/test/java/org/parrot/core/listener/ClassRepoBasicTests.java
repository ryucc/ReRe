package org.parrot.core.listener;


import org.junit.jupiter.api.Test;
import org.parrot.core.listener.exceptions.SubclassingException;
import org.parrot.core.listener.testUtils.ReturnNullInterceptor;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ClassRepoBasicTests {
    @Test
    public void testCache() throws Exception {
        ClassRepo classRepo = new ClassRepo(new ReturnNullInterceptor());
        Class<?> subClass = classRepo.getOrDefineSubclass(MyClass.class);
        assertThat(MyClass.class.isAssignableFrom(subClass)).isTrue();

        Class<?> subClass2 = classRepo.getOrDefineSubclass(MyClass.class);
        assertThat(subClass2).isEqualTo(subClass);

    }

    @Test
    public void testIsSubClass() throws Exception {
        ClassRepo classRepo = new ClassRepo(new ReturnNullInterceptor());
        Class<?> subClass = classRepo.getOrDefineSubclass(MyClass.class);
        assertThat(MyClass.class.isAssignableFrom(subClass)).isTrue();
    }

    @Test
    public void testFinalClass() {
        ClassRepo classRepo = new ClassRepo(new ReturnNullInterceptor());
        assertThatExceptionOfType(SubclassingException.class).isThrownBy(() -> classRepo.getOrDefineSubclass(MyFinal.class));
    }

    @Test
    public void testPrivateClass() throws Exception {
        ClassRepo classRepo = new ClassRepo(new ReturnNullInterceptor());
        Class<?> subClass = classRepo.getOrDefineSubclass(MyPrivate.class);
        assertThat(MyPrivate.class.isAssignableFrom(subClass)).isTrue();
    }

    public static class MyClass {
    }

    public static final class MyFinal {
    }

    private static class MyPrivate {
    }
}