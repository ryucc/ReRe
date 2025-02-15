/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.data.objects;

import org.rere.core.data.methods.EnvironmentMethodCall;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents the serialization a Environment Object.
 */
public class EnvironmentNode implements Serializable, ReReObjectNode<EnvironmentNode> {
    private final List<EnvironmentMethodCall> environmentMethodCalls;
    private final UUID uuid;

    /**
     * The class of the original object at runtime. Maybe final, private, or anonymous.
     * This is the first choice type to reconstruct the object as, to avoid casting problems.
     */
    private final Class<?> runtimeClass;

    /**
     * The lower bound class this object must be.
     * If this environment object was returned from another Environment Object, the representing class
     * would be the return class from that method's signature.
     * If this environment object was a field of a record, the representingClass would be the field type.
     */
    private final Class<?> representingClass;
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
                           Class<?> representingClass,
                           String comments,
                           List<EnvironmentNode> directChildren) {
        this.environmentMethodCalls = environmentMethodCalls;
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

    public static EnvironmentNode ofNull(Class<?> clazz) {
        return new EnvironmentNode(new ArrayList<>(),
                UUID.randomUUID(),
                "null",
                true,
                false,
                false,
                clazz,
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
                clazz,
                "",
                new ArrayList<>());
    }

    public static EnvironmentNode ofInternal(Class<?> runtimeClass, Class<?> representingClass) {
        return new EnvironmentNode(new ArrayList<>(),
                UUID.randomUUID(),
                "",
                false,
                false,
                false,
                runtimeClass,
                representingClass,
                "",
                new ArrayList<>());
    }

    @Override
    public Class<?> getRepresentingClass() {
        return representingClass;
    }

    public boolean isSerialized() {
        return serialized;
    }

    public void setSerialized(boolean serialized) {
        this.serialized = serialized;
    }

    /**
     * Comments when creating this node. Usually why we failed tracing the object.
     *
     * @return Plaintext comments.
     */
    public String getComments() {
        return comments;
    }

    /**
     * TODO: make immutable.
     *
     * @param comments
     */
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

    public Class<?> getRuntimeClass() {
        return runtimeClass;
    }

    public List<EnvironmentMethodCall> getMethodCalls() {
        return environmentMethodCalls;
    }

    /**
     * Terminal if this is a null node, or serialized node.
     * Terminal objects do not have methods tracked.
     *
     * @return true if node is terminal.
     */
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
