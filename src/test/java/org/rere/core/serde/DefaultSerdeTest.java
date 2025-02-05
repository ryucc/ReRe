/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.serde;

import org.rere.core.serde.exceptions.SerializationException;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultSerdeTest {
    DefaultSerde defaultSerde = new DefaultSerde();

    @Test
    public void testInt() throws Exception {
        Integer a = 1;
        String ser = defaultSerde.serialize(a);
        int b = (int) defaultSerde.deserialize(ser);
        assertThat(a).isEqualTo(b);
    }
    @Test
    public void testDouble() throws Exception {
        double a = 1;
        String ser = defaultSerde.serialize(a);
        double b = (double) defaultSerde.deserialize(ser);
        assertThat(a).isEqualTo(b);
    }

    @Test
    public void testSer() throws Exception {
        Class<?> a = Class.class;
        String ser = defaultSerde.serialize(a);
        Class<?> b = (Class<?>) defaultSerde.deserialize(ser);
        assertThat(a).isEqualTo(b);
    }

    @Test
    public void testSerializable() throws Exception {
        SerializableClass a = new SerializableClass(1);
        String ser = defaultSerde.serialize(a);
        SerializableClass b = (SerializableClass) defaultSerde.deserialize(ser);
        assertThat(b).isEqualTo(a);
    }

    @Test
    public void testNotSerializable() {
        NotSerializable a = new NotSerializable();
        assertThatThrownBy(() -> defaultSerde.serialize(a)).isInstanceOf(SerializationException.class);
    }

    record SerializableClass(int a) implements Serializable {
    }

    static class NotSerializable {

    }


}