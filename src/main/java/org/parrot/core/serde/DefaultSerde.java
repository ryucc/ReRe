package org.parrot.core.serde;

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


    public String serialize(Object object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(encoder.wrap(baos));
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        objectOutputStream.close();
        return baos.toString();
    }

    public Object deserialize(String serialization) throws IOException {
        try {
            InputStream stream = new ByteArrayInputStream(serialization.getBytes(StandardCharsets.UTF_8));
            ObjectInputStream objectInputStream = new ObjectInputStream(decoder.wrap(stream));
            objectInputStream.close();
            return objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
