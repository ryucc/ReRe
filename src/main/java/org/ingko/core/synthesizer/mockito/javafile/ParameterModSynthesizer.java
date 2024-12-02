package org.ingko.core.synthesizer.mockito.javafile;

import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.Signature;
import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.synthesizer.NamingStrategy;
import org.ingko.core.synthesizer.OrderedNaming;
import org.ingko.core.synthesizer.mockito.nodes.EnvironmentNodeSynthesizer;
import org.ingko.core.synthesizer.mockito.nodes.ParamModdingNodeSynthesizer;
import org.ingko.core.synthesizer.mockito.nodes.ReturnOnlyNodeSynthesizer;
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
public class ParameterModSynthesizer {

    private final String packageName;
    private final String methodName;
    private final NamingStrategy namingStrategy;

    private final EnvironmentNodeSynthesizer environmentNodeSynthesizer;

    private int environmentId;
    private int answerId;

    public ParameterModSynthesizer(String packageName, String methodName) {
        this.packageName = packageName;
        this.methodName = methodName;
        namingStrategy = new OrderedNaming();
        environmentId = 0;
        this.environmentNodeSynthesizer = new ParamModdingNodeSynthesizer();
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

        String fileName = "Mock" + "Creator";
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(fileName).addModifiers(Modifier.PUBLIC);

        environmentNodeSynthesizer.generateEnvironmentNode(typeBuilder, root);

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
                .addStaticImport(Mockito.class, "mock")
                .build();
        System.out.println(javaFile);
        return javaFile.toString();
    }

    public record MethodGroup(Signature signature, List<EnvironmentMethodCall> methodCalls) {
    }
}
