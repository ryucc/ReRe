/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.synthesizer.mockito.nodes;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.methods.MethodResult;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.data.objects.LocalSymbol;
import org.rere.core.listener.utils.ClassUtils;
import org.rere.core.serde.PrimitiveSerde;
import org.rere.core.synthesizer.mockito.CodeUtils;
import org.rere.core.synthesizer.mockito.MockitoSynthesizer;
import org.rere.core.synthesizer.mockito.methods.BasicAnswerSynthesizer;
import org.rere.core.synthesizer.mockito.methods.EnvironmentAnswerSynthesizer;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import static org.rere.core.synthesizer.mockito.CodeUtils.declareMock;
import static org.rere.core.synthesizer.mockito.CodeUtils.generateDo;
import static org.rere.core.synthesizer.mockito.CodeUtils.getBestType;
import static org.rere.core.synthesizer.mockito.CodeUtils.groupMethods;

//TODO topological sort again.
public class ParamModdingNodeSynthesizer implements EnvironmentNodeSynthesizer {

    private final EnvironmentAnswerSynthesizer answerSynthesizer;
    private final String packageName;
    private int environmentId;


    public ParamModdingNodeSynthesizer(String packageName) {
        this.answerSynthesizer = new BasicAnswerSynthesizer(packageName, this);
        this.environmentId = 0;
        this.packageName = packageName;
    }


    public SynthResult generateRecordEnvironmentNode(TypeSpec.Builder typeBuilder, EnvironmentNode root) {

        String methodName = "environmentNode" + environmentId;
        environmentId++;

        Type returnType = getBestType(packageName, root);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addException(Exception.class)
                .returns(returnType);

        Queue<EnvironmentNode> front = new ArrayDeque<>();
        Set<EnvironmentNode> explored = new HashSet<>();
        HashMap<EnvironmentNode, Integer> childCount = new HashMap<>();
        HashMap<EnvironmentNode, List<EnvironmentNode>> parents = new HashMap<>();
        HashMap<EnvironmentNode, String> variableNames = new HashMap<>();

        front.add(root);

        Queue<EnvironmentNode> readyQueue = new ArrayDeque<>();

        int nameIndex = 0;
        while (!front.isEmpty()) {
            EnvironmentNode cur = front.poll();
            if (explored.contains(cur)) continue;

            explored.add(cur);
            String objectName = "object" + nameIndex;
            nameIndex++;
            variableNames.put(cur, objectName);
            front.addAll(cur.getDirectChildren());
            methodBuilder.addStatement("$T $L", getBestType(packageName, cur), objectName);
            if (cur.getRuntimeClass().isArray() || cur.getDirectChildren().isEmpty()) {
                readyQueue.add(cur);
            } else {
                for (EnvironmentNode child : cur.getDirectChildren()) {
                    parents.computeIfAbsent(child, (x) -> new ArrayList<>()).add(cur);
                }
                childCount.put(cur, cur.getDirectChildren().size());
            }
        }
        List<EnvironmentNode> arrays = new ArrayList<>();
        while (!readyQueue.isEmpty()) {
            EnvironmentNode cur = readyQueue.poll();
            if (ClassUtils.isRecord(cur.getRuntimeClass())) {
                String children = cur.getDirectChildren()
                        .stream()
                        .map(c -> variableNames.get(c))
                        .collect(Collectors.joining(","));
                methodBuilder.addStatement("$L = new $T($L)", variableNames.get(cur), cur.getRuntimeClass(), children);

            } else if (cur.getRuntimeClass().equals(Optional.class)) {
                if(cur.getDirectChildren().isEmpty()) {
                    methodBuilder.addStatement("$L = $T.empty()", variableNames.get(cur), Optional.class);
                } else {
                    methodBuilder.addStatement("$L = $T.of($L)", variableNames.get(cur),
                            Optional.class,
                            variableNames.get(cur.getDirectChildren().get(0)));
                }
            } else if (cur.getRuntimeClass().isArray()) {
                methodBuilder.addStatement("$L = new $T[$L]",
                        variableNames.get(cur),
                        cur.getRuntimeClass().getComponentType(),
                        cur.getDirectChildren().size());
                arrays.add(cur);
            } else {
                methodBuilder.addStatement("$L = $L",
                        variableNames.get(cur),
                        generateLeafEnvironmentNode(typeBuilder, cur).methodName());
            }
            for (EnvironmentNode parent : parents.computeIfAbsent(cur, c -> new ArrayList<>())) {
                childCount.compute(parent, (p, x) -> x - 1);
                if (childCount.get(parent) == 0) {
                    readyQueue.add(parent);
                }
            }
        }
        for (EnvironmentNode arrayNode : arrays) {
            for (int i = 0; i < arrayNode.getDirectChildren().size(); i++) {
                methodBuilder.addStatement("$L[$L] = $L",
                        variableNames.get(arrayNode),
                        i,
                        variableNames.get(arrayNode.getDirectChildren().get(i)));
            }
        }
        methodBuilder.addStatement("return $L", variableNames.get(root));
        typeBuilder.addMethod(methodBuilder.build());
        return new SynthResult(methodName + "()", returnType);

    }

