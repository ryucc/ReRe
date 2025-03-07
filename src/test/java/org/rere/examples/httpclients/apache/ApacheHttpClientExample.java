/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.examples.httpclients.apache;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.rere.api.ReRe;
import org.rere.api.ReReSettings;
import org.rere.core.data.objects.EnvironmentNode;

import java.io.IOException;

public class ApacheHttpClientExample {
    public static void main(String[] args) throws IOException, InterruptedException {
        ReRe reRe = new ReRe(new ReReSettings().withParameterModding(false));
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpClient rereClient = reRe.createSpiedObject(httpclient, CloseableHttpClient.class);
        HttpGet httpGet = new HttpGet("https://mit-license.org");
        CloseableHttpResponse response1 = rereClient.execute(httpGet);

        try {
            System.out.println("/*");
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed

            StatusLine statusLine = response1.getStatusLine();
            System.out.println(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
            System.out.println(EntityUtils.toString(entity1));

            System.out.println("*/");
        } finally {
            response1.close();
        }
        System.out.println(reRe.exportMockito("org.rere.examples.httpclients.apache", "create", "ApacheHttpClientExampleExpected"));
    }
}