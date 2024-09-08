package org.ingko.core.data.objects;

import org.ingko.core.data.methods.MethodCall;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Node implements Serializable {
    private final List<MethodCall> methodCalls;
    private final UUID uuid;
    private final String value;
    private final boolean terminal;
    private final boolean serialized;
    private final boolean failedNode;
    private final Class<?> runtimeClass;
    private final Class<?> representingClass;
    private final String comments;
    private final List<Node> directChildren;
    public Node(List<MethodCall> methodCalls,
                UUID uuid,
                String value,
                boolean terminal,
                boolean serialized,
                boolean failedNode,
                Class<?> runtimeClass,
                Class<?> representingClass,
                String comments,
                List<Node> directChildren) {
        this.methodCalls = methodCalls;
        this.uuid = uuid;
        this.value = value;
        this.terminal = terminal;
        this.serialized = serialized;
        this.failedNode = failedNode;
        this.runtimeClass = runtimeClass;
        this.representingClass = representingClass;
        this.comments = comments;
        this.directChildren = directChildren;
    }

    public static Node ofFailed(Class<?> clazz, String comments) {
        return new Node(new ArrayList<>(),
                UUID.randomUUID(),
                "null",
                true,
                false,
                true,
                clazz,
                clazz,
                comments,
                new ArrayList<>());
    }

    public static Node ofPrimitive(Class<?> clazz, String value) {
        return new Node(new ArrayList<>(),
                UUID.randomUUID(),
                value,
                true,
                false,
                false,
                clazz,
                clazz,
                "",
                new ArrayList<>());
    }
    public static Node ofInternal(Class<?> clazz) {
        return new Node(new ArrayList<>(),
                UUID.randomUUID(),
                "",
                false,
                false,
                false,
                clazz,
                clazz,
                "",
                new ArrayList<>());
    }

    public static Node ofSerialized(Class<?> clazz, String value) {
        return new Node(new ArrayList<>(),
                UUID.randomUUID(),
                value,
                true,
                true,
                false,
                clazz,
                clazz,
                "",
                new ArrayList<>());
    }

    public boolean isSerialized() {
        return serialized;
    }

    public String getComments() {
        return comments;
    }

    public boolean isFailedNode() {
        return failedNode;
    }

    public String getValue() {
        return value;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Class<?> getRuntimeClass() {
        return runtimeClass;
    }

    public List<MethodCall> getMethodCalls() {
        return methodCalls;
    }

    public boolean isTerminal() {
        return this.terminal;
    }

    public void addEdge(MethodCall methodCall) {
        methodCalls.add(methodCall);
    }

    public void addDirectChild(Node node) {
        directChildren.add(node);
    }

    public List<Node> getDirectChildren() {
        return directChildren;
    }

}
