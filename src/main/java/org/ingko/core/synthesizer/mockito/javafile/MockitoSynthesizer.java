package org.ingko.core.synthesizer.mockito.javafile;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.EnvironmentObjectListener;
import org.ingko.core.synthesizer.NamingStrategy;
import org.ingko.core.synthesizer.OrderedNaming;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.MethodResult;
import org.ingko.core.data.methods.Signature;
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

    public String generateMockito(EnvironmentObjectListener environmentObjectListener) {
        EnvironmentNode root = environmentObjectListener.getRoot();
        Class<?> clazz = root.getRuntimeClass();
        //String fileName = "Mock" + clazz.getSimpleName() + "Creator";
        String fileName = "Mock" + "Creator";
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

    private void generateObject(EnvironmentNode objectEnvironmentNode, MethodSpec.Builder methodBuilder) {
        if (objectEnvironmentNode.getRuntimeClass().isRecord()) {
            String mockName = namingStrategy.getUniqueMockName(objectEnvironmentNode);
            Class<?> clazz = objectEnvironmentNode.getRuntimeClass();

            List<String> childrenNames = new ArrayList<>();
            for (EnvironmentNode child : objectEnvironmentNode.getDirectChildren()) {
                generateObject(child, methodBuilder);
                childrenNames.add(namingStrategy.getUniqueMockName(child));
            }
            String params = childrenNames.stream().collect(Collectors.joining(", "));
            methodBuilder.addStatement("$T $L = new $T($L)", clazz, mockName, clazz, params);
            return;
        }
        if (objectEnvironmentNode.getRuntimeClass().isArray()) {
            String mockName = namingStrategy.getUniqueMockName(objectEnvironmentNode);
            Class<?> clazz = objectEnvironmentNode.getRuntimeClass();

            List<String> childrenNames = new ArrayList<>();
            for (EnvironmentNode child : objectEnvironmentNode.getDirectChildren()) {
                generateObject(child, methodBuilder);
                childrenNames.add(namingStrategy.getUniqueMockName(child));
            }
            String params = childrenNames.stream().collect(Collectors.joining(", "));
            methodBuilder.addStatement("$T $L = {$L}", clazz, mockName, params);
            return;
        }
        if (objectEnvironmentNode.isSerialized()) {
            Class<?> clazz = objectEnvironmentNode.getRuntimeClass();
            String mockName = namingStrategy.getUniqueMockName(objectEnvironmentNode);
            methodBuilder
                    .addStatement("$T $L = ($T) defaultSerde.deserialize($S)", clazz, mockName, clazz, objectEnvironmentNode.getValue());
            return;
        }
        if (objectEnvironmentNode.isTerminal()) {
            String mockName = namingStrategy.getUniqueMockName(objectEnvironmentNode);
            Class<?> clazz = objectEnvironmentNode.getRuntimeClass();
            String comments = objectEnvironmentNode.getComments();
            if (!comments.isEmpty()) {
                String escaped = comments.replace("$", "$$");
                methodBuilder.addComment(escaped);
            }
            methodBuilder.addStatement("$T $L = $L", clazz, mockName, objectEnvironmentNode.getValue());
            return;
        }
        Map<Signature, List<String>> methodActions = new HashMap<>();
        for (EnvironmentMethodCall environmentMethodCall : objectEnvironmentNode.getMethodCalls()) {
            Signature signature = environmentMethodCall.getSignature();
            if (!methodActions.containsKey(signature)) {
                methodActions.put(signature, new ArrayList<>());
            }
            if (environmentMethodCall.isVoid()) {
                if (environmentMethodCall.getResult() == MethodResult.RETURN) {
                    methodActions.get(environmentMethodCall.getSignature()).add("doNothing().");
                } else if (environmentMethodCall.getResult() == MethodResult.THROW) {
                    EnvironmentNode throwable = environmentMethodCall.getDest();
                    generateObject(throwable, methodBuilder);
                    String action = String.format("doThrow(%s).", namingStrategy.getUniqueMockName(throwable));
                    methodActions.get(environmentMethodCall.getSignature()).add(action);
                }
            } else {
                if (environmentMethodCall.getResult() == MethodResult.RETURN) {
                    if (environmentMethodCall.getDest().isTerminal()) {
                        if (environmentMethodCall.getDest().getRuntimeClass().equals(String.class)) {
                            String action = String.format("doReturn(\"%s\").", environmentMethodCall.getDest().getValue());
                            methodActions.get(signature).add(action);
                        } else {
                            String action = String.format("doReturn(%s).", environmentMethodCall.getDest().getValue());
                            methodActions.get(signature).add(action);
                        }
                    } else {
                        EnvironmentNode returnVal = environmentMethodCall.getDest();
                        generateObject(returnVal, methodBuilder);
                        String action = String.format("doReturn(%s).", namingStrategy.getUniqueMockName(returnVal));
                        methodActions.get(signature).add(action);
                    }
                } else if (environmentMethodCall.getResult() == MethodResult.THROW) {
                    EnvironmentNode throwable = environmentMethodCall.getDest();
                    generateObject(throwable, methodBuilder);
                    String action = String.format("doThrow(%s).", namingStrategy.getUniqueMockName(throwable));
                    methodActions.get(environmentMethodCall.getSignature()).add(action);
                }
            }
        }
        Class<?> clazz = objectEnvironmentNode.getRuntimeClass();
        String mockName = namingStrategy.getUniqueMockName(objectEnvironmentNode);
        methodBuilder.addStatement("$T $L = $T.mock($T.class)", clazz, mockName, Mockito.class, clazz);

        for (Signature key : methodActions.keySet()) {
            //System.out.println(key);
            CodeBlock.Builder statement = CodeBlock.builder();
            List<String> actions = methodActions.get(key);
            for (String action : actions) {
                statement.add(action);
            }
            statement.add("when($L).", namingStrategy.getUniqueMockName(objectEnvironmentNode));

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
