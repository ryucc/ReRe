/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.synthesizer.mockito;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.methods.Signature;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.serde.DefaultSerde;
import org.rere.core.synthesizer.mockito.nodes.EnvironmentNodeSynthesizer;
import org.rere.core.synthesizer.mockito.nodes.ParamModdingNodeSynthesizer;

import javax.lang.model.element.Modifier;
import java.util.List;

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


    public String generateMockito(EnvironmentNode root) {

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(fileName).addModifiers(Modifier.PUBLIC);

        environmentNodeSynthesizer.generateEnvironmentNode(typeBuilder, root);

        FieldSpec defaultSerde = FieldSpec.builder(DefaultSerde.class, "defaultSerde")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("new $T()", DefaultSerde.class)
                .build();
        typeBuilder.addField(defaultSerde);


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
        private final Signature signature;
        private final List<EnvironmentMethodCall> methodCalls;

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
    }
}
