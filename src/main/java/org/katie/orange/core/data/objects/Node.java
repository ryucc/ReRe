package org.katie.orange.core.data.objects;

import org.katie.orange.core.data.methods.MethodCall;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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

    public UUID getUuid() {
        return uuid;
    }

    private final Class<?> runtimeClass;

    public Node(Class<?> clazz) {
        this.methodCalls = new ArrayList<>();
        this.uuid = UUID.randomUUID();
        this.runtimeClass = clazz;
        this.value = "";
        this.terminal = false;
    }

    public Node(Class<?> clazz, String value) {
        this.methodCalls = new ArrayList<>();
        this.uuid = UUID.randomUUID();
        this.runtimeClass = clazz;
        this.value = value;
        this.terminal = true;
    }

    public static Node ofSerialized(Serializable object) {
        return null;
    }

    public Class<?> getRuntimeClass() {
        return runtimeClass;
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

}
