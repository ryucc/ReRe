package org.ingko.core.listener;

import org.ingko.core.listener.testUtils.GraphCompare;
import org.junit.jupiter.api.Test;
import org.ingko.core.data.methods.MethodCall;
import org.ingko.core.data.methods.MethodResult;
import org.ingko.core.data.objects.Node;

import static org.assertj.core.api.Assertions.assertThat;

public class ListenerRecursiveTests {

    @Test
    public void test() throws Exception {
        HttpClient client = new HttpClient();

        Listener listener = new Listener();
        HttpClient wrappedClient = listener.createRoot(client, HttpClient.class);
        String s = wrappedClient.get().getBody();

        Node root = listener.getRoot();
        Node expectedRoot = Node.ofInternal(HttpClient.class);
        Node reponseNode = Node.ofInternal(HttpResponse.class);
        MethodCall getCall = new MethodCall(HttpClient.class.getMethod("get"),
                expectedRoot,
                reponseNode,
                MethodResult.RETURN);
        expectedRoot.addEdge(getCall);
        Node bodyNode = Node.ofPrimitive(String.class, "\"Hello World!\"");
        MethodCall getBodyCall = new MethodCall(HttpResponse.class.getMethod("getBody"),
                reponseNode,
                bodyNode,
                MethodResult.RETURN);
        reponseNode.addEdge(getBodyCall);

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