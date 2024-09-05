package org.parrot.core.data.objects;

import org.parrot.core.data.methods.MethodCall;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Node implements Serializable {
    private final List<MethodCall> methodCalls;
    private final UUID uuid;
    private final String value;
    private final boolean terminal;

    public boolean isSerialized() {
        return serialized;
    }

    private final boolean serialized;
    private final boolean failedNode;
    private final Class<?> runtimeClass;
    private final Class<?> representingClass;
    private final String comments;
    private final List<Node> directChildren;


    //TODO: ArrayNodes, serialization nodes, record Nodes
    public Node(Class<?> clazz) {
        this.methodCalls = new ArrayList<>();
        this.uuid = UUID.randomUUID();
        this.runtimeClass = clazz;
        this.representingClass = clazz;
        this.value = "";
        this.terminal = false;
        this.failedNode = false;
        this.comments = "";
        this.directChildren = new ArrayList<>();
        this.serialized = false;
    }

    public Node(Class<?> clazz, Class<?> returnClazz) {
        this.methodCalls = new ArrayList<>();
        this.uuid = UUID.randomUUID();
        this.runtimeClass = clazz;
        this.representingClass = returnClazz;
        this.value = "";
        this.terminal = false;
        this.failedNode = false;
        this.comments = "";
        this.directChildren = new ArrayList<>();
        this.serialized = false;
    }

    public Node(Class<?> clazz, Exception reason) {
        this.methodCalls = new ArrayList<>();
        this.uuid = UUID.randomUUID();
        this.runtimeClass = clazz;
        this.representingClass = clazz;
        this.value = "null";
        this.terminal = true;
        this.failedNode = true;
        this.comments = reason.getMessage();
        this.directChildren = new ArrayList<>();
        this.serialized = false;
    }

    public static Node ofSerialized(Class<?> clazz, String value) {
        return new Node(clazz, value, true);
    }

    public static Node ofRawValue(Class<?> clazz, String value) {
        return new Node(clazz, value);
    }
    public Node(Class<?> clazz, String value, boolean serialized) {
        this.methodCalls = new ArrayList<>();
        this.uuid = UUID.randomUUID();
        this.runtimeClass = clazz;
        this.representingClass = clazz;
        this.value = value;
        this.terminal = true;
        this.failedNode = false;
        this.comments = "";
        this.directChildren = new ArrayList<>();
        this.serialized = serialized;
    }

    public Node(Class<?> clazz, String value) {
        this.methodCalls = new ArrayList<>();
        this.uuid = UUID.randomUUID();
        this.runtimeClass = clazz;
        this.representingClass = clazz;
        this.value = value;
        this.terminal = true;
        this.failedNode = false;
        this.comments = "";
        this.directChildren = new ArrayList<>();
        this.serialized = false;
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

    public List<Node> getDirectChildren(){
        return directChildren;
    }

}
