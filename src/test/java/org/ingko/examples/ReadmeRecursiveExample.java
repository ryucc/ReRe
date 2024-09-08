package org.ingko.examples;

import org.ingko.core.listener.Listener;
import org.ingko.core.synthesizer.MockitoSynthesizer;

public class ReadmeRecursiveExample {

    public static class HttpClient {
        public HttpResponse get(int i) {
            return new HttpResponse();
        }
    }

    public static class HttpResponse {
        public HttpResponse() {
        }

        public String getBody() {
            return body;
        }

        private final String body = "Hello World!";

    }

    public static void main(String[] args) {
        HttpClient client = new HttpClient();

        Listener listener = new Listener();
        HttpClient wrappedClient = listener.createRoot(client, HttpClient.class);

        System.out.println(wrappedClient.get(0).getBody());

        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer("org.katie.orange.examples", "create");

        System.out.println(mockitoSynthesizer.generateMockito(listener));
    }
}