package org.katie.orange.core.data.objects;

import org.katie.orange.core.data.methods.InternalMethodCall;

import java.util.List;

public class NullNode {


    public String getDisplayName() {
        return "null";
    }
    public List<InternalMethodCall> getEdges() {
        return List.of();
    }
}
