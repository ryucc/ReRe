package org.katie.orange.core.data.objects;

import org.katie.orange.core.data.methods.InternalMethodCall;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class ThrowableNode implements Serializable {

    private final Throwable e;
    private final UUID uuid;

    public ThrowableNode(Throwable e) {
        this.e = e;
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getDisplayName() {
        return "";
    }

}