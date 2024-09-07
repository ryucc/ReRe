package org.parrot.core.listener;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.parrot.core.data.methods.MethodCall;
import org.parrot.core.data.methods.MethodResult;
import org.parrot.core.data.objects.Node;
import org.parrot.core.listener.exceptions.InitializationException;
import org.parrot.core.listener.utils.ClassUtils;
import org.parrot.core.listener.utils.ParrotFieldAccessors;
import org.parrot.core.serde.DefaultSerde;
import org.parrot.core.serde.exceptions.SerializationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unchecked")
public class Listener {
    private static final DefaultSerde defaultSerde = new DefaultSerde();
    List<Node> roots;
    ClassRepo classRepo;

    public Listener() {
        roots = new ArrayList<>();
        classRepo = new ClassRepo(this);
    }

    public Node getRoot() {
        return roots.getFirst();
    }

    public <T> T createRoot(Object original, Class<T> targetClass) {
        ListenResult<?> result = handleAnything(original, targetClass);
        roots.add(result.dataNode());
        return (T) result.wrapped();
    }

    public Object findClassThenInit(Object original, Class<?> clazz) throws InitializationException {
        // TODO: while final class, try to mock parent until parent == return class
        Class<?> mockedClass = classRepo.getOrDefineSubclass(original.getClass());
        return ObjectInitializer.create(mockedClass);
    }

    public <T> ListenResult<T> handleAnything(T returnValue, Class<?> targetClass) {
        // Need to think more about this.
        // targetClass will be Object.class on generics.
        if (returnValue == null || ClassUtils.isStringOrPrimitive(returnValue.getClass()) || ClassUtils.isStringOrPrimitive(
                targetClass)) {
            return handlePrimitiveOrNull(returnValue, targetClass);
        } else if (Throwable.class.isAssignableFrom(targetClass)) {
            return handleBySerialization(returnValue, targetClass);
        } else if (targetClass.isRecord()) {
            return handleRecord(returnValue, targetClass);
        } else if (targetClass.isArray()) {
            // do something
        }
        return handleInternal(returnValue, targetClass);
    }

    public <T> ListenResult<T> handleBySerialization(T returnValue, Class<?> targetClass) {

        try {
            String s = defaultSerde.serialize(returnValue);
            Node serializedNode = Node.ofSerialized(returnValue.getClass(), s);
            return new ListenResult<>(returnValue, serializedNode);
        } catch (SerializationException e) {
            Node failureNode = Node.ofFailed(returnValue.getClass(), e.getMessage());
            return new ListenResult<>(returnValue, failureNode);
        }
    }

    public <T> ListenResult<T> handleInternal(T returnValue, Class<?> targetClass) {
        try {
            T mocked = (T) findClassThenInit(returnValue, targetClass);
            Node dest = Node.ofInternal(returnValue.getClass());
            ParrotFieldAccessors.setOriginal(mocked, returnValue);
            ParrotFieldAccessors.setNode(mocked, dest);
            return new ListenResult<>(mocked, dest);
        } catch (Exception e) {
            Node dest = Node.ofFailed(returnValue.getClass(), e.getMessage());
            return new ListenResult<>(returnValue, dest);
        }
    }

    public <T> ListenResult<T> handlePrimitiveOrNull(T value, Class<?> clazz) {
        Node dest = switch (value) {
            case null -> Node.ofPrimitive(clazz, "null");
            case String sValue -> Node.ofPrimitive(String.class, "\"" + sValue + "\"");
            default -> Node.ofPrimitive(clazz, value.toString());
        };
        return new ListenResult<>(value, dest);
    }

    @RuntimeType
    public Object intercept(@AllArguments Object[] allArguments,
                            @Origin Method orignalMethod,
                            @This Object self) throws Throwable {
        Object original = ParrotFieldAccessors.getOriginal(self);
        Node source = ParrotFieldAccessors.getNode(self);

        Object returnValue;

        try {
            orignalMethod.setAccessible(true);
            returnValue = orignalMethod.invoke(original, allArguments);
        } catch (InvocationTargetException e) {
            ListenResult<Throwable> result = handleAnything(e.getTargetException(), e.getTargetException().getClass());
            MethodCall edge = new MethodCall(orignalMethod, source, result.dataNode, MethodResult.THROW);
            source.addEdge(edge);
            throw result.wrapped();
        } catch (IllegalAccessException e) {
            // Parrot does not have permissions to invoke the method.
            // should never happen?
            throw new RuntimeException(e);
        }

        ListenResult<?> result = handleAnything(returnValue, orignalMethod.getReturnType());
        MethodCall edge = new MethodCall(orignalMethod, source, result.dataNode, MethodResult.RETURN);
        source.addEdge(edge);

        return result.wrapped;
    }

    public <T> ListenResult<T> handleRecord(T original, Class<?> clazz) {
        Node cur = Node.ofInternal(clazz);

        RecordComponent[] components = original.getClass().getRecordComponents();
        List<Object> children = new ArrayList<>();
        for (RecordComponent component : components) {
            try {
                Object fieldValue = component.getAccessor().invoke(original);
                ListenResult<?> result = handleAnything(fieldValue, component.getType());
                cur.addDirectChild(result.dataNode());
                children.add(result.wrapped());
            } catch (IllegalAccessException | InvocationTargetException e) {
                // Failed to access fields for a record
                // this should never happen since all fields have public getters;
                Node failureNode = Node.ofFailed(original.getClass(), e.getMessage());
                return new ListenResult<>(original, failureNode);
            }
        }

        try {
            T dubbed = (T) ObjectInitializer.initRecord(clazz, children);
            return new ListenResult<>(dubbed, cur);
        } catch (InitializationException e) {
            Node failureNode = Node.ofFailed(original.getClass(), e.getMessage());
            return new ListenResult<>(original, failureNode);
        }
    }

    public record ListenResult<T>(T wrapped, Node dataNode) {
    }

}