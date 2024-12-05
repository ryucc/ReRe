package org.ingko.core.listener;

import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.testUtils.GraphCompare;
import org.junit.jupiter.api.Test;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.MethodResult;

import static org.assertj.core.api.Assertions.assertThat;

public class EnvironmentObjectListenerRecursiveTests {

    @Test
    public void test() throws Exception {
        HttpClient client = new HttpClient();

        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        HttpClient wrappedClient = environmentObjectListener.createRoot(client, HttpClient.class);
        String s = wrappedClient.get().getBody();

        EnvironmentNode root = environmentObjectListener.getRoot();
        EnvironmentNode expectedRoot = EnvironmentNode.ofInternal(HttpClient.class);
        EnvironmentNode reponseEnvironmentNode = EnvironmentNode.ofInternal(HttpResponse.class);
        EnvironmentMethodCall getCall = new EnvironmentMethodCall(HttpClient.class.getMethod("get"));
        getCall.registerReturnNode(reponseEnvironmentNode);
        getCall.setResult(MethodResult.RETURN);
        expectedRoot.addEdge(getCall);
        EnvironmentNode bodyEnvironmentNode = EnvironmentNode.ofPrimitive(String.class, "\"Hello World!\"");
        EnvironmentMethodCall getBodyCall = new EnvironmentMethodCall(HttpResponse.class.getMethod("getBody"));
        getBodyCall.registerReturnNode(bodyEnvironmentNode);
        getBodyCall.setResult(MethodResult.RETURN);
        reponseEnvironmentNode.addEdge(getBodyCall);

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