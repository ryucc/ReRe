/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.synthesizer.mockito;

import com.squareup.javapoet.MethodSpec;
import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.methods.Signature;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CodeUtils {


    public static boolean getVisibility(String packageName, Class<?> clazz) {
        int modifiers = clazz.getModifiers();;
        if (java.lang.reflect.Modifier.isPublic(modifiers)){
            return true;
        } else if(java.lang.reflect.Modifier.isPrivate(modifiers)) {
            return false;
        }
        Package pack = clazz.getPackage();
        return pack.getName().equals(packageName);
    }

    //TODO, use implemented interfaces first
    public static Class<?> getBestClass(String packageName, Class<?> runtimeClass, Class<?> lowerBoundClass) {
        if(runtimeClass.equals(String.class)) {
            return runtimeClass;
        }
        boolean visible = getVisibility(packageName, runtimeClass);
        boolean notFinal = !java.lang.reflect.Modifier.isFinal(runtimeClass.getModifiers());
        if(visible && notFinal) {
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
    public static void declareMock(Class<?> clazz, String name, MethodSpec.Builder methodBuilder) {
        methodBuilder.addStatement("$T $L = mock($T.class)", clazz, name, clazz);
    }

    public static void generateDo(MethodSpec.Builder methodBuilder, List<String> answerList, Signature signature) {
        String ans = String.join(".", answerList);

        String paramString = CodeUtils.generateParamString(signature.getParamTypes());
        methodBuilder.addStatement("$L.when(mockObject).$L($L)", ans, signature.getMethodName(), paramString);
    }
    public static String generateParamString(List<Type> paramTypes) {
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
                params.add("any()");
            }
        }

        return String.join(", ", params);
    }
}
