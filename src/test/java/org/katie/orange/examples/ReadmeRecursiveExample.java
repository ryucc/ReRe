package org.katie.orange.examples;

import org.katie.orange.core.listener.Listener;
import org.katie.orange.core.synthesizer.CodeSynthesizer;

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