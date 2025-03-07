/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.synthesizer.mockito;

import com.squareup.javapoet.MethodSpec;
import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.methods.Signature;
import org.rere.core.data.objects.ReReObjectNode;
import org.rere.core.listener.utils.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CodeUtils {


    public static void addComments(MethodSpec.Builder methodBuilder, String comments) {
        if (comments.contains("\n")) {
            methodBuilder.addCode("/*\n");
            for (String s : comments.split("\n")) {
                methodBuilder.addCode(" * $L\n", s);
            }
            methodBuilder.addCode("*/\n");
        } else {
            methodBuilder.addComment("$L", comments);
        }
    }
    public static boolean getVisibility(String packageName, Class<?> clazz) {
        // check module

        Package pack = clazz.getPackage();
        Module mod = clazz.getModule();
        if(pack != null && mod != null) {
            boolean notExported = !mod.isExported(pack.getName());
            boolean notOpen = !mod.isOpen(pack.getName());
            if(notExported && notOpen) {
                return false;
            }
        }

        // check access
        int modifiers = clazz.getModifiers();
        if (java.lang.reflect.Modifier.isPublic(modifiers)) {
            return true;
        } else if (java.lang.reflect.Modifier.isPrivate(modifiers)) {
            return false;
        }
        return pack == null || pack.getName().equals(packageName);
    }
    public static Type getVisibleBestType(String packageName, ReReObjectNode<?> node) {
        if (node.getRuntimeClass().equals(Optional.class)) {
            if(node.getDirectChildren().isEmpty()) {
                Type[] typeArgs = {Object.class};
                return new ReReParamType(typeArgs, Optional.class, Optional.class);
            } else {
                Type childType = getVisibleBestType(packageName, node.getDirectChildren().get(0));
                Type[] typeArgs = {childType};
                return new ReReParamType(typeArgs, Optional.class, Optional.class);
            }
        }
        Class<?> runtimeClass = node.getRuntimeClass();
        Class<?> lowerBoundClass = node.getRepresentingClass();
        if (runtimeClass.equals(String.class)) {
            return runtimeClass;
        } else if(ClassUtils.isWrapperOrPrimitive(runtimeClass)) {
            return runtimeClass;
        }
        boolean visible = getVisibility(packageName, runtimeClass);
        if (visible) {
            return runtimeClass;
        }
        return lowerBoundClass;
    }

    //TODO, use implemented interfaces first
    // TODO final is okay for user nodes
    public static Type getNonFinalBestType(String packageName, ReReObjectNode<?> node) {
        if (node.getRuntimeClass().equals(Optional.class)) {
            if(node.getDirectChildren().isEmpty()) {
                Type[] typeArgs = {Object.class};
                return new ReReParamType(typeArgs, Optional.class, Optional.class);
            } else {
                Type childType = getNonFinalBestType(packageName, node.getDirectChildren().get(0));
                Type[] typeArgs = {childType};
                return new ReReParamType(typeArgs, Optional.class, Optional.class);
            }
        }
        Class<?> runtimeClass = node.getRuntimeClass();
        Class<?> lowerBoundClass = node.getRepresentingClass();
        if (runtimeClass.equals(String.class)) {
            return runtimeClass;
        } else if(ClassUtils.isWrapperOrPrimitive(runtimeClass)) {
            return runtimeClass;
        }
        boolean visible = getVisibility(packageName, runtimeClass);
        boolean notFinal = !java.lang.reflect.Modifier.isFinal(runtimeClass.getModifiers());
        if (visible && notFinal) {
            return runtimeClass;
        }
        return lowerBoundClass;
    }

    public static List<MockitoSynthesizer.MethodGroup> groupMethods(List<EnvironmentMethodCall> environmentMethodCalls) {
        Map<Signature, List<EnvironmentMethodCall>> m = environmentMethodCalls.stream()
                .collect(Collectors.groupingBy(EnvironmentMethodCall::getSignature));
        return m.entrySet()
                .stream()
                .map(e -> new MockitoSynthesizer.MethodGroup(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    public static void declareMock(Type clazz, String name, MethodSpec.Builder methodBuilder) {
        methodBuilder.addStatement("$T $L = mock($T.class)", clazz, name, clazz);
    }

    public static void generateWhen(MethodSpec.Builder methodBuilder, List<String> answerList, Signature signature) {
        String ans = String.join(".", answerList);

        String paramFormatString = CodeUtils.generateParamString(signature.getParamTypes());
        Object[] paramClassArgs = signature.getParamTypes().stream().filter(x -> !ClassUtils.isStringOrPrimitive(x))
                .collect(Collectors.toList()).stream().toArray();
        methodBuilder.addCode("$L.when(mockObject).$L", ans, signature.getMethodName());
        methodBuilder.addStatement(paramFormatString, paramClassArgs);
    }

    public static String generateParamString(List<Class<?>> paramTypes) {
        List<String> params = new ArrayList<>();
        for (Type clazz : paramTypes) {
            if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                params.add("anyInt()");
            } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
                params.add("anyDouble()");
            } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
                params.add("anyLong()");
            } else if (clazz.equals(Short.class) || clazz.equals(short.class)) {
                params.add("anyShort()");
            } else if (clazz.equals(Character.class) || clazz.equals(char.class)) {
                params.add("anyChar()");
            } else if (clazz.equals(Byte.class) || clazz.equals(byte.class)) {
                params.add("anyByte()");
            } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
                params.add("anyBoolean()");
            } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
                params.add("anyFloat()");
            } else if (clazz.equals(String.class)) {
                params.add("anyString()");
            } else {
                params.add("any($T.class)");
            }
        }

        return "(" + String.join(", ", params) + ")";
    }

    private static class ReReParamType implements ParameterizedType {
        private final Type[] typeArgs;
        private final Type rawType;
        private final Type ownerType;

        public ReReParamType(Type[] typeArgs, Type rawType, Type ownerType) {
            this.typeArgs = typeArgs;
            this.rawType = rawType;
            this.ownerType = ownerType;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return typeArgs;
        }

        @Override
        public Type getRawType() {
            return rawType;
        }

        @Override
        public Type getOwnerType() {
            return ownerType;
        }

        @Override
        public String getTypeName() {
            String fmt = "%s<%s>";

            return String.format(fmt,
                    rawType,
                    Arrays.stream(typeArgs)
                            .map(Type::getTypeName)
                            .collect(Collectors.joining(", ")));
        }

    }
}
