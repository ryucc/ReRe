/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.utils;

import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Array;

import static org.assertj.core.api.Assertions.assertThat;

class ClassUtilsTest {
    record SomeRecord(){}

    @Test
    public void testArrayEquals() {

        Integer[][] arr = new Integer[2][2];
        arr[0][0] = 1;
        arr[0][1] = 2;
        arr[1][0] = 3;
        arr[1][1] = 4;
        Integer[][] brr = new Integer[2][2];
        brr[0][0] = 1;
        brr[0][1] = 2;
        brr[1][0] = 3;
        brr[1][1] = 4;
        assertThat(ClassUtils.arrayEquals(arr, brr)).isTrue();
        brr[1][1] = 5;
        assertThat(ClassUtils.arrayEquals(arr, brr)).isFalse();
    }
    @Test
    public void testShallowCopyIntoArray() {
        Integer[][] iarr = new Integer[2][2];
        iarr[0][0] = 1;
        iarr[0][1] = 2;
        iarr[1][0] = 3;
        iarr[1][1] = 4;

        Integer[][] brr = new Integer[2][2];
        ClassUtils.shallowCopyIntoArray(iarr, brr);
        assertThat(brr).isEqualTo(iarr);
    }
    @Test
    public void testDeepCopyArray() {
        Integer[][] iarr = new Integer[2][2];
        iarr[0][0] = 1;
        iarr[0][1] = 2;
        iarr[1][0] = 3;
        iarr[1][1] = 4;

        Integer[][] brr = ClassUtils.deepCopyArray(iarr);
        assertThat(brr).isEqualTo(iarr);
    }
    @Test
    public void testIsPrimitiveArray() {
        Integer[][] iarr = new Integer[10][5];
        assertThat(ClassUtils.isPrimitiveArray(iarr.getClass())).isTrue();
        assertThat(ClassUtils.isPrimitiveArray(ClassUtilsTest.class)).isFalse();
        assertThat(ClassUtils.isPrimitiveArray(Integer.class)).isFalse();
    }
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