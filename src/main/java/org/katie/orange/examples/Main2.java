package org.katie.orange.examples;

import org.katie.orange.core.Listener;
import org.katie.orange.core.synthesizer.CodeSynthesizer;

public class Main2 {
    public static void main(String[] args) {
        HttpClient client = new HttpClient();

        Listener listener = new Listener();
        HttpClient wrappedClient = listener.wrap(client);

        System.out.println(wrappedClient.get().getBody());

        CodeSynthesizer codeSynthesizer = new CodeSynthesizer("org.katie.orange.examples", "create");

        System.out.println(codeSynthesizer.generateMockito(listener));
    }
}