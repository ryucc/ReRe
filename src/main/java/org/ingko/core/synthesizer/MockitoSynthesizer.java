package org.ingko.core.synthesizer;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.ingko.core.data.methods.MethodCall;
import org.ingko.core.data.methods.MethodResult;
import org.ingko.core.data.methods.Signature;
import org.ingko.core.data.objects.Node;
import org.ingko.core.listener.Listener;
import org.ingko.core.serde.DefaultSerde;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MockitoSynthesizer {
    private final String packageName;
    private final String methodName;
    private final NamingStrategy namingStrategy;

    public MockitoSynthesizer(String packageName, String methodName) {
        this.packageName = packageName;
        this.methodName = methodName;
        namingStrategy = new OrderedNaming();
    }

    public String generateMockito(Listener listener) {
        Node root = listener.getRoot();
        Class<?> clazz = root.getRuntimeClass();
        String fileName = "Mock" + clazz.getSimpleName() + "Creator";
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(fileName).addModifiers(Modifier.PUBLIC);


        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(this.methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(clazz);
        generateObject(root, methodBuilder);
        methodBuilder.addStatement("return $L", namingStrategy.getUniqueMockName(root));
        typeBuilder.addMethod(methodBuilder.build());

        FieldSpec defaultSerde = FieldSpec.builder(DefaultSerde.class, "defaultSerde")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $T()", DefaultSerde.class)
                .build();
        typeBuilder.addField(defaultSerde);

        JavaFile javaFile = JavaFile.builder(this.packageName, typeBuilder.build())
                .addStaticImport(ArgumentMatchers.class, "*")
                .addStaticImport(Mockito.class, "doReturn")
                .build();
        return javaFile.toString();
    }

    private void generateObject(Node objectNode, MethodSpec.Builder methodBuilder) {
        if (objectNode.getRuntimeClass().isRecord()) {
            String mockName = namingStrategy.getUniqueMockName(objectNode);
            Class<?> clazz = objectNode.getRuntimeClass();

            List<String> childrenNames = new ArrayList<>();
            for (Node child : objectNode.getDirectChildren()) {
                generateObject(child, methodBuilder);
                childrenNames.add(namingStrategy.getUniqueMockName(child));
            }
            String params = childrenNames.stream().collect(Collectors.joining(", "));
            methodBuilder.addStatement("$T $L = new $T($L)", clazz, mockName, clazz, params);
            return;
        }
        if (objectNode.isSerialized()) {
            Class<?> clazz = objectNode.getRuntimeClass();
            String mockName = namingStrategy.getUniqueMockName(objectNode);
            methodBuilder
                    .addStatement("$T $L = ($T) defaultSerde.deserialize($S)", clazz, mockName, clazz, objectNode.getValue());
            return;
        }
        if (objectNode.isTerminal()) {
            String mockName = namingStrategy.getUniqueMockName(objectNode);
            Class<?> clazz = objectNode.getRuntimeClass();
            String comments = objectNode.getComments();
            if (!comments.isEmpty()) {
                String escaped = comments.replace("$", "$$");
                methodBuilder.addComment(escaped);
            }
            methodBuilder.addStatement("$T $L = $L", clazz, mockName, objectNode.getValue());
            return;
        }
        Map<Signature, List<String>> methodActions = new HashMap<>();
        for (MethodCall methodCall : objectNode.getMethodCalls()) {
            Signature signature = methodCall.getSignature();
            if (!methodActions.containsKey(signature)) {
                methodActions.put(signature, new ArrayList<>());
            }
            if (methodCall.isVoid()) {
                if (methodCall.getResult() == MethodResult.RETURN) {
                    methodActions.get(methodCall.getSignature()).add("doNothing().");
                } else if (methodCall.getResult() == MethodResult.THROW) {
                    Node throwable = methodCall.getDest();
                    generateObject(throwable, methodBuilder);
                    String action = String.format("doThrow(%s).", namingStrategy.getUniqueMockName(throwable));
                    methodActions.get(methodCall.getSignature()).add(action);
                }
            } else {
                if (methodCall.getResult() == MethodResult.RETURN) {
                    if (methodCall.getDest().isTerminal()) {
                        if (methodCall.getDest().getRuntimeClass().equals(String.class)) {
                            String action = String.format("doReturn(\"%s\").", methodCall.getDest().getValue());
                            methodActions.get(signature).add(action);
                        } else {
                            String action = String.format("doReturn(%s).", methodCall.getDest().getValue());
                            methodActions.get(signature).add(action);
                        }
                    } else {
                        Node returnVal = methodCall.getDest();
                        generateObject(returnVal, methodBuilder);
                        String action = String.format("doReturn(%s).", namingStrategy.getUniqueMockName(returnVal));
                        methodActions.get(signature).add(action);
                    }
                } else if (methodCall.getResult() == MethodResult.THROW) {
                    Node throwable = methodCall.getDest();
                    generateObject(throwable, methodBuilder);
                    String action = String.format("doThrow(%s).", namingStrategy.getUniqueMockName(throwable));
                    methodActions.get(methodCall.getSignature()).add(action);
                }
            }
        }
        Class<?> clazz = objectNode.getRuntimeClass();
        String mockName = namingStrategy.getUniqueMockName(objectNode);
        methodBuilder.addStatement("$T $L = $T.mock($T.class)", clazz, mockName, Mockito.class, clazz);

        for (Signature key : methodActions.keySet()) {
            //System.out.println(key);
            CodeBlock.Builder statement = CodeBlock.builder();
            List<String> actions = methodActions.get(key);
            for (String action : actions) {
                statement.add(action);
            }
            statement.add("when($L).", namingStrategy.getUniqueMockName(objectNode));

            statement.add("$L($L)", key.getMethodName(), generateParamString(key.getParamClasses()));
            methodBuilder.addStatement(statement.build());
        }
    }

    private String generateParamString(List<Class<?>> paramTypes) {
        List<String> params = new ArrayList<>();
        for (Class<?> clazz : paramTypes) {
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
