package org.parrot.core.listener;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.parrot.core.listener.testUtils.ReturnNullInterceptor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ClassRepoInheritanceTests {
    /*
    Consider this inheritance graph
                        (public interface A)
                          /                 \
           (public interface B)         (public class C)
                         \                 /
                         (private class D)
    Trying to mock D with target A should result in C.
     */
    @Test
    public void testImplementInterface() throws Exception{
        ClassRepo classRepo = new ClassRepo(new ReturnNullInterceptor());
        Class<?> subClass = classRepo.getOrDefineSubclass(DiamondCommonParent.class);
        assertThat(DiamondCommonParent.class.isAssignableFrom(subClass)).isTrue();
    }
    @Test
    @DisplayName("in diamond inheritance, prioritize implementing the superclass")
    void testDiamondInheritanceSuperclassPriority() throws Exception {
        ClassRepo classRepo = new ClassRepo(new ReturnNullInterceptor());
        Class<?> subClass = classRepo.getOrDefineSubclass(DiamondFinalChild.class, DiamondCommonParent.class);
        assertThat(DiamondMiddleClass.class.isAssignableFrom(subClass)).isTrue();
    }

    @Test
    @DisplayName("in diamond inheritance, prioritize implementing the superclass")
    void testDiamondInheritanceSuperclassPriority2() throws Exception {
        ClassRepo classRepo = new ClassRepo(new ReturnNullInterceptor());
        Class<?> subClass = classRepo.getOrDefineSubclass(DiamondPrivateChild2.class, DiamondCommonParent.class);
        assertThat(DiamondMiddleInterface.class.isAssignableFrom(subClass)).isTrue();
    }

    @Test
    @DisplayName("in diamond inheritance, prioritize implementing the superclass")
    void testDiamondInheritanceSuperclassPriority3() throws Exception {
        ClassRepo classRepo = new ClassRepo(new ReturnNullInterceptor());
        Class<?> subClass = classRepo.getOrDefineSubclass(DiamondPrivateChild.class, DiamondCommonParent.class);
        assertThat(DiamondCommonParent.class.isAssignableFrom(subClass)).isTrue();
    }

    public interface DiamondCommonParent {
    }

    public interface DiamondMiddleInterface extends DiamondCommonParent {
    }

    public static class DiamondMiddleClass implements DiamondCommonParent {
    }
    private static class DiamondPrivateMiddleClass {
    }

    private interface  DiamondPrivateMiddleInterface extends DiamondCommonParent {
    }
    private static class DiamondPrivateChild2 extends DiamondPrivateMiddleClass implements DiamondMiddleInterface {
    }

    private static class DiamondPrivateChild extends DiamondPrivateMiddleClass implements DiamondPrivateMiddleInterface {
    }

    public final static class DiamondFinalChild extends DiamondMiddleClass implements DiamondMiddleInterface {
    }

    public static class MyClass {
    }

    public static final class MyFinal {
    }

    private static class MyPrivate {
    }
}