package org.rere.examples.readmeRecursive;

import org.rere.api.ReRe;

public class ReadmeRecursiveExample {

    public static void main(String[] args) {
        HttpClient client = new HttpClient();
        ReRe rere = ReRe.newSession();

        HttpClient wrappedClient = rere.createRoot(client, HttpClient.class);

        System.out.println("/*");
        System.out.println(wrappedClient.get(0).getBody());
        System.out.println("*/");

        String code = rere.createMockito("org.rere.examples.readmeRecursive",
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