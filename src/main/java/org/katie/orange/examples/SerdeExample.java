package org.katie.orange.examples;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Encoder;
public class SerdeExample {

    public static void main(String[] args) throws Exception{
        //ser
        Integer a = 10;
        Encoder encoder = Base64.getEncoder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(encoder.wrap(baos));
        objectOutputStream.writeObject(a);
        objectOutputStream.flush();
        objectOutputStream.close();
        String s = baos.toString();
        System.out.println(s);

        //deser
        InputStream stream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
        Base64.Decoder decoder = Base64.getDecoder();
        ObjectInputStream objectInputStream
                = new ObjectInputStream(decoder.wrap(stream));
        Integer aa = (Integer) objectInputStream.readObject();
        objectInputStream.close();
        System.out.println(aa);
    }
}
