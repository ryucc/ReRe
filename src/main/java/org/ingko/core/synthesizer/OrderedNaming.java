package org.ingko.core.synthesizer;

import org.ingko.core.data.objects.EnvironmentNode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrderedNaming implements NamingStrategy {

    private final Map<Class<?>, Map<UUID, Integer>> ids;

    public OrderedNaming() {
        ids = new HashMap<>();
    }

    private String forArray(Class<?> clazz) {
        Class<?> internal = clazz.getComponentType();
        return getClassName(internal) + "Array";
    }

    private String forEnclosed(Class<?> clazz) {
        String[] arr = clazz.getName().split("\\.");
        String last = arr[arr.length - 1];
        int ind = last.indexOf('$');
        if (ind != -1) {
            //return getClassName(clazz.getEnclosingClass()) + last.substring(ind + 1);
            return last.substring(ind + 1);
        }
        return last;
    }

    public String getClassName(Class<?> clazz) {
        if (clazz.isArray()) {
            return forArray(clazz);
        } else if (clazz.getEnclosingClass() != null) {
            return forEnclosed(clazz);
        } else {
            return forEnclosed(clazz);
        }
    }

    private Integer getIntegerId(EnvironmentNode environmentNode) {
        Class<?> clazz = environmentNode.getRuntimeClass();
        ids.putIfAbsent(clazz, new HashMap<>());
        Map<UUID, Integer> classIds = ids.get(clazz);
        classIds.putIfAbsent(environmentNode.getUuid(), classIds.size() + 1);
        return classIds.get(environmentNode.getUuid());
    }


    @Override
    public String getUniqueMockName(EnvironmentNode environmentNode) {
        Integer id = getIntegerId(environmentNode);
        String className = getClassName(environmentNode.getRuntimeClass());

        if (!environmentNode.isTerminal()) {
            return "mock" + className.substring(0, 1).toUpperCase() + className.substring(1) + id.toString();
        } else {
            return className.substring(0, 1).toLowerCase() + className.substring(1) + id.toString();
        }
    }
}
