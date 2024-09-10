package org.ingko.core.listener;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.MethodResult;
import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.exceptions.InitializationException;
import org.ingko.core.listener.utils.ClassUtils;
import org.ingko.core.listener.utils.ParrotFieldAccessors;
import org.ingko.core.serde.DefaultSerde;
import org.ingko.core.serde.exceptions.SerializationException;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("unchecked")
public class Listener {
    private static final DefaultSerde defaultSerde = new DefaultSerde();
    List<EnvironmentNode> roots;
    ClassRepo classRepo;

    public Listener() {
        roots = new ArrayList<>();
        classRepo = new ClassRepo(this);
    }

    public EnvironmentNode getRoot() {
        return roots.getFirst();
    }

    public <T> T createRoot(Object original, Class<T> targetClass) {
        ListenResult<?> result = handleAnything(original, targetClass);
        roots.add(result.dataEnvironmentNode());
        return (T) result.wrapped();
    }

    public Object findClassThenInit(Object original, Class<?> clazz) throws InitializationException {
        // TODO: while final class, try to mock parent until parent == return class
        Class<?> mockedClass = classRepo.getOrDefineSubclass(original.getClass());
        return ObjectInitializer.create(mockedClass);
    }

    public <T> ListenResult<T> handleAnything(T returnValue, Class<?> targetClass) {
        return handleAnything(returnValue, targetClass, new HashMap<>());
    }

    public <T> ListenResult<T> handleAnything(T returnValue, Class<?> targetClass, Map<Object, ListenResult<?>> explored) {
        // Need to think more about this.
        // targetClass will be Object.class on generics.
        if (returnValue == null || ClassUtils.isStringOrPrimitive(returnValue.getClass())) {
            return handlePrimitiveOrNull(returnValue, targetClass);
        } else if (Throwable.class.isAssignableFrom(targetClass)) {
            return handleBySerialization(returnValue, targetClass);
        } else if (returnValue.getClass().isRecord()) {
            return handleRecord(returnValue, targetClass, explored);
        } else if (returnValue.getClass().isArray()) { // TODO: componentType or runtime type?
            return handleArray(returnValue, targetClass, explored);
        }
        return handleInternal(returnValue, targetClass);
    }

    public <T> ListenResult<T> handleArray(T original, Class<?> clazz, Map<Object, ListenResult<?>> explored) {
        if(explored.containsKey(original)) {
            // Loop detection might cause duplicate record heads.
            // Should not be a problem.
            // Example
            // R' -> A <-> R
            return (ListenResult<T>) explored.get(original);
        }

        List<Object> children = new ArrayList<>();
        int size = Array.getLength(original);
        Class<?> componentType = clazz.getComponentType();

        EnvironmentNode cur = EnvironmentNode.ofInternal(clazz);
        Object aObject = Array.newInstance(componentType, size);

        for (int i = 0; i < size; i++) {
            Object child = Array.get(original, i);
            explored.put(original, new ListenResult<>(aObject, cur));
            ListenResult<?> result = handleAnything(child, componentType, explored);
            cur.addDirectChild(result.dataEnvironmentNode());
            children.add(result.wrapped());
        }

        int length = Array.getLength(aObject);
        for (int i = 0; i < length; i++) {
            Array.set(aObject, i, children.get(i));
        }
        T dubbed = (T) aObject;
        return new ListenResult<>(dubbed, cur);
    }

    public <T> ListenResult<T> handleBySerialization(T returnValue, Class<?> targetClass) {

        try {
            String s = defaultSerde.serialize(returnValue);
            EnvironmentNode serializedEnvironmentNode = EnvironmentNode.ofSerialized(returnValue.getClass(), s);
            return new ListenResult<>(returnValue, serializedEnvironmentNode);
        } catch (SerializationException e) {
            EnvironmentNode failureEnvironmentNode = EnvironmentNode.ofFailed(returnValue.getClass(), e.getMessage());
            return new ListenResult<>(returnValue, failureEnvironmentNode);
        }
    }

