package org.ingko.core.data.objects;

import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.LocalSymbol;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserNode implements ParrotObjectNode {
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
    private final UUID uuid;
    /**
     * Scope needs to change to a EnvironmentNode for stateful objects
     */
    private EnvironmentMethodCall scope;
    private LocalSymbol symbol;
    private String comments;

    public List<UserNode> getDirectChildren() {
        return directChildren;
    }

    private final List<UserNode> directChildren;
    private boolean failedNode;

    public void addDirectChild(UserNode child) {
        directChildren.add(child);
    }

    public UserNode(Class<?> runtimeClass) {
        this(runtimeClass, null, UUID.randomUUID());
    }
    public UserNode(Class<?> runtimeClass, String comments) {
        this(runtimeClass, null, UUID.randomUUID(), comments);
    }
    public UserNode(Class<?> runtimeClass, EnvironmentMethodCall scope) {
        this(runtimeClass, scope, UUID.randomUUID());
    }

    public UserNode(Class<?> runtimeClass, EnvironmentMethodCall scope, UUID uuid) {
        this.runtimeClass = runtimeClass;
        this.scope = scope;
        this.uuid = uuid;
        directChildren = new ArrayList<>();
    }

    public UserNode(Class<?> runtimeClass, EnvironmentMethodCall scope, UUID uuid, String comments) {
        this.runtimeClass = runtimeClass;
        this.scope = scope;
        this.uuid = uuid;
        this.comments = comments;
        directChildren = new ArrayList<>();
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
    public void setFailedNode(boolean failedNode) {
        this.failedNode = failedNode;
    }

    public Class<?> getDeclaredClass() {
        return runtimeClass;
    }

    public EnvironmentMethodCall getScope() {
        return scope;
    }

    public void setScope(EnvironmentMethodCall scope) {
        this.scope = scope;
    }

    public UUID getUuid() {
        return uuid;
    }
}
