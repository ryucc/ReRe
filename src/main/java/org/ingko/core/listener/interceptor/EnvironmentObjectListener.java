package org.ingko.core.listener.interceptor;

import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.LocalSymbol;
import org.ingko.core.data.methods.MethodResult;
import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.data.objects.UserNode;
import org.ingko.core.listener.EnvironmentNodeManager;
import org.ingko.core.listener.utils.EnvironmentObjectSpy;
import org.ingko.core.listener.utils.UserObjectSpy;
import org.ingko.core.listener.wrap.ParrotObjectWrapper;
import org.ingko.core.listener.wrap.ParrotWrapResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/*
TODO: better type inference
 */
//@SuppressWarnings("unchecked")
public class EnvironmentObjectListener implements ParrotMethodInterceptor<EnvironmentNode> {
    private final List<EnvironmentNode> roots;

    private final UserObjectListener userObjectListener;
    private final ParrotObjectWrapper<EnvironmentNode, EnvironmentNodeManager> wrapper;

    public EnvironmentObjectListener() {
        roots = new ArrayList<>();
        wrapper = new ParrotObjectWrapper<>(new EnvironmentNodeManager(this));
        userObjectListener = new UserObjectListener(wrapper);
    }

    public EnvironmentNode getRoot() {
        return roots.getFirst();
    }

    public <T> T createRoot(Object original, Class<T> targetClass) {
        ParrotWrapResult<T, EnvironmentNode> result = wrapper.createRoot(original, targetClass);
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
        //List<UserNode> params = new ArrayList<>();

        for (int i = 0; i < allArguments.length; i++) {
            Object cur = allArguments[i];
            //Class<?> argClass = orignalMethod.getParameterTypes()[i];
            Class<?> argClass = cur.getClass();
            LocalSymbol accessSymbol = new LocalSymbol(LocalSymbol.Source.PARAMETER, i);
            ParrotWrapResult<?, UserNode> result = userObjectListener.createRoot(cur, argClass, edge, accessSymbol);
            wrappedArguments[i] = result.wrapped();
            //params.add(result.userNode());
        }

        try {
            orignalMethod.setAccessible(true);
            returnValue = orignalMethod.invoke(original, wrappedArguments);
        } catch (InvocationTargetException e) {
            ParrotWrapResult<?, EnvironmentNode> result = wrapper.createRoot(e.getTargetException(),
                    e.getTargetException().getClass());
            edge.setReturnNode(result.node());
            edge.setResult(MethodResult.THROW);
            sourceNode.addMethodCall(edge);
            throw (Throwable) result.wrapped();
        } catch (IllegalAccessException e) {
            // Parrot does not have permissions to invoke the method.
            // should never happen?
            throw new RuntimeException(e);
        }

        edge.setResult(MethodResult.RETURN);

        if (returnValue instanceof UserObjectSpy) {
            UserNode returnedUserNode = ((UserObjectSpy) returnValue).getParrotUserNode();
            LocalSymbol symbol = returnedUserNode.getSymbol();
            edge.setReturnSymbol(symbol);
            returnValue = ((UserObjectSpy) returnValue).getParrotOriginObject();
        } else if (returnValue instanceof EnvironmentObjectSpy) {
            edge.setReturnSymbol(new LocalSymbol(LocalSymbol.Source.LOCAL_ENV, 0));
            returnValue = ((EnvironmentObjectSpy) returnValue).getParrotOriginObject();
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

        ParrotWrapResult<?, EnvironmentNode> result = wrapper.createRoot(returnValue, returnType);
        edge.setReturnNode(result.node());
        edge.setResult(MethodResult.RETURN);
        sourceNode.addMethodCall(edge);

        return result.wrapped();
    }
}