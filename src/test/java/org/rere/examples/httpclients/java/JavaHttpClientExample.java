/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.examples.httpclients.java;

import org.rere.api.ReRe;
import org.rere.api.ReReSettings;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


public class JavaHttpClientExample {
    public static void main(String[] args) throws Exception {

        ReRe reRe = new ReRe(new ReReSettings().withNoParameterModding(true));
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
        HttpClient rereClient = reRe.createSpiedObject(client, HttpClient.class);

        System.out.println("/*");
        HttpRequest request = HttpRequest.newBuilder().uri(new URI("https://www.google.com")).GET().build();
        HttpResponse<String> response = rereClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        System.out.println("*/");
        System.out.println(reRe.exportMockito("org.rere.examples.httpclients.java",
                "create",
                "JavaHttpClientExampleExpected"));
    }
}