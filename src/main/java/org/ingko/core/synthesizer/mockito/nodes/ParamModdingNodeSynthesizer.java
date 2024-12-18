package org.ingko.core.synthesizer.mockito.nodes;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.synthesizer.mockito.javafile.ParameterModSynthesizer;
import org.ingko.core.synthesizer.mockito.methods.BasicAnswerSynthesizer;
import org.ingko.core.synthesizer.mockito.methods.EnvironmentAnswerSynthesizer;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.ingko.core.synthesizer.mockito.CodeUtils.declareMock;
import static org.ingko.core.synthesizer.mockito.CodeUtils.generateDo;
import static org.ingko.core.synthesizer.mockito.CodeUtils.groupMethods;

//TODO topological sort again.
public class ParamModdingNodeSynthesizer implements EnvironmentNodeSynthesizer {

    private final EnvironmentAnswerSynthesizer answerSynthesizer;
    private int environmentId;


    public ParamModdingNodeSynthesizer() {
        this.answerSynthesizer = new BasicAnswerSynthesizer(this);
        this.environmentId = 0;
    }


    @Override
    public SynthResult generateEnvironmentNode(TypeSpec.Builder typeBuilder, EnvironmentNode root) {

        String methodName = "environmentNode" + environmentId;
        environmentId++;

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(root.getRuntimeClass());

        declareMock(root.getRuntimeClass(), "mockObject", methodBuilder);

        List<ParameterModSynthesizer.MethodGroup> methodGroups = groupMethods(root.getMethodCalls());
        for (ParameterModSynthesizer.MethodGroup methodGroup : methodGroups) {
            List<String> answerList = new ArrayList<>();
            for (EnvironmentMethodCall methodCall : methodGroup.methodCalls()) {
                String answerName = answerSynthesizer.generateAnswer(typeBuilder, methodCall).methodName();
                String doAnswer = String.format("doAnswer(%s())", answerName);
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

        return new SynthResult(methodName);
    }
}
