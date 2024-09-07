package org.parrot.core.serde;

import org.parrot.core.serde.exceptions.SerializationException;

public interface ParrotSerde<T> {
    String serialize(T object) throws SerializationException;

    /**
     * Return null or throw runtime errors on failures.
     */
    T deserialize(String serialization);
}
