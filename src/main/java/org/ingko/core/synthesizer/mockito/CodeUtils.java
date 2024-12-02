package org.ingko.core.synthesizer.mockito;

import com.palantir.javapoet.MethodSpec;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.Signature;
import org.ingko.core.synthesizer.mockito.javafile.ParameterModSynthesizer;

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

        String paramString = "any()";
        methodBuilder.addStatement("$L.when(mockObject).$L($L)", ans, signature.getMethodName(), paramString);
    }
}
