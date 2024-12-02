package org.ingko.core.data.objects;

import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.LocalSymbol;

import java.util.UUID;

public class UserNode {
    /**
     * Runtime class implements representing type.
     *
     * Will we ever need the runtime class?
     * no. we only need the lower bound.
     *
     * Example:
     *
     * List<Integer> is upper bound
     * ArrayList<Integer> is good lower bound
     * ArrayList<Object> is bad lower bound(runtime)
     *
     *
     * upper bound is good as long as user doesn't type cast downwards
     */
    private final Class<?> runtimeClass;
    /**
     * Scope needs to change to a EnvironmentNode for stateful objects
     */
    private final EnvironmentMethodCall scope;
    private final UUID uuid;

    public LocalSymbol getSymbol() {
        return symbol;
    }

    public void setSymbol(LocalSymbol symbol) {
        this.symbol = symbol;
    }

    private LocalSymbol symbol;

    public void setComments(String comments) {
        this.comments = comments;
    }

    private String comments;

    public UserNode(Class<?> runtimeClass, EnvironmentMethodCall scope) {
        this(runtimeClass, scope, UUID.randomUUID());
    }
    public UserNode(Class<?> runtimeClass, EnvironmentMethodCall scope, UUID uuid) {
        this.runtimeClass = runtimeClass;
        this.scope = scope;
        this.uuid = uuid;
    }
    public UserNode(Class<?> runtimeClass, EnvironmentMethodCall scope, UUID uuid, String comments) {
        this.runtimeClass = runtimeClass;
        this.scope = scope;
        this.uuid = uuid;
        this.comments = comments;
    }

    public Class<?> getDeclaredClass() {
        return runtimeClass;
    }

    public EnvironmentMethodCall getScope() {
        return scope;
    }

    public UUID getUuid() {
        return uuid;
    }
}
