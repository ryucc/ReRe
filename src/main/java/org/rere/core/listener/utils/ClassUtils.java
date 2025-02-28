/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClassUtils {
    public static final Map<Type, Type> wrapperMap = new HashMap<Type, Type>() {{
        put(int.class, Integer.class);
        put(byte.class, Byte.class);
        put(char.class, Character.class);
        put(boolean.class, Boolean.class);
        put(double.class, Double.class);
        put(float.class, Float.class);
        put(long.class, Long.class);
        put(short.class, Short.class);
        put(void.class, Void.class);
    }};
    private static final Class<?> recordClass = initRecordClass();
    private static final Set<Class<?>> wrappers = new HashSet<>(Arrays.asList(Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class));

    public static boolean arrayEquals(Object arr, Object brr) {
        if(arr == null && brr == null) {
            return true;
        } else if(arr == null || brr == null) {
            return false;
        }
        if (!arr.getClass().equals(brr.getClass())) {
            return false;
        }
        if(isWrapperOrPrimitive(arr.getClass())) {
            return arr.equals(brr);
        }

        int lenA = Array.getLength(arr);
        int lenB = Array.getLength(brr);
        if (lenA != lenB) {
            return false;
        }
        for(int i = 0; i < lenA; i++) {
            Object a = Array.get(arr, i);
            Object b = Array.get(brr, i);
            if(!arrayEquals(a, b)) {
                return false;
            }
        }
        return true;
    }
    public static <T> T deepCopyArray(T arr) {
        if(arr == null) {
            return null;
        } else if(ClassUtils.isWrapperOrPrimitive(arr.getClass())) {
            return arr;
        }
        int len = Array.getLength(arr);
        Class<?> type = arr.getClass().getComponentType();
        T copy = (T) Array.newInstance(type , len);
        for (int i = 0; i < len; i++) {
            Array.set(copy, i, deepCopyArray(Array.get(arr, i)));
        }
        return copy;
    }
    public static <T> void shallowCopyIntoArray(T src, T dest) {
        int len = Array.getLength(src);
        for (int i = 0; i < len; i++) {
            Array.set(dest, i, Array.get(src, i));
        }
    }

    public static boolean isPrimitiveArray(Class<?> a) {
        if(isWrapperOrPrimitive(a)) {
            return false;
        }
        Class<?> next = a;
        while(next.isArray()) {
            next = next.getComponentType();
        }
        return isWrapperOrPrimitive(next);
    }

    private static Class<?> initRecordClass() {
        try {
            return Class.forName("java.lang.Record");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static boolean isRecord(Class<?> clazz) {
        if (recordClass == null) {
            return false;
        }
        return recordClass.isAssignableFrom(clazz);
    }

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
