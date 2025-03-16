/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay;

import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.objects.ReReObjectNode;

import java.util.List;

// Doens't need to be Serializable.
public class InOrderReplayNode implements ReReObjectNode<InOrderReplayNode> {

    final Class<?> representingClass;
    final Class<?> runtimeClass;
    private final List<EnvironmentMethodCall> methodCalls;
    private int nextCandidateMethod;

    public InOrderReplayNode(Class<?> representingClass,
                             Class<?> runtimeClass,
                             List<EnvironmentMethodCall> methodCalls) {
        this.representingClass = representingClass;
        this.runtimeClass = runtimeClass;
        this.methodCalls = methodCalls;
        this.nextCandidateMethod = 0;
    }
    public List<EnvironmentMethodCall> getMethodCalls() {
        return methodCalls;
    }

    public int getNextCandidateMethod() {
        return nextCandidateMethod;
    }

    public void setNextCandidateMethod(int nextCandidateMethod) {
        this.nextCandidateMethod = nextCandidateMethod;
    }

    @Override
    public Class<?> getRepresentingClass() {
        return representingClass;
    }

    @Override
    public Class<?> getRuntimeClass() {
        return runtimeClass;
    }

    @Override
    public void setFailedNode(boolean flag) {
        throw new RuntimeException("addChild not used for replay node.");
    }

    @Override
    public void setComments(String comments) {
        throw new RuntimeException("addChild not used for replay node.");
    }

    @Override
    public void addChild(InOrderReplayNode child) {
        throw new RuntimeException("addChild not used for replay node.");
    }

    @Override
    public List<? extends ReReObjectNode<InOrderReplayNode>> getDirectChildren() {
        return null;
    }
}