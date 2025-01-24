package org.ingko.core.synthesizer.mockito.nodes;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeSpec;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.LocalSymbol;
import org.ingko.core.data.methods.MethodResult;
import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.utils.ClassUtils;
import org.ingko.core.synthesizer.mockito.MockitoSynthesizer;
import org.ingko.core.synthesizer.mockito.methods.BasicAnswerSynthesizer;
import org.ingko.core.synthesizer.mockito.methods.EnvironmentAnswerSynthesizer;

import javax.lang.model.element.Modifier;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

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


    public SynthResult generateRecordEnvironmentNode(TypeSpec.Builder typeBuilder, EnvironmentNode root) {

        String methodName = "environmentNode" + environmentId;
        environmentId++;

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(root.getRuntimeClass());

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
            methodBuilder.addStatement("$T $L", cur.getRuntimeClass(), objectName);
            if (!cur.getRuntimeClass().isRecord() || cur.getDirectChildren().isEmpty()) {
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
            if (cur.getRuntimeClass().isRecord()) {
                String children = cur.getDirectChildren()
                        .stream()
                        .map(c -> variableNames.get(c))
                        .collect(Collectors.joining(","));
                methodBuilder.addStatement("$L = new $T($L)", variableNames.get(cur), cur.getRuntimeClass(), children);

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
        return new SynthResult(methodName + "()");

    }

    @Override
    public SynthResult generateEnvironmentNode(TypeSpec.Builder typeBuilder, EnvironmentNode root) {
        if (ClassUtils.isStringOrPrimitive(root.getRuntimeClass())) {
            return new SynthResult(root.getValue());
        }
        if (root.getRuntimeClass().isRecord() || root.getRuntimeClass().isArray()) {
            return generateRecordEnvironmentNode(typeBuilder, root);
        }
        return generateLeafEnvironmentNode(typeBuilder, root);
    }

    public SynthResult generateLeafEnvironmentNode(TypeSpec.Builder typeBuilder, EnvironmentNode root) {

        String methodName = "environmentNode" + environmentId;
        environmentId++;

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(root.getRuntimeClass());

        if (root.isSerialized()) {
            Class<?> clazz = root.getRuntimeClass();
            methodBuilder.addStatement("return ($T) defaultSerde.deserialize($S)", clazz, root.getValue());
            typeBuilder.addMethod(methodBuilder.build());
            return new SynthResult(methodName + "()");
        }

        declareMock(root.getRuntimeClass(), "mockObject", methodBuilder);

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

        return new SynthResult(methodName + "()");
    }
}
