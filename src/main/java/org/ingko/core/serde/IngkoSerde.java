package org.ingko.core.serde;

import org.ingko.core.serde.exceptions.SerializationException;

public interface IngkoSerde<T> {
    String serialize(T object) throws SerializationException;

    /**
     * Return null or throw runtime errors on failures.
     */
    T deserialize(String serialization);
}
