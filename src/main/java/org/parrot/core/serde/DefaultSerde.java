package org.parrot.core.serde;

import org.parrot.core.serde.exceptions.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DefaultSerde implements ParrotSerde<Object> {
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();


    public String serialize(Object object) throws SerializationException {
        try (
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(encoder.wrap(baos))) {
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            return baos.toString();
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    public Object deserialize(String serialization) {
        try (InputStream stream = new ByteArrayInputStream(serialization.getBytes(StandardCharsets.UTF_8));
             ObjectInputStream objectInputStream = new ObjectInputStream(decoder.wrap(stream))) {
            return objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            return null;
        }
    }

}
