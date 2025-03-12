/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.replay;

import org.rere.api.ReReSettings;
import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.methods.MethodResult;
import org.rere.core.data.methods.Signature;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.data.objects.UserNode;
import org.rere.core.data.objects.reference.LocalSymbol;
import org.rere.core.listener.EnvironmentNodeManager;
import org.rere.core.listener.UserNodeManager;
import org.rere.core.listener.interceptor.ReReMethodInterceptor;
import org.rere.core.listener.interceptor.UserObjectListener;
import org.rere.core.listener.spies.EnvironmentObjectSpy;
import org.rere.core.listener.spies.UserObjectSpy;
import org.rere.core.listener.utils.ClassUtils;
import org.rere.core.wrap.EnvironmentObjectWrapper;
import org.rere.core.wrap.ReReWrapResult;
import org.rere.core.wrap.TopoOrderObjectWrapper;
import org.rere.core.wrap.mockito.UserObjectWrapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
TODO: better type inference
 */
public class ReplayObjectListener implements ReReMethodInterceptor<EnvironmentNode> {
    private final List<EnvironmentNode> roots;

    private EnvironmentObjectWrapper environmentObjectWrapper;
    private final UserObjectWrapper userObjectWrapper;

    private final boolean parameterModding;
    private final Set<Class<?>> skipModClasses;

    public ReplayObjectListener() {
        this(new ReReSettings());
    }
    public ReplayObjectListener(ReReSettings reReSettings) {
        roots = new ArrayList<>();
        environmentObjectWrapper = new EnvironmentObjectWrapper(new EnvironmentNodeManager(this, reReSettings));
        UserObjectListener userObjectListener = new UserObjectListener(environmentObjectWrapper);
        userObjectWrapper = new UserObjectWrapper(new TopoOrderObjectWrapper<>(new UserNodeManager(userObjectListener)));
        userObjectListener.setUserObjectWrapper(userObjectWrapper);
        parameterModding = reReSettings.parameterModding();
        skipModClasses = reReSettings.skipMethodTracingClasses();
    }

    public Object interceptInterface(Object original,
                                     Method orignalMethod,
                                     EnvironmentNode sourceNode,
                                     Object[] allArguments) throws Throwable {
        List<EnvironmentMethodCall> methodCalls = sourceNode.getMethodCalls();
        Signature signature = new Signature(orignalMethod);
        EnvironmentMethodCall target = null;
        // todo: start from current state
        for(EnvironmentMethodCall methodCall: methodCalls) {
            if(signature.equals(methodCall.getSignature())) {
                target = methodCall;
                break;
            }
        }
        if (target == null) {
            // pure replay: throw some error
            // checkpointing: call real object.
            return null;
        }

        Object result = unwrap(target.getDest());
    }
}