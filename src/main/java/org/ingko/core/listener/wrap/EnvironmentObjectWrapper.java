package org.ingko.core.listener.wrap;

import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.EnvironmentNodeManager;
import org.ingko.core.listener.interceptor.EnvironmentObjectListener;

public class EnvironmentObjectWrapper extends ParrotObjectWrapper<EnvironmentNode, EnvironmentNodeManager>{
    public EnvironmentObjectWrapper(EnvironmentNodeManager nodeManager) {
        super(nodeManager);
    }
}
