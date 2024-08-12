package org.parrot.core.listener;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.parrot.core.data.methods.MethodCall;
import org.parrot.core.data.methods.MethodResult;
import org.parrot.core.data.objects.Node;
import org.parrot.core.listener.exceptions.InitializationException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Listener {
    List<Node> roots;
    Map<Object, Object> originMap;
    Map<Object, Node> nodeMap;
    ClassRepo classRepo;

    public Listener() {
        roots = new ArrayList<>();
        originMap = new HashMap<>();
        nodeMap = new HashMap<>();
        classRepo = new ClassRepo(this);
    }

    public Node getRoot() {
        return roots.getFirst();
    }

    @SuppressWarnings("unchecked")
    public <T> T createRoot(T original) {
        try {
            Node root = new Node(original.getClass());
            T mocked = (T) createAndRegister(original, root);
            roots.add(root);
            return mocked;
        } catch (InitializationException  e) {
            Node root = new Node(original.getClass(), e);
            roots.add(root);
            return original;
        }
    }

    public Object createAndRegister(Object original, Node node) throws InitializationException {
        // TODO: while final class, try to mock parent until parent == return class
        Class<?> mockedClass = classRepo.getOrDefineSubclass(original.getClass());
        Object mocked = ObjectInitializer.create(mockedClass);
        originMap.put(mocked, original);
        nodeMap.put(mocked, node);
        return mocked;
    }
    public Exception handleThrow(Node source, Method method, Exception exception) {
        try {
            Node dest = new Node(exception.getClass());
            Exception ee = (Exception) createAndRegister(exception, dest);
            MethodCall edge = new MethodCall(method, source, dest, MethodResult.THROW);
            source.addEdge(edge);
            return ee;
        } catch (InitializationException e) {
            Node dest = new Node(exception.getClass(), e);
            MethodCall edge = new MethodCall(method, source, dest, MethodResult.THROW);
            source.addEdge(edge);
            return exception;
        }
    }

    public void handleNull(Node source, Method method) {
        Node dest = new Node(method.getReturnType());
        MethodCall edge = new MethodCall(method, source, dest, MethodResult.RETURN);
        source.addEdge(edge);
    }

    public void handlePrimitive(Node source, Method method, String value) {
        Node dest = new Node(method.getReturnType(), value);
        MethodCall edge = new MethodCall(method, source, dest, MethodResult.RETURN);
        source.addEdge(edge);
    }

    @RuntimeType
    public Object intercept(@AllArguments Object[] allArguments,
                            @Origin Method orignalMethod,
                            @This Object self) throws Exception {
        Object original = originMap.get(self);
        Node source = nodeMap.get(self);
        Object returnValue;

        try {
            returnValue = orignalMethod.invoke(original, allArguments);
        } catch (Exception e) {
            throw handleThrow(source, orignalMethod, e);
        }

        if (returnValue == null) {
            //void also returns null
            handleNull(source, orignalMethod);
            return null;
        } else if (ClassUtils.isStringOrPrimitive(orignalMethod.getReturnType())) {
            handlePrimitive(source, orignalMethod, returnValue.toString());
            return returnValue;
        }

        try {
            Node dest = new Node(returnValue.getClass());
            Object mocked = createAndRegister(returnValue, dest);
            MethodCall edge = new MethodCall(orignalMethod, source, dest, MethodResult.RETURN);
            source.addEdge(edge);
            return mocked;
        } catch (InitializationException e) {
            Node dest = new Node(returnValue.getClass(), e);
            MethodCall edge = new MethodCall(orignalMethod, source, dest, MethodResult.RETURN);
            source.addEdge(edge);
            return returnValue;
        }
    }
}