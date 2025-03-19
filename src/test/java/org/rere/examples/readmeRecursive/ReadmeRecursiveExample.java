/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.examples.readmeRecursive;

import org.rere.api.ReRe;

public class ReadmeRecursiveExample {

    public static void main(String[] args) {
        HttpClient client = new HttpClient();
        ReRe rere = new ReRe();

        HttpClient wrappedClient = rere.createReReObject(client, HttpClient.class);

        System.out.println("/*");
        System.out.println(wrappedClient.get(0).getBody());
        System.out.println("*/");

        String code = rere.exportMockito("org.rere.examples.readmeRecursive",
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