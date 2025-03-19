/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay;


import org.junit.jupiter.api.Test;
import org.rere.core.replay.unwrap.PrimitiveInternalUnwrapper;

import static org.assertj.core.api.Assertions.assertThat;

class ReplayUnpackerTest {
    @Test
    public void testInteger() {
        PrimitiveInternalUnwrapper primitiveUnwrapper = new PrimitiveInternalUnwrapper();
        assertThat(primitiveUnwrapper.parsePrimitive(Integer.class, "123")).isEqualTo(123);
    }
    @Test
    public void testInt() {
        PrimitiveInternalUnwrapper primitiveUnwrapper = new PrimitiveInternalUnwrapper();
        assertThat(primitiveUnwrapper.parsePrimitive(int.class, "123")).isEqualTo(123);
    }
    @Test
    public void testChar() {
        PrimitiveInternalUnwrapper primitiveUnwrapper = new PrimitiveInternalUnwrapper();
        assertThat(primitiveUnwrapper.parsePrimitive(char.class, "a")).isEqualTo('a');
    }
    @Test
    public void testBoolean() {
        PrimitiveInternalUnwrapper primitiveUnwrapper = new PrimitiveInternalUnwrapper();
        assertThat(primitiveUnwrapper.parsePrimitive(boolean.class, "true"))
                .isEqualTo(true);
        assertThat(primitiveUnwrapper.parsePrimitive(boolean.class, "false"))
                .isEqualTo(false);
    }
    @Test
    public void testShort() {
        PrimitiveInternalUnwrapper primitiveUnwrapper = new PrimitiveInternalUnwrapper();
        assertThat(primitiveUnwrapper.parsePrimitive(short.class, "19"))
                .isEqualTo((short)19);
        assertThat(primitiveUnwrapper.parsePrimitive(Short.class, "22"))
                .isEqualTo((short) 22);
    }


}