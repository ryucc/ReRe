package org.parrot.core.serde;

import org.junit.jupiter.api.Test;
import org.parrot.core.serde.exceptions.SerializationException;

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
    public void testSerializable() throws Exception {
        SerializableClass a = new SerializableClass(1, 2);
        String ser = defaultSerde.serialize(a);
        SerializableClass b = (SerializableClass) defaultSerde.deserialize(ser);
        assertThat(a).isEqualTo(b);
    }

    @Test
    public void testNotSerializable() {
        NotSerializable a = new NotSerializable();
        assertThatThrownBy(() -> defaultSerde.serialize(a)).isInstanceOf(SerializationException.class);
    }

    record SerializableClass(int a, int b) implements Serializable {
    }

    static class NotSerializable {

    }


}