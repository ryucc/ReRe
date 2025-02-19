/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.interceptor;

import org.rere.api.ReReSettings;
import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.methods.MethodResult;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.data.objects.LocalSymbol;
import org.rere.core.data.objects.UserNode;
import org.rere.core.listener.EnvironmentNodeManager;
import org.rere.core.listener.UserNodeManager;
import org.rere.core.listener.utils.EnvironmentObjectSpy;
import org.rere.core.listener.utils.UserObjectSpy;
import org.rere.core.wrap.EnvironmentObjectWrapper;
import org.rere.core.wrap.ReReWrapResult;
import org.rere.core.wrap.TopoOrderObjectWrapper;
import org.rere.core.wrap.mockito.UserObjectWrapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/*
TODO: better type inference
 */
public class EnvironmentObjectListener implements ReReMethodInterceptor<EnvironmentNode> {
    private final List<EnvironmentNode> roots;

    private EnvironmentObjectWrapper environmentObjectWrapper;
    private final UserObjectWrapper userObjectWrapper;

    private final boolean noParameterMod;
    private final Set<Class<?>> skipModClasses;

    public EnvironmentObjectListener() {
        this(new ReReSettings());
    }
    public EnvironmentObjectListener(ReReSettings reReSettings) {
        roots = new ArrayList<>();
        environmentObjectWrapper = new EnvironmentObjectWrapper(new EnvironmentNodeManager(this, reReSettings));
        UserObjectListener userObjectListener = new UserObjectListener(environmentObjectWrapper);
        userObjectWrapper = new UserObjectWrapper(new TopoOrderObjectWrapper<>(new UserNodeManager(userObjectListener)));
        userObjectListener.setUserObjectWrapper(userObjectWrapper);
        noParameterMod = reReSettings.noParameterModding();
        skipModClasses = reReSettings.skipMethodTracingClasses();
    }

    public void setEnvironmentObjectWrapper(EnvironmentObjectWrapper environmentObjectWrapper) {
        this.environmentObjectWrapper = environmentObjectWrapper;
    }

    public EnvironmentNode getRoot() {
        return roots.get(0);
    }

    public <T> T createRoot(Object original, Class<T> targetClass) {
        ReReWrapResult<T, EnvironmentNode> result = environmentObjectWrapper.createRoot(original, targetClass);
        roots.add(result.node());
        return result.wrapped();
    }


    public Object interceptInterface(Object original,
                                     Method orignalMethod,
                                     EnvironmentNode sourceNode,
                                     Object[] allArguments) throws Throwable {

        EnvironmentMethodCall edge = new EnvironmentMethodCall(orignalMethod);
        Object returnValue;

        Object[] wrappedArguments = new Object[allArguments.length];
        Class<?>[] argClasses = new Class[allArguments.length];

        //List<UserNode> params = new ArrayList<>();

        for (int i = 0; i < allArguments.length; i++) {
            Object cur = allArguments[i];
            if(noParameterMod){
                wrappedArguments[i] = cur;
                continue;
            }
            //Class<?> argClass = orignalMethod.getParameterTypes()[i];
            Class<?> runtimeClass = cur.getClass();
            if(skipModClasses.contains(runtimeClass)) {
                wrappedArguments[i] = cur;
                continue;
            }
            LocalSymbol accessSymbol = new LocalSymbol(LocalSymbol.Source.PARAMETER, i);
            Class<?> representingClass = orignalMethod.getParameterTypes()[i];
            ReReWrapResult<?, UserNode> result = userObjectWrapper.createRoot(cur, representingClass, edge, accessSymbol);
            wrappedArguments[i] = result.wrapped();
            argClasses[i] = runtimeClass;
            //params.add(result.userNode());
        }
        edge.setParamRuntimeClasses(Arrays.asList(argClasses));
        edge.setParamRepresentingClasses(Arrays.asList(orignalMethod.getParameterTypes()));

        try {
            orignalMethod.setAccessible(true);
            returnValue = orignalMethod.invoke(original, wrappedArguments);
        } catch (InvocationTargetException e) {
            ReReWrapResult<?, EnvironmentNode> result = environmentObjectWrapper.createRoot(e.getTargetException(),
                    e.getTargetException().getClass());
            edge.setReturnNode(result.node());
            edge.setResult(MethodResult.THROW);
            sourceNode.addMethodCall(edge);
            throw (Throwable) result.wrapped();
        } catch (IllegalAccessException e) {
            // ReRe does not have permissions to invoke the method.
            // should never happen?
            throw new RuntimeException(e);
        }

        edge.setResult(MethodResult.RETURN);

        if (returnValue instanceof UserObjectSpy) {
            UserNode returnedUserNode = ((UserObjectSpy) returnValue).getReReUserNode();
            LocalSymbol symbol = returnedUserNode.getSymbol();
            edge.setReturnSymbol(symbol);
            returnValue = ((UserObjectSpy) returnValue).getReReOriginObject();
        } else if (returnValue instanceof EnvironmentObjectSpy) {
            edge.setReturnSymbol(new LocalSymbol(LocalSymbol.Source.LOCAL_ENV, 0));
            returnValue = ((EnvironmentObjectSpy) returnValue).getReReOriginObject();
            // TODO: wrap into userNode?
        } else {
            edge.setReturnSymbol(new LocalSymbol(LocalSymbol.Source.LOCAL_ENV, 0));
        }

        /*
            User objects are wrapped here. At code synthesis, if return value only, return the mock.
            Else return the reference.
         */

        // Due to type erasure
        Class<?> representingReturnType = orignalMethod.getReturnType();

        ReReWrapResult<?, EnvironmentNode> result = environmentObjectWrapper.createRoot(returnValue, representingReturnType);
        edge.setReturnNode(result.node());
        edge.setReturnClass(representingReturnType);
        edge.setResult(MethodResult.RETURN);
        sourceNode.addMethodCall(edge);

        return result.wrapped();
    }
}