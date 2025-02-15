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

public class ApacheMockClientExample {
    public static void main(String[] args) throws Exception {
        CloseableHttpClient httpclient = ApacheHttpClientExampleExpected.environmentNode0();
        HttpGet httpGet = new HttpGet("http://www.google.com");
        CloseableHttpResponse response1 = httpclient.execute(httpGet);

        try {
            System.out.println("/*");
            System.out.println(response1.getStatusLine());
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
    }
}