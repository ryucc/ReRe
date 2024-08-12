package org.parrot.examples;

import org.parrot.core.listener.Listener;
import org.parrot.core.synthesizer.CodeSynthesizer;
import org.parrot.examples.testObjects.HttpClient;

public class ReadmeRecursiveExample {
    public static void main(String[] args) {
        HttpClient client = new HttpClient();

        Listener listener = new Listener();
        HttpClient wrappedClient = listener.createRoot(client);

        System.out.println(wrappedClient.get(0).getBody());

        CodeSynthesizer codeSynthesizer = new CodeSynthesizer("org.katie.orange.examples", "create");

        System.out.println(codeSynthesizer.generateMockito(listener));
    }
}