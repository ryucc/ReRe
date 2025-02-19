/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.examples.httpclients.java;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class JavaHttpClientResultTest {
    public static void main(String[] args) throws Exception {
        HttpClient client = JavaHttpClientExampleExpected.create();

        System.out.println("/*");
        HttpRequest request = HttpRequest.newBuilder().uri(new URI("https://www.google.com")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        System.out.println("*/");

        HttpHeaders httpHeaders = JavaHttpClientExampleExpected.environmentNode2();
        System.out.println(httpHeaders.map());
    }
}