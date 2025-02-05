package org.rere.core.listener.interceptor;

import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.objects.LocalSymbol;
import org.rere.core.data.methods.MethodResult;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.data.objects.UserNode;
import org.rere.core.listener.EnvironmentNodeManager;
import org.rere.core.listener.utils.EnvironmentObjectSpy;
import org.rere.core.listener.utils.UserObjectSpy;
import org.rere.core.listener.wrap.TopoOrderObjectWrapper;
import org.rere.core.listener.wrap.ReReWrapResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
TODO: better type inference
 */
public class EnvironmentObjectListener implements ReReMethodInterceptor<EnvironmentNode> {
    private final List<EnvironmentNode> roots;

    private UserObjectListener userObjectListener;

    public void setEnvironmentObjectWrapper(TopoOrderObjectWrapper<EnvironmentNode, EnvironmentNodeManager> environmentObjectWrapper) {
        this.environmentObjectWrapper = environmentObjectWrapper;
    }

    private TopoOrderObjectWrapper<EnvironmentNode, EnvironmentNodeManager> environmentObjectWrapper;

    public EnvironmentObjectListener() {
        roots = new ArrayList<>();
        environmentObjectWrapper = new TopoOrderObjectWrapper<>(new EnvironmentNodeManager(this));
        userObjectListener = new UserObjectListener(environmentObjectWrapper);
    }

    public EnvironmentNode getRoot() {
        return roots.getFirst();
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
            //Class<?> argClass = orignalMethod.getParameterTypes()[i];
            Class<?> argClass = cur.getClass();
            LocalSymbol accessSymbol = new LocalSymbol(LocalSymbol.Source.PARAMETER, i);
            ReReWrapResult<?, UserNode> result = userObjectListener.createRoot(cur, argClass, edge, accessSymbol);
            wrappedArguments[i] = result.wrapped();
            argClasses[i] = argClass;
            //params.add(result.userNode());
        }
        edge.setParamClasses(Arrays.asList(argClasses));

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
        Class<?> returnType = returnValue == null ? orignalMethod.getReturnType() : returnValue.getClass();

        ReReWrapResult<?, EnvironmentNode> result = environmentObjectWrapper.createRoot(returnValue, returnType);
        edge.setReturnNode(result.node());
        edge.setReturnClass(returnType);
        edge.setResult(MethodResult.RETURN);
        sourceNode.addMethodCall(edge);

        return result.wrapped();
    }
}