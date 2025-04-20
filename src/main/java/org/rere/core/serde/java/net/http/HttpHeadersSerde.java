/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.serde.java.net.http;

import org.rere.core.serde.PrimitiveSerde;
import org.rere.core.serde.ReReSerde;
import org.rere.core.serde.exceptions.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class HttpHeadersSerde implements ReReSerde {
    private final PrimitiveSerde primitiveSerde;
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();
    public HttpHeadersSerde() {
        primitiveSerde = new PrimitiveSerde();
    }


    @Override
    public String serialize(Object object) throws SerializationException {
        if(object instanceof HttpHeaders) {
            HttpHeaders httpHeaders = (HttpHeaders) object;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    baos)) {
                objectOutputStream.writeObject(httpHeaders.map());
                objectOutputStream.flush();
                String in = encoder.encodeToString(baos.toByteArray());
                return in;
            } catch (IOException e) {
                throw new SerializationException(e);
            }
        }
        throw new SerializationException("Wrong class to serialize");
    }

    @Override
    public Object deserialize(String serialization) {
        try (InputStream stream = new ByteArrayInputStream(serialization.getBytes(StandardCharsets.UTF_8)); ObjectInputStream objectInputStream = new ObjectInputStream(
                decoder.wrap(stream))) {
            Map<String, List<String>> map = (Map<String, List<String>>) objectInputStream.readObject();
            return HttpHeaders.of(map, (s1, s2) -> true );
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean accept(Class clazz) {
        return HttpHeaders.class.isAssignableFrom(clazz);
    }
}
