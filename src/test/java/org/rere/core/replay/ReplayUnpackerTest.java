/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay;


import org.junit.jupiter.api.Test;
import org.rere.core.replay.unwrap.PrimitiveUnwrapper;

import static org.assertj.core.api.Assertions.assertThat;

class ReplayUnpackerTest {
    @Test
    public void testInteger() {
        PrimitiveUnwrapper primitiveUnwrapper = new PrimitiveUnwrapper();
        assertThat(primitiveUnwrapper.parsePrimitive(Integer.class, "123")).isEqualTo(123);
    }
    @Test
    public void testInt() {
        PrimitiveUnwrapper primitiveUnwrapper = new PrimitiveUnwrapper();
        assertThat(primitiveUnwrapper.parsePrimitive(int.class, "123")).isEqualTo(123);
    }
    @Test
    public void testChar() {
        PrimitiveUnwrapper primitiveUnwrapper = new PrimitiveUnwrapper();
        assertThat(primitiveUnwrapper.parsePrimitive(char.class, "a")).isEqualTo('a');
    }
    @Test
    public void testBoolean() {
        PrimitiveUnwrapper primitiveUnwrapper = new PrimitiveUnwrapper();
        assertThat(primitiveUnwrapper.parsePrimitive(boolean.class, "true"))
                .isEqualTo(true);
        assertThat(primitiveUnwrapper.parsePrimitive(boolean.class, "false"))
                .isEqualTo(false);
    }
    @Test
    public void testShort() {
        PrimitiveUnwrapper primitiveUnwrapper = new PrimitiveUnwrapper();
        assertThat(primitiveUnwrapper.parsePrimitive(short.class, "19"))
                .isEqualTo((short)19);
        assertThat(primitiveUnwrapper.parsePrimitive(Short.class, "22"))
                .isEqualTo((short) 22);
    }


}