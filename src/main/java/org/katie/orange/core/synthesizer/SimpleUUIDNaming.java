package org.katie.orange.core.synthesizer;

import org.katie.orange.core.data.objects.Node;

public class SimpleUUIDNaming implements NamingStrategy {

    @Override
    public String getUniqueMockName(Node node) {
        String[] arr = node.getRuntimeClass().getName().split("\\.");
        String last = arr[arr.length - 1];
        int ind = last.indexOf('$');
        if (ind != -1) {
            last = last.substring(ind + 1);
        }
        if (!node.isTerminal()) {
            return "mock" + last + "_" + node.getUuid().toString().substring(0, 4);
        }
        String lastlast = last.substring(0, 1).toLowerCase() + last.substring(1);
        return lastlast + "_" + node.getUuid().toString().substring(0, 4);
    }
}
