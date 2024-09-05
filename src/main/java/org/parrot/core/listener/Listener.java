package org.parrot.core.listener;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.parrot.core.data.methods.MethodCall;
import org.parrot.core.data.methods.MethodResult;
import org.parrot.core.data.objects.Node;
import org.parrot.core.listener.exceptions.InitializationException;
import org.parrot.core.listener.utils.ParrotFieldAccessors;
import org.parrot.core.listener.utils.ClassUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@SuppressWarnings("unchecked")
public class Listener {
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
        if (returnValue == null || ClassUtils.isStringOrPrimitive(targetClass)) {
            return handlePrimitiveOrNull(returnValue, targetClass);
        } else if(Serializable.class.isAssignableFrom(targetClass)) {
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
            Base64.Encoder encoder = Base64.getEncoder();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(encoder.wrap(baos));
            objectOutputStream.writeObject(returnValue);
            objectOutputStream.flush();
            objectOutputStream.close();
            String s = baos.toString();
            Node serializedNode = new Node(returnValue.getClass(), s);
            return new ListenResult<>(returnValue, serializedNode);
        } catch (IOException e) {
            Node failureNode = new Node(returnValue.getClass(), e);
            return new ListenResult<>(returnValue, failureNode);
        }
    }

    public <T> ListenResult<T> handleInternal(T returnValue, Class<?> targetClass) {
        try {
            T mocked = (T) findClassThenInit(returnValue, targetClass);
            Node dest = new Node(returnValue.getClass());
            ParrotFieldAccessors.setOriginal(mocked, returnValue);
            ParrotFieldAccessors.setNode(mocked, dest);
            return new ListenResult<>(mocked, dest);
        } catch (Exception e) {
            Node dest = new Node(returnValue.getClass(), e);
            return new ListenResult<>(returnValue, dest);
        }
    }

    public <T> ListenResult<T> handlePrimitiveOrNull(T value, Class<?> clazz) {
        Node dest = switch (value) {
            case null -> new Node(clazz, "null");
            case String sValue -> new Node(String.class, "\"" + sValue + "\"");
            default -> new Node(clazz, value.toString());
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
        Node cur = new Node(clazz);

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
                Node failureNode = new Node(original.getClass(), e);
                cur.addDirectChild(failureNode);
                return new ListenResult<>(original, new Node(clazz));
            }
        }

        try {
            T dubbed = (T) ObjectInitializer.initRecord(clazz, components, children);
            return new ListenResult<>(dubbed, cur);
        } catch (InitializationException e) {
            Node failureNode = new Node(original.getClass(), e);
            return new ListenResult<>(original, failureNode);
        }
    }

    public record ListenResult<T>(T wrapped, Node dataNode) {
    }

}