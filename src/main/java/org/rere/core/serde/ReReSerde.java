package org.rere.core.serde;

import org.rere.core.serde.exceptions.SerializationException;

public interface ReReSerde<T> {
    String serialize(T object) throws SerializationException;

    /**
     * Return null or throw runtime errors on failures.
     */
    T deserialize(String serialization);
}
