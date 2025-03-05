/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.synthesizer.mockito.nodes;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.synthesizer.mockito.MockitoSynthesizer;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.rere.core.synthesizer.mockito.CodeUtils.declareMock;
import static org.rere.core.synthesizer.mockito.CodeUtils.generateWhen;
import static org.rere.core.synthesizer.mockito.CodeUtils.groupMethods;

public class ReturnOnlyNodeSynthesizer implements EnvironmentNodeSynthesizer {


    private int environmentId;

    public ReturnOnlyNodeSynthesizer() {
        this.environmentId = 0;
    }

    @Override
    public SynthResult generateEnvironmentNode(TypeSpec.Builder typeBuilder, EnvironmentNode root) {

        String methodName = "environmentNode" + environmentId;

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(root.getRuntimeClass());

        // TODO: don't declare if primitive
        declareMock(root.getRuntimeClass(), "mockObject", methodBuilder);

        List<MockitoSynthesizer.MethodGroup> methodGroups = groupMethods(root.getMethodCalls());
        for (MockitoSynthesizer.MethodGroup methodGroup : methodGroups) {
            List<String> answerList = new ArrayList<>();
            for (EnvironmentMethodCall methodCall : methodGroup.methodCalls()) {
                String returnName = generateEnvironmentNode(typeBuilder, methodCall.getDest()).methodName();
                String doAnswer = String.format("doReturn(%s)", returnName);
                answerList.add(doAnswer);
            }
            generateWhen(methodBuilder, answerList, methodGroup.signature());
        }
        if (root.isTerminal()) {
            methodBuilder.addStatement("return " + root.getValue());
        } else {
            methodBuilder.addStatement("return mockObject");
        }
        typeBuilder.addMethod(methodBuilder.build());
        environmentId++;
        return new SynthResult(methodName, root.getRuntimeClass());

    }
}
