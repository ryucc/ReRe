package org.katie.orange.core.synthesizer;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.katie.orange.core.Listener;
import org.katie.orange.core.data.methods.MethodCall;
import org.katie.orange.core.data.methods.MethodResult;
import org.katie.orange.core.data.methods.Signature;
import org.katie.orange.core.data.objects.Node;
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
        try {
            Class<?> clazz = root.getClazz();
            String fileName = "Mock" + clazz.getSimpleName() + "Creator";
            TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(fileName).addModifiers(Modifier.PUBLIC);


            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(this.methodName).addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(clazz);
            generateObject(root, methodBuilder);
            methodBuilder.addStatement("return $L", root.getUniqueMockName());
            typeBuilder.addMethod(methodBuilder.build());

            JavaFile javaFile = JavaFile.builder(this.packageName, typeBuilder.build()).addStaticImport(ArgumentMatchers.class, "*").addStaticImport(Mockito.class, "doReturn").build();
            return javaFile.toString();
        } catch (Exception e) {
        }
        return "";
    }

    private void generateObject(Node objectNode, MethodSpec.Builder methodBuilder) {
        if (objectNode.isTerminal()) {
            Class<?> clazz = objectNode.getClazz();
            String mockName = objectNode.getUniqueMockName();
            methodBuilder.addStatement("$T $L = $L", clazz, mockName, objectNode.getValue());
            return;
        }
        try {
            Class<?> clazz = objectNode.getClazz();

            String mockName = objectNode.getUniqueMockName();

            Map<Signature, List<String>> childObjectNames = new HashMap<>();

            for (MethodCall methodCall : objectNode.getMethodCalls()) {
                Signature signature = methodCall.getSignature();
                if (methodCall.getResult() == MethodResult.RETURN) {
                    generateObject(methodCall.getDest(), methodBuilder);
                    if (!childObjectNames.containsKey(signature)) {
                        childObjectNames.put(signature, new ArrayList<>());
                    }
                    childObjectNames.get(signature).add(methodCall.getDest().getUniqueMockName());
                }
            }
            methodBuilder.addStatement("$T $L = $T.mock($T.class)", clazz, mockName, Mockito.class, clazz);

            for (Signature key : childObjectNames.keySet()) {
                System.out.println(key);
                CodeBlock.Builder statement = CodeBlock.builder();
                List<String> returnNames = childObjectNames.get(key);
                for (String name : returnNames) {
                    statement.add("doReturn($L).", name);
                }
                statement.add("when($L).", objectNode.getUniqueMockName());

                statement.add("$L($L)", key.getMethodName(), generateParamString(key.getParamTypes()));
                methodBuilder.addStatement(statement.build());
            }
        } catch (Exception e) {
        }
    }

    private String generateParamString(List<String> paramTypes) {

        return String.join(", ", Collections.nCopies(paramTypes.size(), "any()"));
    }
}
