/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.utils;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

public class ClassUtils {
    public static final Set<Class<?>> voidClasses = Set.of(Void.class, void.class);

    public static final Set<Class<?>> primitiveClasses = Set.of(Integer.class,
            Byte.class,
            Character.class,
            Boolean.class,
            Double.class,
            Float.class,
            Long.class,
            Short.class,
            int.class,
            byte.class,
            char.class,
            boolean.class,
            double.class,
            float.class,
            long.class,
            short.class);

    public static final Map<Type, Type> wrapperMap = Map.of(int.class,
            Integer.class,
            byte.class,
            Byte.class,
            char.class,
            Character.class,
            boolean.class,
            Boolean.class,
            double.class,
            Double.class,
            float.class,
            Float.class,
            long.class,
            Long.class,
            short.class,
            Short.class,
            void.class,
            Void.class);
    private static final Set<Class<?>> wrappers = Set.of(Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class);

    public static Type getWrapped(Type clazz) {
        return wrapperMap.getOrDefault(clazz, clazz);
    }

    public static boolean isWrapperOrPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() || wrappers.contains(clazz);
    }

    public static boolean isString(Class<?> clazz) {
        return String.class.equals(clazz);
    }
    public static boolean isStringOrPrimitive(Class<?> clazz) {
        return String.class.equals(clazz) || isWrapperOrPrimitive(clazz);
    }
}
