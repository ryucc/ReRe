package org.rere.replay;

import org.rere.api.ReRe;
import org.rere.api.ReReMode;
import org.rere.api.ReReSettings;
import org.rere.api.ReReplayData;
import org.rere.core.data.objects.EnvironmentNode;

public class ReadmeRecursiveExample {

    public static void main(String[] args) {
        HttpClient client = new HttpClient();
        ReRe rere = new ReRe();

        HttpClient wrappedClient = rere.createReReObject(client, HttpClient.class);

        System.out.println("/*");
        System.out.println(wrappedClient.get(0).getBody());
        System.out.println("*/");

        ReReplayData reReplayData = rere.getReReRecordData();
        ReRe replayReRe = new ReRe(new ReReSettings().withReReMode(ReReMode.REPLAY)
                .withReReplayData(reReplayData));
        HttpClient replayDice = replayReRe.createReReObject(null, HttpClient.class);
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
