/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.synthesizer.mockito;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.methods.Signature;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.serde.DefaultSerde;
import org.rere.core.synthesizer.mockito.nodes.EnvironmentNodeSynthesizer;
import org.rere.core.synthesizer.mockito.nodes.ParamModdingNodeSynthesizer;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Expect not to be thread safe.
 * It's a single document... not considering the need to parallel generate right now.
 */
public class MockitoSynthesizer {

    private final String packageName;
    private final String methodName;
    private final String fileName;

    private final EnvironmentNodeSynthesizer environmentNodeSynthesizer;


    public MockitoSynthesizer(String packageName, String methodName, String fileName) {
        this.packageName = packageName;
        this.methodName = methodName;
        this.fileName = fileName;
        this.environmentNodeSynthesizer = new ParamModdingNodeSynthesizer();
    }
    public MockitoSynthesizer(String packageName, String methodName) {
        this(packageName, methodName, "MockCreator");
    }

    public static void declareMock(Class<?> clazz, String name, MethodSpec.Builder methodBuilder) {
        methodBuilder.addStatement("$T $L = mock($T.class)", clazz, name, clazz);
    }


    public List<MethodGroup> groupMethods(List<EnvironmentMethodCall> environmentMethodCalls) {
        Map<Signature, List<EnvironmentMethodCall>> m = environmentMethodCalls.stream()
                .collect(Collectors.groupingBy(EnvironmentMethodCall::getSignature));
        return m.entrySet().stream().map(e -> new MethodGroup(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }





    public String generateMockito(EnvironmentNode root) {

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(fileName).addModifiers(Modifier.PUBLIC);

        environmentNodeSynthesizer.generateEnvironmentNode(typeBuilder, root);

        FieldSpec defaultSerde = FieldSpec.builder(DefaultSerde.class, "defaultSerde")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $T()", DefaultSerde.class)
                .build();
        typeBuilder.addField(defaultSerde);
        /*
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("testMethodName")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(root.getRuntimeClass());
        generateMethod(typeBuilder, root.getMethodCalls().get(0));
        methodBuilder.addStatement("return $L", "returnValue");
        typeBuilder.addMethod(methodBuilder.build());
         */


        JavaFile javaFile = JavaFile.builder(packageName, typeBuilder.build())
                .addStaticImport(ArgumentMatchers.class, "*")
                .addStaticImport(Mockito.class, "doReturn")
                .addStaticImport(Mockito.class, "doAnswer")
                .addStaticImport(Mockito.class, "doNothing")
                .addStaticImport(Mockito.class, "mock")
                .build();
        return javaFile.toString();
    }

    public static class MethodGroup {
        public MethodGroup(Signature signature, List<EnvironmentMethodCall> methodCalls) {
            this.signature = signature;
            this.methodCalls = methodCalls;
        }

        public Signature signature() {
            return signature;
        }

        public List<EnvironmentMethodCall> methodCalls() {
            return methodCalls;
        }

        private final Signature signature;
        private final List<EnvironmentMethodCall> methodCalls;
    }
}