    @Override
    public SynthResult generateEnvironmentNode(TypeSpec.Builder typeBuilder, EnvironmentNode root) {
        if (ClassUtils.isWrapperOrPrimitive(root.getRuntimeClass())) {
            String cast = String.format("(%s) ", root.getRuntimeClass().getName());
            return new SynthResult(cast + root.getValue(), root.getRuntimeClass());
        }
        if (root.getRuntimeClass().equals(Optional.class)) {
            return generateRecordEnvironmentNode(typeBuilder, root);
        } else if (ClassUtils.isRecord(root.getRuntimeClass()) || root.getRuntimeClass().isArray()) {
            return generateRecordEnvironmentNode(typeBuilder, root);
        }
        return generateLeafEnvironmentNode(typeBuilder, root);
    }

    public void generateRootMethod(TypeSpec.Builder typeBuilder, EnvironmentNode root, String methodName) {

        Type declaringClass = getBestType(packageName, root);
        MethodSpec.Builder rootMethodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addException(Exception.class)
                .returns(declaringClass);
        EnvironmentNodeSynthesizer.SynthResult result = generateEnvironmentNode(typeBuilder, root);

        rootMethodBuilder.beginControlFlow("try")
                .addStatement("return $L", result.methodName())
                .endControlFlow()
                .beginControlFlow("catch (Exception e)")
                .addStatement("throw new $T(e)", RuntimeException.class)
                .endControlFlow();
        typeBuilder.addMethod(rootMethodBuilder.build());
    }


    public void addComments(MethodSpec.Builder methodBuilder, String comments) {
        if (comments.contains("\n")) {
            methodBuilder.addCode("/*\n");
            for (String s : comments.split("\n")) {
                methodBuilder.addCode(" * $L\n", s);
            }
            methodBuilder.addCode("*/\n");
        } else {
            methodBuilder.addComment("$L", comments);
        }
    }

    public SynthResult generateLeafEnvironmentNode(TypeSpec.Builder typeBuilder, EnvironmentNode root) {

        String methodName = "environmentNode" + environmentId;
        environmentId++;

        Type declaringClass = CodeUtils.getBestType(packageName, root);


        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addException(Exception.class)
                .returns(declaringClass);

        if (root.getComments() != null && !root.getComments().isEmpty()) {
            addComments(methodBuilder, root.getComments());
        }
        if (root.isSerialized()) {
            Class<?> clazz = root.getRuntimeClass();
            Class<?> serdeClass = root.getSerializer();
            if (serdeClass.equals(PrimitiveSerde.class)) {
                methodBuilder.addStatement("return ($T) defaultSerde.deserialize($S)", clazz, root.getValue());
            } else {
                methodBuilder.addStatement("return ($T) new $T().deserialize($S)", clazz, serdeClass, root.getValue());
            }
            typeBuilder.addMethod(methodBuilder.build());
            return new SynthResult(methodName + "()", clazz);
        }

        declareMock(declaringClass, "mockObject", methodBuilder);

        List<MockitoSynthesizer.MethodGroup> methodGroups = groupMethods(root.getMethodCalls());
        for (MockitoSynthesizer.MethodGroup methodGroup : methodGroups) {
            List<String> answerList = new ArrayList<>();
            // TODO better grouping
            for (EnvironmentMethodCall methodCall : methodGroup.methodCalls()) {
                // TODO clean up
                if (methodCall.getResult().equals(MethodResult.THROW)) {
                    String returnName = generateEnvironmentNode(typeBuilder, methodCall.getDest()).methodName();
                    String doThrow = String.format("doThrow(%s)", returnName);
                    answerList.add(doThrow);
                } else if (methodCall.getReturnSymbol()
                        .getSource() == LocalSymbol.Source.LOCAL_ENV && methodCall.getUserMethodCalls().isEmpty()) {
                    if (methodCall.isVoid()) {
                        answerList.add("doNothing()");
                    } else {
                        String returnName = generateEnvironmentNode(typeBuilder, methodCall.getDest()).methodName();
                        String doAnswer = String.format("doReturn(%s)", returnName);
                        answerList.add(doAnswer);
                    }
                } else {
                    String answerName = answerSynthesizer.generateAnswer(typeBuilder, methodCall).methodName();
                    String doAnswer = String.format("doAnswer(%s())", answerName);
                    answerList.add(doAnswer);
                }
            }
            generateDo(methodBuilder, answerList, methodGroup.signature());
        }
        if (root.isTerminal()) {
            methodBuilder.addStatement("return " + root.getValue());
        } else {
            methodBuilder.addStatement("return mockObject");
        }
        typeBuilder.addMethod(methodBuilder.build());

        return new SynthResult(methodName + "()", declaringClass);
    }
}
