package org.ingko.core.listener;

import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.testUtils.GraphCompare;
import org.junit.jupiter.api.Test;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.MethodResult;

import static org.assertj.core.api.Assertions.assertThat;

public class ListenerRecursiveTests {

    @Test
    public void test() throws Exception {
        HttpClient client = new HttpClient();

        Listener listener = new Listener();
        HttpClient wrappedClient = listener.createRoot(client, HttpClient.class);
        String s = wrappedClient.get().getBody();

        EnvironmentNode root = listener.getRoot();
        EnvironmentNode expectedRoot = EnvironmentNode.ofInternal(HttpClient.class);
        EnvironmentNode reponseEnvironmentNode = EnvironmentNode.ofInternal(HttpResponse.class);
        EnvironmentMethodCall getCall = new EnvironmentMethodCall(HttpClient.class.getMethod("get"),
                expectedRoot,
                reponseEnvironmentNode,
                MethodResult.RETURN);
        expectedRoot.addEdge(getCall);
        EnvironmentNode bodyEnvironmentNode = EnvironmentNode.ofPrimitive(String.class, "\"Hello World!\"");
        EnvironmentMethodCall getBodyCall = new EnvironmentMethodCall(HttpResponse.class.getMethod("getBody"),
                reponseEnvironmentNode,
                bodyEnvironmentNode,
                MethodResult.RETURN);
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