package org.katie.orange.core.synthesizer;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.katie.orange.core.data.methods.MethodCall;
import org.katie.orange.core.data.methods.MethodResult;
import org.katie.orange.core.data.methods.Signature;
import org.katie.orange.core.data.objects.Node;
import org.katie.orange.core.listener.Listener;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeSynthesizer {
    private final String packageName;
    private final String methodName;

    public CodeSynthesizer(String packageName, String methodName) {
        this.packageName = packageName;
        this.methodName = methodName;
    }

    public String generateMockito(Listener listener) {
        Node root = listener.getRoot();
        Class<?> clazz = root.getClazz();
        String fileName = "Mock" + clazz.getSimpleName() + "Creator";
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(fileName).addModifiers(Modifier.PUBLIC);


        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(this.methodName).addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(clazz);
        generateObject(root, methodBuilder);
        methodBuilder.addStatement("return $L", root.getUniqueMockName());
        typeBuilder.addMethod(methodBuilder.build());

        JavaFile javaFile = JavaFile.builder(this.packageName, typeBuilder.build()).addStaticImport(ArgumentMatchers.class, "*").addStaticImport(Mockito.class, "doReturn").build();
        return javaFile.toString();
    }

    private void generateObject(Node objectNode, MethodSpec.Builder methodBuilder) {
        Map<Signature, List<String>> childObjectNames = new HashMap<>();
        for (MethodCall methodCall : objectNode.getMethodCalls()) {
            Signature signature = methodCall.getSignature();
            if (methodCall.getResult() == MethodResult.RETURN) {
                if (!childObjectNames.containsKey(signature)) {
                    childObjectNames.put(signature, new ArrayList<>());
                }
                if (methodCall.getDest().isTerminal()) {
                    if (methodCall.getDest().getClazz().equals(String.class)) {
                        String value = methodCall.getDest().getValue();
                        childObjectNames.get(signature).add("\"" + value + "\"");
                    } else {
                        childObjectNames.get(signature).add(methodCall.getDest().getValue());
                    }
                } else {
                    generateObject(methodCall.getDest(), methodBuilder);
                    childObjectNames.get(signature).add(methodCall.getDest().getUniqueMockName());
                }
            }
        }
        Class<?> clazz = objectNode.getClazz();
        String mockName = objectNode.getUniqueMockName();
        methodBuilder.addStatement("$T $L = $T.mock($T.class)", clazz, mockName, Mockito.class, clazz);

        for (Signature key : childObjectNames.keySet()) {
            System.out.println(key);
            CodeBlock.Builder statement = CodeBlock.builder();
            List<String> returnNames = childObjectNames.get(key);
            for (String name : returnNames) {
                statement.add("doReturn($L).", name);
            }
            statement.add("when($L).", objectNode.getUniqueMockName());

            statement.add("$L($L)", key.getMethodName(), generateParamString(key.getParamClasses()));
            methodBuilder.addStatement(statement.build());
        }
    }

    private String generateParamString(List<Class<?>> paramTypes) {
        List<String> params = new ArrayList<>();
        for(Class<?> clazz : paramTypes) {
            if(clazz.equals(Integer.class) || clazz.equals(int.class)) {
                params.add("anyInt()");
            } else if(clazz.equals(Double.class) || clazz.equals(double.class)) {
                params.add("anyDouble()");
            } else if(clazz.equals(Long.class) || clazz.equals(long.class)) {
                params.add("anyLong()");
            } else if(clazz.equals(Short.class) || clazz.equals(short.class)) {
                params.add("anyShort()");
            } else if(clazz.equals(Character.class) || clazz.equals(char.class)) {
                params.add("anyChar()");
            } else if(clazz.equals(Byte.class) || clazz.equals(byte.class)) {
                params.add("anyByte()");
            } else if(clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
                params.add("anyBoolean()");
            } else if(clazz.equals(Float.class) || clazz.equals(float.class)) {
                params.add("anyFloat()");
            } else if(clazz.equals(String.class)) {
                params.add("anyString()");
            } else {
                params.add("any()");
            }
        }

        return String.join(", ", params);
    }
}
