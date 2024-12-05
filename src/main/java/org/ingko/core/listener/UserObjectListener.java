package org.ingko.core.listener;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.LocalSymbol;
import org.ingko.core.data.methods.UserMethodCall;
import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.data.objects.UserNode;
import org.ingko.core.listener.exceptions.InitializationException;
import org.ingko.core.listener.utils.ClassUtils;
import org.ingko.core.listener.utils.EnvironmentObjectSpy;
import org.ingko.core.listener.utils.ObjectSpy;
import org.ingko.core.listener.utils.UserObjectSpy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserObjectListener {

    private final EnvironmentObjectListener environmentObjectListener;

    private final ClassRepo classRepo;

    public UserObjectListener(EnvironmentObjectListener environmentObjectListener) {
        this.environmentObjectListener = environmentObjectListener;
        classRepo = new ClassRepo(this,
                Map.of(ObjectSpy.FIELD, ObjectSpy.TYPE,
                        UserObjectSpy.FIELD, UserObjectSpy.TYPE),
                List.of(ObjectSpy.class, UserObjectSpy.class));
    }

    public <T> ListenResult<T> handlePrimitiveOrNull(T value, Class<?> clazz, EnvironmentMethodCall scope) {
        // Primitives are passed by value
        // The local changes will not affect the method caller.
        // Thus, we don't need to track them.
        UserNode node = new UserNode(value.getClass(), scope);
        return new ListenResult<>(value, node);
    }

    public <T> ListenResult<T> findClassThenInit(T original,
                                                 Class<?> clazz,
                                                 EnvironmentMethodCall scope) throws InitializationException {
        // TODO: while final class, try to mock parent until parent == return class
        Class<?> mockedClass = classRepo.getOrDefineSubclass(original.getClass());
        T mocked = (T) ObjectInitializer.create(mockedClass);
        ((UserObjectSpy) mocked).setParrotOriginObject(original);
        UserNode node = new UserNode(original.getClass(), scope);
        ((UserObjectSpy) mocked).setParrotUserNode(node);
        return new ListenResult<>(mocked, node);
    }

    public <T> ListenResult<T> handleInternal(T returnValue, Class<?> clazz, EnvironmentMethodCall scope) {
        try {
            return findClassThenInit(returnValue, clazz, scope);
        } catch (Exception e) {
            UserNode node = new UserNode(returnValue.getClass(), scope);
            node.setComments("Failed node");
            return new ListenResult<>(returnValue, node);
        }
    }

    public <T> ListenResult<T> handleAnything(T returnValue, Class<?> targetClass, EnvironmentMethodCall scope) {
        return handleAnything(returnValue, targetClass, scope, Map.of());
    }

    public <T> ListenResult<T> handleAnything(T returnValue,
                                              Class<?> targetClass,
                                              EnvironmentMethodCall scope,
                                              Map<Object, EnvironmentObjectListener.ListenResult<?>> explored) {
        // Need to think more about this.
        // targetClass will be Object.class on generics.
        if (returnValue == null || ClassUtils.isStringOrPrimitive(returnValue.getClass())) {
            return handlePrimitiveOrNull(returnValue, targetClass, scope);
        } else if (Throwable.class.isAssignableFrom(targetClass)) {
            // Don't track for now...
            UserNode userNode = new UserNode(returnValue.getClass(), scope);
            return new ListenResult<>(returnValue, userNode);
        } else if (returnValue.getClass().isRecord()) {
            //return handleRecord(returnValue, targetClass, explored);
        } else if (returnValue.getClass().isArray()) { // TODO: componentType or runtime type?
            //return handleArray(returnValue, targetClass, explored);
        }
        return handleInternal(returnValue, targetClass, scope);
    }


    @RuntimeType
    public Object intercept(@AllArguments Object[] allArguments,
                            @Origin Method orignalMethod,
                            @This Object self) throws Throwable {
        Object original = ((UserObjectSpy) self).getParrotOriginObject();

        List<EnvironmentNode> environmentNodes = new ArrayList<>();
        List<Object> wrappedArguments = new ArrayList<>();
        List<LocalSymbol> parameterSourceList = new ArrayList<>();
        for (Object arg : allArguments) {
            if (arg instanceof UserObjectSpy) {
                UserNode node = ((UserObjectSpy) arg).getParrotUserNode();
                parameterSourceList.add(node.getSymbol());
                Object origin = ((UserObjectSpy) arg).getParrotOriginObject();
                wrappedArguments.add(origin);
            } else if (arg instanceof EnvironmentObjectSpy) {
                // should never happen?
                // nope, this will happen
                // Can ignore only if this is a stateless non user-environment node.
                // Let's assume environments are stateless first... handle this later.
                System.err.println("User method call received environment object as parameter.");
            } else {
                EnvironmentObjectListener.ListenResult<?> listenResult = environmentObjectListener.handleAnything(arg, arg.getClass());

                int curIndex = environmentNodes.size();
                LocalSymbol symbol = new LocalSymbol(LocalSymbol.Source.LOCAL_ENV, curIndex);
                parameterSourceList.add(symbol);

                environmentNodes.add(listenResult.dataEnvironmentNode());
                wrappedArguments.add(listenResult.wrapped());
            }
        }
        // register method call to scope

        UserNode userNode = ((UserObjectSpy) self).getParrotUserNode();
        EnvironmentMethodCall scopeMethod = userNode.getScope();

        {
            String methodName = orignalMethod.getName();
            LocalSymbol operand = userNode.getSymbol();
            UserMethodCall userMethodCall = new UserMethodCall(operand,
                    methodName,
                    environmentNodes,
                    parameterSourceList,
                    orignalMethod.getGenericReturnType());
            scopeMethod.addUserMethodCall(userMethodCall);
        }

        // We can set the return values after registering...
        // The return values only need a pointer to the scope and its index.

        int currentReturnIndex = scopeMethod.getLastReturnIndex();

        try {
            orignalMethod.setAccessible(true);
            Object ret = orignalMethod.invoke(original, wrappedArguments.toArray());

            if (ret instanceof EnvironmentObjectSpy) {
                // Environment object: untrack. Even if its member is user object, the method calls will be traced.
                // Stateful: need to make it into a environment - user object
                return ((EnvironmentObjectSpy) ret).getParrotOriginObject();
            }
            if (ret instanceof UserObjectSpy) {
                // User Object: do nothing is okay, but rewrap is probably better. (Can register new localSymbol)
                // Rewrapping makings synthesized code look closer to original code.
                ret = ((UserObjectSpy) ret).getParrotOriginObject();
            }
            ListenResult<?> result = handleAnything(ret, ret.getClass(), scopeMethod);
            LocalSymbol symbol = new LocalSymbol(LocalSymbol.Source.RETURN_VALUE, currentReturnIndex);
            result.userNode().setSymbol(symbol);
            return result.wrapped();
        } catch (InvocationTargetException e) {
            Throwable real = e.getTargetException();
            ListenResult<Throwable> result = handleAnything(real, real.getClass(), scopeMethod);
            LocalSymbol symbol = new LocalSymbol(LocalSymbol.Source.THROW, currentReturnIndex);
            result.userNode().setSymbol(symbol);
            throw result.wrapped();
        } catch (IllegalAccessException e) {
            // Parrot does not have permissions to invoke the method.
            // should never happen?
            throw new RuntimeException(e);
        }
    }

    public record ListenResult<T>(T wrapped, UserNode userNode) {
    }
}
