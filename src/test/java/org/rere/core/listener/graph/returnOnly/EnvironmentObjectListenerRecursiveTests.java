/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.graph.returnOnly;

import org.rere.api.ReRe;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.serde.PrimitiveSerde;
import org.rere.testData.GraphCompare;
import org.junit.jupiter.api.Test;
import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.methods.MethodResult;

import static org.assertj.core.api.Assertions.assertThat;

public class EnvironmentObjectListenerRecursiveTests {

    @Test
    public void test() throws Exception {
        HttpClient client = new HttpClient();


        ReRe reRe = new ReRe();
        HttpClient wrappedClient = reRe.createReReObject(client, HttpClient.class);
        String s = wrappedClient.get().getBody();

        EnvironmentNode root = reRe.getReReRecordData().roots().get(0);
        EnvironmentNode expectedRoot = EnvironmentNode.ofInternal(HttpClient.class, HttpClient.class);
        EnvironmentNode reponseEnvironmentNode = EnvironmentNode.ofInternal(HttpResponse.class, HttpResponse.class);
        EnvironmentMethodCall getCall = new EnvironmentMethodCall(HttpClient.class.getMethod("get"));
        getCall.setReturnNode(reponseEnvironmentNode);
        getCall.setResult(MethodResult.RETURN);
        expectedRoot.addMethodCall(getCall);
        EnvironmentNode bodyEnvironmentNode = EnvironmentNode.ofPrimitive(String.class, new PrimitiveSerde().serialize("Hello World!"));
        EnvironmentMethodCall getBodyCall = new EnvironmentMethodCall(HttpResponse.class.getMethod("getBody"));
        getBodyCall.setReturnNode(bodyEnvironmentNode);
        getBodyCall.setResult(MethodResult.RETURN);
        reponseEnvironmentNode.addMethodCall(getBodyCall);

        GraphCompare graphCompare = new GraphCompare();
        assertThat(graphCompare.diffNode(root, expectedRoot)).isTrue();
    }

    public static class HttpClient {
        public HttpResponse get() {
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