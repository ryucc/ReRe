package org.katie.orange.core.data.objects;

import org.katie.orange.core.data.methods.MethodCall;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Node {
    private final List<MethodCall> methodCalls;
    private final UUID uuid;
    String className;



    public static final Set<Class<?>> primitiveClasses = Set.of(Integer.class,
            Byte.class, Character.class, Boolean.class, Double.class, Float.class, Long.class, Short.class,
            int.class, byte.class, char.class, boolean.class, double.class, float.class, long.class, short.class);
    public static final Set<Class<?>> voidClasses = Set.of(Void.class, void.class);

    public boolean isTerminal() {
        try {
            if (primitiveClasses.contains(Class.forName(className))) {
                return true;
            } else if (voidClasses.contains(Class.forName(className))) {
                return true;
            }
        } catch (Exception e){
            return false;
        }
        return false;
    }


    public Node(Class<?> clazz) {
        this.methodCalls = new ArrayList<>();
        this.className = clazz.getName();
        this.uuid = UUID.randomUUID();
    }

    public void addEdge(MethodCall methodCall) {
        methodCalls.add(methodCall);
    }

    public static Node ofSerialized(Serializable object) {
        return null;
    }

    public String getUniqueMockName() {
        String[] arr = className.split("\\.");
        String last = arr[arr.length - 1];
        int ind = last.indexOf('$');
        if (ind != -1) {
            last = last.substring(ind + 1);
        }
        return "mock" + last + "_" + uuid.toString().substring(0, 4);
    }
}
