package org.ingko.core.listener;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.LocalSymbol;
import org.ingko.core.data.methods.MethodResult;
import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.data.objects.UserNode;
import org.ingko.core.listener.utils.EnvironmentObjectSpy;
import org.ingko.core.listener.utils.ObjectSpy;
import org.ingko.core.listener.utils.UserObjectSpy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/*
TODO: beter type inference
 */
@SuppressWarnings("unchecked")
public class EnvironmentObjectListener {
    private final List<EnvironmentNode> roots;

    private final UserObjectListener userObjectListener;
    private final EnvironmentObjectWrapper<EnvironmentNode, EnvironmentNodeManager> wrapper;

    public EnvironmentObjectListener() {
        roots = new ArrayList<>();
        ClassRepo classRepo = new ClassRepo(this,
                Map.of(EnvironmentObjectSpy.FIELD, EnvironmentObjectSpy.TYPE, ObjectSpy.FIELD, ObjectSpy.TYPE),
                List.of(EnvironmentObjectSpy.class, ObjectSpy.class));
        wrapper = new EnvironmentObjectWrapper<>(new EnvironmentNodeManager(classRepo));
        userObjectListener = new UserObjectListener(wrapper);
    }

    public EnvironmentNode getRoot() {
        return roots.getFirst();
    }

    public <T> T createRoot(Object original, Class<T> targetClass) {
        EnvironmentObjectWrapper.WrapResult<T,EnvironmentNode> result = wrapper.createRoot(original, targetClass);
        roots.add(result.dataEnvironmentNode());
        return result.wrapped();
    }

    @RuntimeType
    public Object intercept(@AllArguments Object[] allArguments,
                            @Origin Method orignalMethod,
                            @This Object self) throws Throwable {
        Object original = ((EnvironmentObjectSpy) self).getParrotOriginObject();
        EnvironmentNode source = ((EnvironmentObjectSpy) self).getParrotNodePointer();

        EnvironmentMethodCall edge = new EnvironmentMethodCall(orignalMethod);
        Object returnValue;

        Object[] wrappedArguments = new Object[allArguments.length];
        List<UserNode> params = new ArrayList<>();

        for (int i = 0; i < allArguments.length; i++) {
            Object cur = allArguments[i];
            Class<?> argClass = orignalMethod.getParameterTypes()[i];
            UserObjectListener.ListenResult<?> result = userObjectListener.handleAnything(cur,
                    argClass,
                    edge,
                    Map.of());
            result.userNode().setSymbol(new LocalSymbol(LocalSymbol.Source.PARAMETER, i));
            wrappedArguments[i] = result.wrapped();
            params.add(result.userNode());
        }

        try {
            orignalMethod.setAccessible(true);
            returnValue = orignalMethod.invoke(original, wrappedArguments);
        } catch (InvocationTargetException e) {
            EnvironmentObjectWrapper.WrapResult<?, EnvironmentNode> result = wrapper.createRoot(e.getTargetException(),
                    e.getTargetException().getClass());
            edge.registerReturnNode(result.dataEnvironmentNode());
            edge.setResult(MethodResult.THROW);
            source.addEdge(edge);
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

        EnvironmentObjectWrapper.WrapResult<?, EnvironmentNode> result = wrapper.createRoot(returnValue, orignalMethod.getReturnType());
        edge.registerReturnNode(result.dataEnvironmentNode());
        edge.setResult(MethodResult.RETURN);
        source.addEdge(edge);

        return result.wrapped();
    }

    public record ListenResult<T>(T wrapped, EnvironmentNode dataEnvironmentNode) {
    }

}