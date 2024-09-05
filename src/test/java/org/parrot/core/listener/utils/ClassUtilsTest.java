package org.parrot.core.listener.utils;

import org.junit.jupiter.api.Test;
import org.parrot.core.listener.utils.ClassUtils;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

class ClassUtilsTest {
    @Test
    public void testIsPrimitive(){
        assertThat(ClassUtils.isWrapperOrPrimitive(Integer.class)).isTrue();
        assertThat(ClassUtils.isWrapperOrPrimitive(Float.class)).isTrue();
        assertThat(ClassUtils.isWrapperOrPrimitive(Double.class)).isTrue();
        assertThat(ClassUtils.isWrapperOrPrimitive(Short.class)).isTrue();
        assertThat(ClassUtils.isWrapperOrPrimitive(Long.class)).isTrue();
        assertThat(ClassUtils.isWrapperOrPrimitive(Character.class)).isTrue();
        assertThat(ClassUtils.isWrapperOrPrimitive(Byte.class)).isTrue();
        assertThat(ClassUtils.isWrapperOrPrimitive(this.getClass())).isFalse();

        int[][] a = new int[10][100];

        Class<?> cls = a.getClass();
        System.out.println(cls.isRecord());
        System.out.println(cls.getComponentType());
        System.out.println(cls.getComponentType().getComponentType());
        System.out.println(Serializable.class.isAssignableFrom(cls));
        System.out.println(Serializable.class.isAssignableFrom(int.class));
    }

}