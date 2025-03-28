/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.interceptor;

import org.rere.api.ReReSettings;
import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.methods.MethodResult;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.data.objects.reference.LocalSymbol;
import org.rere.core.data.objects.UserNode;
import org.rere.core.listener.EnvironmentNodeManager;
import org.rere.core.listener.UserNodeManager;
import org.rere.core.listener.utils.ClassUtils;
import org.rere.core.listener.spies.EnvironmentObjectSpy;
import org.rere.core.listener.spies.UserObjectSpy;
import org.rere.core.wrap.EnvironmentObjectWrapper;
import org.rere.core.wrap.ReReWrapResult;
import org.rere.core.wrap.TopoOrderObjectWrapper;
import org.rere.core.wrap.mockito.UserObjectWrapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Set;

/*
TODO: better type inference
 */
public class EnvironmentObjectListener implements ReReMethodInterceptor<EnvironmentNode> {
    private final List<EnvironmentNode> roots;

    private EnvironmentObjectWrapper environmentObjectWrapper;
    private final UserObjectWrapper userObjectWrapper;

    private final boolean parameterModding;
    private final Set<Class<?>> skipModClasses;
    public EnvironmentObjectListener(ReReSettings reReSettings) {
        roots = new ArrayList<>();
        environmentObjectWrapper = new EnvironmentObjectWrapper(new EnvironmentNodeManager(this, reReSettings));
        UserObjectListener userObjectListener = new UserObjectListener(environmentObjectWrapper);
        userObjectWrapper = new UserObjectWrapper(new TopoOrderObjectWrapper<>(new UserNodeManager(userObjectListener)));
        userObjectListener.setUserObjectWrapper(userObjectWrapper);
        parameterModding = reReSettings.parameterModding();
        skipModClasses = reReSettings.skipMethodTracingClasses();
    }

    public void setEnvironmentObjectWrapper(EnvironmentObjectWrapper environmentObjectWrapper) {
        this.environmentObjectWrapper = environmentObjectWrapper;
    }

    public Object interceptInterface(Object original,
                                     Method orignalMethod,
                                     EnvironmentNode sourceNode,
                                     Object[] allArguments) throws Throwable {

        Map<Integer, Object> initialArrays = new HashMap<>();
        for(int i = 0; i < allArguments.length; i++) {
            Object arg = allArguments[i];
            if(arg == null) continue;
            if(ClassUtils.isPrimitiveArray(arg.getClass())) {
                Object copy = ClassUtils.deepCopyArray(arg);
                initialArrays.put(i, copy);
            }
        }

        EnvironmentMethodCall edge = new EnvironmentMethodCall(orignalMethod);
        Object returnValue;

        Object[] wrappedArguments = new Object[allArguments.length];
        Class<?>[] argClasses = new Class[allArguments.length];
        List<UserNode> paramNodes = new ArrayList<>();

        //List<UserNode> params = new ArrayList<>();

        for (int i = 0; i < allArguments.length; i++) {
            Object cur = allArguments[i];
            if(!parameterModding){
                wrappedArguments[i] = cur;
                continue;
            }
            if(cur != null && ClassUtils.isPrimitiveArray(cur.getClass())) {
                // TODO need to match by reference for return value
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
            paramNodes.add(result.node());
            argClasses[i] = runtimeClass;
            if(result.node().isFailedNode()) {
                edge.getFailedUserObjects().add(result.wrapped());
                edge.getFailedUserNodes().add(result.node());
            }
            //params.add(result.userNode());
        }

        edge.setParameterNodes(paramNodes);
        edge.setParamRepresentingClasses(Arrays.asList(orignalMethod.getParameterTypes()));

        try {
            orignalMethod.setAccessible(true);
            returnValue = orignalMethod.invoke(original, wrappedArguments);
        } catch (InvocationTargetException e) {
            ReReWrapResult<?, EnvironmentNode> result = environmentObjectWrapper.wrapObject(e.getTargetException(),
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
        /*
         *  Record changes to primitive arrays here.
         *  primitive array changes on throws: skip for now. Maybe todo later.
         */
        for(int i = 0; i < allArguments.length; i++) {
            Object arg = allArguments[i];
            if(arg == null) continue;
            if(ClassUtils.isPrimitiveArray(arg.getClass()) &&
                !ClassUtils.arrayEquals(initialArrays.get(i), arg)) {
                Object copy = ClassUtils.deepCopyArray(arg);
                edge.getEndResult().put(i, copy);
            }
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

        ReReWrapResult<?, EnvironmentNode> result = environmentObjectWrapper.wrapObject(returnValue, representingReturnType);
        edge.setReturnNode(result.node());
        edge.setReturnClass(representingReturnType);
        edge.setResult(MethodResult.RETURN);
        sourceNode.addMethodCall(edge);

        return result.wrapped();
    }
}