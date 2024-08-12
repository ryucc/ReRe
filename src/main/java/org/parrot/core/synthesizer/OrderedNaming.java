package org.parrot.core.synthesizer;

import org.parrot.core.data.objects.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrderedNaming implements NamingStrategy {

    private final Map<Class<?>, Map<UUID, Integer>> ids;

    public OrderedNaming() {
        ids = new HashMap<>();
    }

    @Override
    public String getUniqueMockName(Node node) {
        Class<?> clazz = node.getClass();
        ids.putIfAbsent(clazz, new HashMap<>());

        Map<UUID, Integer> classIds = ids.get(clazz);

        classIds.putIfAbsent(node.getUuid(), classIds.size() + 1);
        Integer id = classIds.get(node.getUuid());

        String[] arr = node.getRuntimeClass().getName().split("\\.");
        String last = arr[arr.length - 1];
        int ind = last.indexOf('$');
        if (ind != -1) {
            last = last.substring(ind + 1);
        }
        if (!node.isTerminal()) {
            return "mock" + last + id.toString();
        }
        String lastlast = last.substring(0, 1).toLowerCase() + last.substring(1);
        return lastlast + id.toString();
    }
}
