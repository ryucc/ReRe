/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.serde;

import org.rere.core.serde.exceptions.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PrimitiveSerde implements ReReSerde {
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();


    public String serialize(Object object) throws SerializationException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                baos)) {
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            return encoder.encodeToString(baos.toByteArray());
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    public Object deserialize(String serialization) {
        try (InputStream stream = new ByteArrayInputStream(serialization.getBytes(StandardCharsets.UTF_8)); ObjectInputStream objectInputStream = new ObjectInputStream(
                decoder.wrap(stream))) {
            return objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean accept(Class<?> clazz) {
        return Serializable.class.isAssignableFrom(clazz);
    }

}
