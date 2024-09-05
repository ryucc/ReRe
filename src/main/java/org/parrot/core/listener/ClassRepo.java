package org.parrot.core.listener;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.parrot.core.data.objects.Node;
import org.parrot.core.listener.exceptions.SubclassingException;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import static net.bytebuddy.matcher.ElementMatchers.not;

public class ClassRepo {
    private final Object interceptor;
    private final Map<Class<?>, Class<?>> classCache;

    public ClassRepo(Object interceptor) {
        this.interceptor = interceptor;
        this.classCache = new HashMap<>();
    }

    public Class<?> getOrDefineSubclass(Class<?> child, Class<?> target) throws SubclassingException {
        Queue<Class<?>> candidates = new ArrayDeque<>();
        if (target.isAssignableFrom(child)) {
            candidates.add(child);
        } else {
            throw new SubclassingException("Target class is not assignable from input.");
        }
        List<Class<?>> triedClasses = new ArrayList<>();
        List<Throwable> failureReason = new ArrayList<>();
        while (!candidates.isEmpty()) {
            Class<?> cur = candidates.poll();
            try {
                return getOrDefineSubclass(cur);
            } catch (SubclassingException e) {
                triedClasses.add(cur);
                failureReason.add(e);
            }
            if (cur.getSuperclass() != null && target.isAssignableFrom(cur.getSuperclass())) {
                candidates.add(cur.getSuperclass());
            }
            Class<?>[] interfaces = cur.getInterfaces();
            for (Class<?> implementedInterface : interfaces) {
                if (target.isAssignableFrom(implementedInterface) || target.equals(implementedInterface)) {
                    candidates.add(implementedInterface);
                }
            }
        }

        List<String> explainations = new ArrayList<>();

        for(int i = 0; i < failureReason.size(); i++) {
            String reason = String.format("%s failed with %s.", triedClasses.get(i).toString(),
                    failureReason.get(i).toString());
            explainations.add(reason);
        }
        String finalReason = String.join("\n", explainations);
        throw new SubclassingException("Can not subclass any suitable interface. The following classes were attempted: " + finalReason);
    }

    private <T> DynamicType.Builder<T> addField(DynamicType.Builder<T> builder, String fieldName, Class<?> fieldType) {
        String getterMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        String setterMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return builder.defineField(fieldName, fieldType, Visibility.PRIVATE)
                .defineMethod(getterMethodName, fieldType, Visibility.PUBLIC)
                .intercept(FieldAccessor.ofField(fieldName))
                .defineMethod(setterMethodName, void.class, Visibility.PUBLIC)
                .withParameter(fieldType)
                .intercept(FieldAccessor.ofField(fieldName));
    }

    public Class<?> getOrDefineSubclass(Class<?> target) throws SubclassingException {
        if (Modifier.isFinal(target.getModifiers())) {
            throw new SubclassingException("Cannot sub final class: " + target.getName());
        } else if (Modifier.isPrivate(target.getModifiers())) {
            throw new SubclassingException("Cannot sub private class: " + target.getName());
        }
        try {
            if (classCache.containsKey(target)) {
                return classCache.get(target);
            }
            DynamicType.Builder<?> builder = new ByteBuddy().subclass(target)
                    .method(not(ElementMatchers.isToString()).and(not(ElementMatchers.hasMethodName("fillInStackTrace")))
                            //.or(ElementMatchers.isHashCode().or(ElementMatchers.isEquals()))
                    )
                    .intercept(MethodDelegation.to(interceptor));
            builder = addField(builder, "parrotNodePointer", Node.class);
            builder = addField(builder, "parrotOriginObjectPointer", Object.class);

            Class<?> newClass = builder
                    .make()
                    .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                    .getLoaded();
            classCache.put(target, newClass);
            return newClass;
        } catch (Throwable t) {
            throw new SubclassingException("Cannot subclass", t);
        }
    }
}
