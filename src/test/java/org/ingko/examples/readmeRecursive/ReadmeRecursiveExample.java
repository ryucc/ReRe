package org.ingko.examples.readmeRecursive;

import org.ingko.core.listener.interceptor.EnvironmentObjectListener;
import org.ingko.core.synthesizer.mockito.MockitoSynthesizer;

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

        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        HttpClient wrappedClient = environmentObjectListener.createRoot(client, HttpClient.class);

        System.out.println("/*");
        System.out.println(wrappedClient.get(0).getBody());
        System.out.println("*/");

        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer("org.ingko.examples.readmeRecursive",
                "create",
                "ReadmeRecursiveExampleExpected");

        System.out.println(mockitoSynthesizer.generateMockito(environmentObjectListener.getRoot()));
    }
}