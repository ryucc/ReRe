package org.rere.replay;

import org.rere.api.ReRe;
import org.rere.core.data.objects.EnvironmentNode;

public class ReadmeRecursiveExample {

    public static void main(String[] args) {
        HttpClient client = new HttpClient();
        ReRe rere = new ReRe();

        HttpClient wrappedClient = rere.createSpiedObject(client, HttpClient.class);

        System.out.println("/*");
        System.out.println(wrappedClient.get(0).getBody());
        System.out.println("*/");

        EnvironmentNode node = rere.getReReRecordData().roots().get(0);
        HttpClient replayDice = rere.createReplayMock(node, HttpClient.class);
        System.out.println("/*");
        System.out.println(replayDice.get(0).getBody());
        System.out.println("*/");
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
