/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.data.objects;

import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.objects.reference.LocalSymbol;

import java.util.ArrayList;
import java.util.List;

public class UserNode implements ReReObjectNode<UserNode> {
    /**
     * Runtime class implements representing type.
     * <p>
     * Will we ever need the runtime class?
     * no. we only need the lower bound.
     * <p>
     * Example:
     * <p>
     * List<Integer> is upper bound
     * ArrayList<Integer> is good lower bound
     * ArrayList<Object> is bad lower bound(runtime)
     * <p>
     * <p>
     * upper bound is good as long as user doesn't type cast downwards
     */
    private final Class<?> runtimeClass;
    private final Class<?> representingClass;
    private final List<UserNode> directChildren;
    /**
     * Remove scope, read from a global stack instead.
     */
    private EnvironmentMethodCall scope;
    private LocalSymbol symbol;

    public String getComments() {
        return comments;
    }

    private String comments;

    public boolean isFailedNode() {
        return failedNode;
    }

    private boolean failedNode;

    public UserNode(Class<?> runtimeClass,
                    Class<?> representingClass,
                    List<UserNode> directChildren,
                    EnvironmentMethodCall scope,
                    LocalSymbol symbol,
                    String comments,
                    boolean failedNode) {
        this.runtimeClass = runtimeClass;
        this.representingClass = representingClass;
        this.directChildren = directChildren;
        this.scope = scope;
        this.symbol = symbol;
        this.comments = comments;
        this.failedNode = failedNode;
    }

    public UserNode(Class<?> runtimeClass, Class<?> representingClass) {

        this(runtimeClass, representingClass, new ArrayList<>(), null, null, "", false);
    }

    public UserNode(Class<?> runtimeClass, String comments) {
        this(runtimeClass, runtimeClass, new ArrayList<>(), null, null, comments, false);
    }

    @Override
    public Class<?> getRepresentingClass() {
        return representingClass;
    }

    public List<UserNode> getDirectChildren() {
        return directChildren;
    }

    public LocalSymbol getSymbol() {
        return symbol;
    }

    public void setSymbol(LocalSymbol symbol) {
        this.symbol = symbol;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public void addChild(UserNode child) {
        directChildren.add(child);
    }

    @Override
    public void setFailedNode(boolean failedNode) {
        this.failedNode = failedNode;
    }

    @Override
    public Class<?> getRuntimeClass() {
        return runtimeClass;
    }

    public EnvironmentMethodCall getScope() {
        return scope;
    }

    public void setScope(EnvironmentMethodCall scope) {
        this.scope = scope;
    }

}
