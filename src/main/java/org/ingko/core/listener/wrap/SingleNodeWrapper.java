package org.ingko.core.listener.wrap;

public interface SingleNodeWrapper<NODE> {
    <T> T initiateSpied(T returnValue, NODE node);
}
