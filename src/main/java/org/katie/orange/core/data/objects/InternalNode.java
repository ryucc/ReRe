package org.katie.orange.core.data.objects;

import org.katie.orange.core.data.methods.InternalMethodCall;
import org.katie.orange.core.data.methods.PrimitiveMethodCall;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InternalNode {
    private final UUID uuid;
    List<InternalMethodCall> internalMethodCalls;
    List<PrimitiveMethodCall> primitiveMethodCalls;
    List<InternalMethodCall> voidMethodCalls;
    String className;
    public InternalNode(Class<?> clazz) {
        internalMethodCalls = new ArrayList<>();
        voidMethodCalls = new ArrayList<>();
        primitiveMethodCalls = new ArrayList<>();
        this.className = clazz.getName();
        this.uuid = UUID.randomUUID();
    }

    public List<PrimitiveMethodCall> getPrimitiveMethodCalls() {
        return primitiveMethodCalls;
    }

    public String getUniqueMockName() {
        System.out.println(className);
        String[] arr = className.split("\\.");
        String last = arr[arr.length - 1];
        int ind = last.indexOf('$');
        if (ind != -1) {
            last = last.substring(ind + 1);
        }
        return "mock" + last + "_" + uuid.toString().substring(0, 4);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getDisplayName() {
        return className;
    }

    public void addEdge(InternalMethodCall edge) {
        internalMethodCalls.add(edge);
    }

    public void addPrimitiveEdge(PrimitiveMethodCall edge) {
        primitiveMethodCalls.add(edge);
    }

    public List<InternalMethodCall> getInternalMethodCalls() {
        return internalMethodCalls;
    }
}
