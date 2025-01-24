package org.ingko.core.synthesizer.mockito.nodes;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.synthesizer.mockito.javafile.ParameterModSynthesizer;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.ingko.core.synthesizer.mockito.CodeUtils.declareMock;
import static org.ingko.core.synthesizer.mockito.CodeUtils.generateDo;
import static org.ingko.core.synthesizer.mockito.CodeUtils.groupMethods;

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

        List<ParameterModSynthesizer.MethodGroup> methodGroups = groupMethods(root.getMethodCalls());
        for (ParameterModSynthesizer.MethodGroup methodGroup : methodGroups) {
            List<String> answerList = new ArrayList<>();
            for (EnvironmentMethodCall methodCall : methodGroup.methodCalls()) {
                String returnName = generateEnvironmentNode(typeBuilder, methodCall.getDest()).methodName();
                String doAnswer = String.format("doReturn(%s)", returnName);
                answerList.add(doAnswer);
            }
            generateDo(methodBuilder, answerList, methodGroup.signature());
        }
        if (root.isTerminal()) {
            methodBuilder.addStatement("return " + root.getValue());
        } else {
            methodBuilder.addStatement("return mockObject");
        }
        typeBuilder.addMethod(methodBuilder.build());
        environmentId++;
        return new SynthResult(methodName);

    }
}
