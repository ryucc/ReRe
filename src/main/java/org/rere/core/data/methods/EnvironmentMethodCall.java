/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.data.methods;

import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.data.objects.LocalSymbol;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnvironmentMethodCall implements Serializable {

    /*
    1. Parameters (symbol name important)
    2. repeat:
        2.1 Declare env nodes
        2.2 call methods
            2.2.1 look up symbol name
    3. return val
     */

    private final Signature signature;

    public List<Class<?>> getParamRuntimeClasses() {
        return paramRuntimeClasses;
    }

    private List<Class<?>> paramRuntimeClasses;

    public void setParamRuntimeClasses(List<Class<?>> paramClasses) {
        this.paramRuntimeClasses = paramClasses;
    }
    public void setParamRepresentingClasses(List<Class<?>> paramClasses) {
        this.signature.setParamClasses(paramClasses);
    }
    public List<Class<?>> getParamRepresentingClasses() {
        return this.signature.getParamClasses();
    }
    // Dynamic
    private final UUID uuid;
    private final List<UserMethodCall> userMethodCalls;
    private EnvironmentNode mockReturn;
    /* TODO later: the return values need to be stored on the node.
        In case user objects are stored, then modified later across method calls.
        Example use case:
        envObj.saveObject(obj);
        envObj.modifyObject(obj);
     */
    private LocalSymbol returnSymbol;
    private Class<?> returnClass;
    private MethodResult result;

    public EnvironmentMethodCall(Method method) {
        this.uuid = UUID.randomUUID();
        this.signature = new Signature(method);
        this.userMethodCalls = new ArrayList<>();
    }

    public Class<?> getReturnClass() {
        return returnClass;
    }

    public void setReturnClass(Class<?> returnClass) {
        this.returnClass = returnClass;
    }

    public List<UserMethodCall> getUserMethodCalls() {
        return userMethodCalls;
    }

    public void setReturnNode(EnvironmentNode node) {

        mockReturn = node;
    }

    public void addUserMethodCall(UserMethodCall userMethodCall) {
        userMethodCalls.add(userMethodCall);
    }

    public int getLastReturnIndex() {
        return userMethodCalls.size() - 1;
    }

    public LocalSymbol getReturnSymbol() {
        return returnSymbol;
    }

    public void setReturnSymbol(LocalSymbol returnSymbol) {
        this.returnSymbol = returnSymbol;
    }

    public Signature getSignature() {
        return signature;
    }

    public EnvironmentNode getDest() {
        return mockReturn;
    }

    public String getName() {
        return signature.getMethodName();
    }

    public MethodResult getResult() {
        return result;
    }

    public void setResult(MethodResult result) {
        this.result = result;
    }

    public boolean isVoid() {
        return signature.getReturnType().equals("java.lang.Void") || signature.getReturnType().equals("void");
    }
}
