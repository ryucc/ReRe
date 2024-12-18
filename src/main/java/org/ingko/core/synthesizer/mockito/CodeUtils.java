package org.ingko.core.synthesizer.mockito;

import com.palantir.javapoet.MethodSpec;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.Signature;
import org.ingko.core.synthesizer.mockito.javafile.ParameterModSynthesizer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CodeUtils {

    public static List<ParameterModSynthesizer.MethodGroup> groupMethods(List<EnvironmentMethodCall> environmentMethodCalls) {
        Map<Signature, List<EnvironmentMethodCall>> m = environmentMethodCalls.stream()
                .collect(Collectors.groupingBy(EnvironmentMethodCall::getSignature));
        return m.entrySet()
                .stream()
                .map(e -> new ParameterModSynthesizer.MethodGroup(e.getKey(), e.getValue()))
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
