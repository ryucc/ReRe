package org.katie.orange.core.synthesizer;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.katie.orange.core.data.methods.InternalMethodCall;
import org.katie.orange.core.data.methods.MethodResult;
import org.katie.orange.core.data.methods.PrimitiveMethodCall;
import org.katie.orange.core.data.methods.Signature;
import org.katie.orange.core.data.objects.InternalNode;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DFS {
    public void generateMockito(InternalNode root) {
        try {
            Class<?> clazz = Class.forName(root.getDisplayName());
            String fileName = "Mock" + clazz.getSimpleName() + "Creator";
            TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(fileName).addModifiers(Modifier.PUBLIC);


            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("create").addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(clazz);
            generateObject(root, methodBuilder);
            methodBuilder.addStatement("return $L", root.getUniqueMockName());
            typeBuilder.addMethod(methodBuilder.build());

            JavaFile javaFile = JavaFile.builder("com.example.helloworld", typeBuilder.build())
                    .addStaticImport(ArgumentMatchers.class, "*")
                    .addStaticImport(Mockito.class, "doReturn")
                    .build();
            javaFile.writeTo(System.out);
        } catch (Exception e) {
        }
    }

    private void generateObject(InternalNode objectNode, MethodSpec.Builder methodBuilder) {
        try {
            Class<?> clazz = Class.forName(objectNode.getDisplayName());

            String mockName = objectNode.getUniqueMockName();

            Map<Signature, List<String>> childObjectNames = new HashMap<>();

            for (InternalMethodCall methodCall : objectNode.getInternalMethodCalls()) {
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

            generatePrimitiveCalls(objectNode, methodBuilder);

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
    private void generatePrimitiveCalls(InternalNode objectNode, MethodSpec.Builder methodBuilder) {

        Map<Signature, List<PrimitiveMethodCall>> methodsBySignature = new HashMap<>();
        for (PrimitiveMethodCall methodCall : objectNode.getPrimitiveMethodCalls()) {
            Signature signature = methodCall.getSignature();
            if (!methodsBySignature.containsKey(signature)) {
                methodsBySignature.put(signature, new ArrayList<>());
            }
            methodsBySignature.get(signature).add(methodCall);
        }
        for (Signature key : methodsBySignature.keySet()) {
            CodeBlock.Builder statement = CodeBlock.builder();
            List<PrimitiveMethodCall> methodCalls = methodsBySignature.get(key);
            for (PrimitiveMethodCall methodCall : methodCalls) {
                if(methodCall.getResult() == MethodResult.RETURN) {
                    statement.add("doReturn($L).", methodCall.getDest().getValue());
                } else {
                    statement.add("doThrow($L).", methodCall.getDest().getValue());
                }
            }
            statement.add("when($L).", objectNode.getUniqueMockName());

            statement.add("$L($L)", key.getMethodName(), generateParamString(key.getParamTypes()));
            methodBuilder.addStatement(statement.build());
        }
    }

    private String generateParamString(List<String> paramTypes) {

        return String.join(", ", Collections.nCopies(paramTypes.size(), "any()"));
    }
}
