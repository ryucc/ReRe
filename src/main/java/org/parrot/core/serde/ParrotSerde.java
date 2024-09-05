package org.parrot.core.serde;

import java.io.IOException;

public interface ParrotSerde<T> {
    String serialize(T object) throws IOException;
    T deserialize(String serialization) throws IOException;
}
