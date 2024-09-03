package org.parrot.examples.fails;

import org.parrot.core.listener.Listener;
import org.parrot.core.synthesizer.MockitoSynthesizer;
import org.parrot.examples.testObjects.HttpClient;

public class ReadmeRecursiveExample {
    public static void main(String[] args) {
        HttpClient client = new HttpClient();

        Listener listener = new Listener();
        HttpClient wrappedClient = listener.createRoot(client, HttpClient.class);

        System.out.println(wrappedClient.get(0).getBody());

        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer("org.katie.orange.examples", "create");

        System.out.println(mockitoSynthesizer.generateMockito(listener));
    }
}