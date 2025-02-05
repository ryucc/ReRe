package org.rere.core.data.objects;

import org.rere.core.data.methods.EnvironmentMethodCall;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnvironmentNode implements Serializable, ReReObjectNode<EnvironmentNode> {
    private final List<EnvironmentMethodCall> environmentMethodCalls;
    private final UUID uuid;
    private final Class<?> runtimeClass;
    private final List<EnvironmentNode> directChildren;
    private String comments;
    private String value;
    private boolean terminal;
    private boolean serialized;
    private boolean failedNode;
    public EnvironmentNode(List<EnvironmentMethodCall> environmentMethodCalls,
                           UUID uuid,
                           String value,
                           boolean terminal,
                           boolean serialized,
                           boolean failedNode,
                           Class<?> runtimeClass,
                           String comments,
                           List<EnvironmentNode> directChildren) {
        this.environmentMethodCalls = environmentMethodCalls;
        this.uuid = uuid;
        this.value = value;
        this.terminal = terminal;
        this.serialized = serialized;
        this.failedNode = failedNode;
        this.runtimeClass = runtimeClass;
        this.comments = comments;
        this.directChildren = directChildren;
    }

    public static EnvironmentNode ofNull(Class<?> clazz) {
        return new EnvironmentNode(new ArrayList<>(),
                UUID.randomUUID(),
                "null",
                true,
                false,
                false,
                clazz,
                "",
                new ArrayList<>());
    }

    public static EnvironmentNode ofFailed(Class<?> clazz, String comments) {
        return new EnvironmentNode(new ArrayList<>(),
                UUID.randomUUID(),
                "null",
                true,
                false,
                true,
                clazz,
                comments,
                new ArrayList<>());
    }

    public static EnvironmentNode ofPrimitive(Class<?> clazz, String value) {
        return new EnvironmentNode(new ArrayList<>(),
                UUID.randomUUID(),
                value,
                true,
                false,
                false,
                clazz,
                "",
                new ArrayList<>());
    }

    public static EnvironmentNode ofInternal(Class<?> clazz) {
        return new EnvironmentNode(new ArrayList<>(),
                UUID.randomUUID(),
                "",
                false,
                false,
                false,
                clazz,
                "",
                new ArrayList<>());
    }

    public static EnvironmentNode ofSerialized(Class<?> clazz, String value) {
        return new EnvironmentNode(new ArrayList<>(),
                UUID.randomUUID(),
                value,
                true,
                true,
                false,
                clazz,
                "",
                new ArrayList<>());
    }

    public boolean isSerialized() {
        return serialized;
    }

    public void setSerialized(boolean serialized) {
        this.serialized = serialized;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public void addChild(EnvironmentNode child) {
        directChildren.add(child);
    }

    public boolean isFailedNode() {
        return failedNode;
    }

    public void setFailedNode(boolean failedNode) {
        this.failedNode = failedNode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Class<?> getDeclaredClass() {
        return runtimeClass;
    }

    public Class<?> getRuntimeClass() {
        return runtimeClass;
    }

    public List<EnvironmentMethodCall> getMethodCalls() {
        return environmentMethodCalls;
    }

    public boolean isTerminal() {
        return this.terminal;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    public void addMethodCall(EnvironmentMethodCall environmentMethodCall) {
        environmentMethodCalls.add(environmentMethodCall);
    }

    public List<EnvironmentNode> getDirectChildren() {
        return directChildren;
    }

}
