/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.utils;

import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

class ClassUtilsTest {
    record SomeRecord(){}
    @Test
    public void testIsRecord(){
        assertThat(ClassUtils.isRecord(SomeRecord.class)).isTrue();
        assertThat(ClassUtils.isRecord(int.class)).isFalse();
    }
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
    }

}