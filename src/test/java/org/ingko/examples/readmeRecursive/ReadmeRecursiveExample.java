package org.ingko.examples.readmeRecursive;

import org.ingko.api.Parrot;

public class ReadmeRecursiveExample {

    public static void main(String[] args) {
        HttpClient client = new HttpClient();
        Parrot parrot = Parrot.newSession();

        HttpClient wrappedClient = parrot.createRoot(client, HttpClient.class);

        System.out.println("/*");
        System.out.println(wrappedClient.get(0).getBody());
        System.out.println("*/");

        String code = parrot.createMockito("org.ingko.examples.readmeRecursive",
                "create",
                "ReadmeRecursiveExampleExpected");

        System.out.println(code);
    }

    public static class HttpClient {
        public HttpResponse get(int i) {
            return new HttpResponse();
        }
    }

    public static class HttpResponse {
        private final String body = "Hello World!";

        public HttpResponse() {
        }

        public String getBody() {
            return body;
        }

    }
}