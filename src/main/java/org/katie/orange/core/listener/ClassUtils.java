package org.katie.orange.core.listener;

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
    private static final Set<Class<?>> wrappers = Set.of(Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            Void.class);
    public static boolean isWrapper(Class<?> clazz) {
        return wrappers.contains(clazz);
    }

    public static boolean isWrapperOrPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() || wrappers.contains(clazz);
    }

    public static boolean isStringOrPrimitive(Class<?> clazz) {
        return String.class.equals(clazz) || isWrapperOrPrimitive(clazz);
    }
}
