package org.katie.orange.core.data.objects;

import org.katie.orange.core.data.methods.MethodCall;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class Node {
    public static final Set<Class<?>> primitiveClasses = Set.of(Integer.class, Byte.class, Character.class, Boolean.class, Double.class, Float.class, Long.class, Short.class, int.class, byte.class, char.class, boolean.class, double.class, float.class, long.class, short.class);
    public static final Set<Class<?>> voidClasses = Set.of(Void.class, void.class);
    private final List<MethodCall> methodCalls;
    private final UUID uuid;

    public String getValue() {
        return value;
    }

    private final String value;
    private final boolean terminal;
    String className;
    transient Class<?> clazz;

    public Node(Class<?> clazz) {
        this.methodCalls = new ArrayList<>();
        this.className = clazz.getName();
        this.uuid = UUID.randomUUID();
        this.clazz = clazz;
        this.value = "";
        this.terminal = false;
    }

    public Node(Class<?> clazz, String value) {
        this.methodCalls = new ArrayList<>();
        this.className = clazz.getName();
        this.uuid = UUID.randomUUID();
        this.clazz = clazz;
        this.value = value;
        this.terminal = true;
    }

    public static Node ofSerialized(Serializable object) {
        return null;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public List<MethodCall> getMethodCalls() {
        return methodCalls;
    }

    public boolean isTerminal() {
        return this.terminal;
        /*
        try {
            if (primitiveClasses.contains(Class.forName(className))) {
                return true;
            } else if (voidClasses.contains(Class.forName(className))) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
         */
    }

    public void addEdge(MethodCall methodCall) {
        methodCalls.add(methodCall);
    }

    public String getUniqueMockName() {
        String[] arr = className.split("\\.");
        String last = arr[arr.length - 1];
        int ind = last.indexOf('$');
        if (ind != -1) {
            last = last.substring(ind + 1);
        }
        if(!terminal) {
            return "mock" + last + "_" + uuid.toString().substring(0, 4);
        }
        String lastlast = last.substring(0,1).toLowerCase() + last.substring(1);
        return lastlast + "_" + uuid.toString().substring(0, 4);
    }
}