    public <T> ListenResult<T> handleInternal(T returnValue, Class<?> targetClass) {
        try {
            T mocked = (T) findClassThenInit(returnValue, targetClass);
            EnvironmentNode dest = EnvironmentNode.ofInternal(returnValue.getClass());
            ParrotFieldAccessors.setOriginal(mocked, returnValue);
            ParrotFieldAccessors.setNode(mocked, dest);
            return new ListenResult<>(mocked, dest);
        } catch (Exception e) {
            EnvironmentNode dest = EnvironmentNode.ofFailed(returnValue.getClass(), e.getMessage());
            return new ListenResult<>(returnValue, dest);
        }
    }

    public <T> ListenResult<T> handlePrimitiveOrNull(T value, Class<?> clazz) {
        EnvironmentNode dest = switch (value) {
            case null -> EnvironmentNode.ofPrimitive(clazz, "null");
            case String sValue -> EnvironmentNode.ofPrimitive(String.class, "\"" + sValue + "\"");
            default -> EnvironmentNode.ofPrimitive(clazz, value.toString());
        };
        return new ListenResult<>(value, dest);
    }

    @RuntimeType
    public Object intercept(@AllArguments Object[] allArguments,
                            @Origin Method orignalMethod,
                            @This Object self) throws Throwable {
        Object original = ParrotFieldAccessors.getOriginal(self);
        EnvironmentNode source = ParrotFieldAccessors.getNode(self);

        Object returnValue;

        try {
            orignalMethod.setAccessible(true);
            returnValue = orignalMethod.invoke(original, allArguments);
        } catch (InvocationTargetException e) {
            ListenResult<Throwable> result = handleAnything(e.getTargetException(), e.getTargetException().getClass());
            EnvironmentMethodCall edge = new EnvironmentMethodCall(orignalMethod, source, result.dataEnvironmentNode, MethodResult.THROW);
            source.addEdge(edge);
            throw result.wrapped();
        } catch (IllegalAccessException e) {
            // Parrot does not have permissions to invoke the method.
            // should never happen?
            throw new RuntimeException(e);
        }

        ListenResult<?> result = handleAnything(returnValue, orignalMethod.getReturnType());
        EnvironmentMethodCall edge = new EnvironmentMethodCall(orignalMethod, source, result.dataEnvironmentNode, MethodResult.RETURN);
        source.addEdge(edge);

        return result.wrapped;
    }

    public <T> ListenResult<T> handleRecord(T original, Class<?> clazz, Map<Object, ListenResult<?>> explored) {
        EnvironmentNode cur = EnvironmentNode.ofInternal(original.getClass());

        RecordComponent[] components = original.getClass().getRecordComponents();
        List<Object> children = new ArrayList<>();
        for (RecordComponent component : components) {
            try {
                Object fieldValue = component.getAccessor().invoke(original);
                ListenResult<?> result = handleAnything(fieldValue, component.getType(), explored);
                cur.addDirectChild(result.dataEnvironmentNode());
                children.add(result.wrapped());
            } catch (IllegalAccessException | InvocationTargetException e) {
                // Failed to access fields for a record
                // this should never happen since all fields have public getters;
                EnvironmentNode failureEnvironmentNode = EnvironmentNode.ofFailed(original.getClass(), e.getMessage());
                return new ListenResult<>(original, failureEnvironmentNode);
            }
        }

        try {
            T dubbed = (T) ObjectInitializer.initRecord(original.getClass(), children);
            return new ListenResult<>(dubbed, cur);
        } catch (InitializationException e) {
            EnvironmentNode failureEnvironmentNode = EnvironmentNode.ofFailed(original.getClass(), e.getMessage());
            return new ListenResult<>(original, failureEnvironmentNode);
        }
    }

    public record ListenResult<T>(T wrapped, EnvironmentNode dataEnvironmentNode) {
    }

}