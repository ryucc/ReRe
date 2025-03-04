/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.synthesizer.mockito.methods;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.objects.LocalSymbol;
import org.rere.core.data.methods.UserMethodCall;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.data.objects.Member;
import org.rere.core.data.objects.UserNode;
import org.rere.core.listener.utils.ClassUtils;
import org.rere.core.serde.PrimitiveSerde;
import org.rere.core.serde.exceptions.SerializationException;
import org.rere.core.synthesizer.mockito.CodeUtils;
import org.rere.core.synthesizer.mockito.nodes.EnvironmentNodeSynthesizer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.lang.model.element.Modifier;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BasicAnswerSynthesizer implements EnvironmentAnswerSynthesizer {
    private final EnvironmentNodeSynthesizer environmentNodeSynthesizer;
    private int answerId;
    // TODO: fix local param to be answer-local.
    int localParamId = 0;

    private final String packageName;
    public BasicAnswerSynthesizer(String packageName, EnvironmentNodeSynthesizer environmentNodeSynthesizer) {
        this.answerId = 0;
        this.environmentNodeSynthesizer = environmentNodeSynthesizer;
        this.packageName = packageName;
    }

    private String symbolNamer(LocalSymbol s) {
        return s.toString() + s.getAccessPath().stream().map(Member::getPath).collect(Collectors.joining());
    }

    public Set<LocalSymbol> exploredUsedSymbols(EnvironmentMethodCall rootMethodCall, List<UserMethodCall> userMethodCalls) {
        Set<LocalSymbol> usedAsParam = userMethodCalls.stream()
                .flatMap(s -> s.getParameters().stream())
                .filter(localSymbol -> localSymbol.getSource().equals(LocalSymbol.Source.RETURN_VALUE))
                .collect(Collectors.toSet());
        Set<LocalSymbol> usedAsSource = userMethodCalls.stream()
                .map(UserMethodCall::getSource)
                .filter(localSymbol -> localSymbol.getSource().equals(LocalSymbol.Source.RETURN_VALUE))
                .collect(Collectors.toSet());
        Set<LocalSymbol> anyUse = new HashSet<>();
        anyUse.addAll(usedAsParam);
        anyUse.addAll(usedAsSource);
        if(rootMethodCall.getReturnSymbol().getSource().equals(LocalSymbol.Source.RETURN_VALUE)) {
            anyUse.add(rootMethodCall.getReturnSymbol());
        }
        return anyUse;
    }

    @Override
    public SynthResult generateAnswer(TypeSpec.Builder typeBuilder, EnvironmentMethodCall rootMethodCall) {
        String answerName = "getAnswer" + answerId;
        answerId++;
        /*

         */
        int arrayId = 0;
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(answerName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ParameterizedTypeName.get(Answer.class,
                        ClassUtils.getWrapped(rootMethodCall.getReturnClass())));
        methodBuilder.beginControlFlow("return ($T invocation) ->", InvocationOnMock.class);
        List<UserNode> paramNodes = rootMethodCall.getParameterNodes();
        for (int i = 0; i < paramNodes.size(); i++) {
            UserNode curNode = paramNodes.get(i);
            if(curNode.isFailedNode()) {
                methodBuilder.addComment("Failed node");
                CodeUtils.addComments(methodBuilder, curNode.getComments());
            }
            String paramName = symbolNamer(new LocalSymbol(LocalSymbol.Source.PARAMETER, i));
            Type rawType = CodeUtils.getVisibleBestType(packageName, paramNodes.get(i));
            if(rawType.equals(Object.class)) {
                System.out.println(paramNodes.get(i).getRuntimeClass());
                System.out.println(paramNodes.get(i).getRepresentingClass());
            }
            //TypeName type = ParameterizedTypeName.get(rawType, rawType.getTypeParameters());
            methodBuilder.addStatement("$T $L = invocation.getArgument($L)", rawType, paramName, String.valueOf(i));
        }

        Set<LocalSymbol> explored = exploredUsedSymbols(rootMethodCall, rootMethodCall.getUserMethodCalls());

        List<UserMethodCall> userMethodCalls = rootMethodCall.getUserMethodCalls();
        for (int i = 0; i < userMethodCalls.size(); i++) {
            generateUserMethodCall(typeBuilder,
                    methodBuilder,
                    userMethodCalls.get(i),
                    new LocalSymbol(LocalSymbol.Source.RETURN_VALUE, i),
                    explored);
        }
        for (Integer i: rootMethodCall.getEndResult().keySet()) {
            Object arr = rootMethodCall.getEndResult().get(i);
            try {
                String s = new PrimitiveSerde().serialize((Serializable) arr);
                String varName = "array" + arrayId;
                methodBuilder.addStatement("Object $L = new $T().deserialize(\"$L\")", varName,
                        PrimitiveSerde.class, s);
                methodBuilder.addStatement("$T.shallowCopyIntoArray($L, invocation.getArgument($L))", ClassUtils.class,
                        varName, i);
            } catch (SerializationException e) {
                continue;
            }
        }
        if (!rootMethodCall.isVoid()) {

            LocalSymbol symbol = rootMethodCall.getReturnSymbol();
            EnvironmentNode returnNode = rootMethodCall.getDest();
            if (symbol.getSource() == LocalSymbol.Source.LOCAL_ENV && !returnNode.isTerminal()) {
                String method = environmentNodeSynthesizer.generateEnvironmentNode(typeBuilder, rootMethodCall.getDest()).methodName();
                methodBuilder.addStatement("return $L", method);
            } else if (symbol.getSource() == LocalSymbol.Source.LOCAL_ENV && returnNode.isTerminal()) {
                methodBuilder.addStatement("return $L", returnNode.getValue());
            } else {
                methodBuilder.addStatement("return $L", symbolNamer(rootMethodCall.getReturnSymbol()));
            }
        } else {
            methodBuilder.addStatement("return null");
        }
        methodBuilder.endControlFlow("");
        typeBuilder.addMethod(methodBuilder.build());
        return new SynthResult(answerName);
    }

    public void generateUserMethodCall(TypeSpec.Builder typeBuilder,
                                       MethodSpec.Builder methodBuilder,
                                       UserMethodCall userMethodCall,
                                       LocalSymbol returnSymbol,
                                       Set<LocalSymbol> explored) {

        Class<?> returnType = userMethodCall.getReturnType();

        String source = symbolNamer(userMethodCall.getSource());
        List<LocalSymbol> paramSources = userMethodCall.getParameters();
        List<EnvironmentNode> locals = userMethodCall.getLocalParameters();
        // Generate parameter string
        List<String> paramNames = new ArrayList<>();
        for (int i = 0; i < paramSources.size(); i++) {
            LocalSymbol symbol = paramSources.get(i);
            if (symbol.getSource() == LocalSymbol.Source.LOCAL_ENV
                    && ClassUtils.isWrapperOrPrimitive(locals.get(symbol.getIndex()).getRuntimeClass())) {
                //Primitive values
                paramNames.add(locals.get(symbol.getIndex()).getValue());
            } else if (symbol.getSource() == LocalSymbol.Source.LOCAL_ENV) {
                // Create environment mocks
                EnvironmentNodeSynthesizer.SynthResult result =  environmentNodeSynthesizer.generateEnvironmentNode(typeBuilder, locals.get(i));
                String paramName = "envParam" + localParamId;
                localParamId++;
                methodBuilder.addStatement("$T $L = $L", result.getDeclaredClass(), paramName, result.methodName());
                paramNames.add(paramName);
            } else {
                // Return values or parameters
                paramNames.add(symbolNamer(symbol));
            }
        }
        String paramString = String.join(", ", paramNames);
        String returnName = symbolNamer(returnSymbol);
        // TODO find where null comes from
        if (returnType == null || returnType.equals(void.class) || returnType.equals(Void.class) ||!explored.contains(returnSymbol)) {
            UserNode returnNode = userMethodCall.getReturnNode();
            if(returnNode.isFailedNode()) {
                methodBuilder.addComment("return node failed");
            }
            if(!returnNode.getComments().isEmpty()) {
                CodeUtils.addComments(methodBuilder, returnNode.getComments());
            }
            methodBuilder.addStatement("$L.$L($L)", source, userMethodCall.getMethodName(), paramString);
        } else {
            UserNode returnNode = userMethodCall.getReturnNode();
            if(!returnNode.getComments().isEmpty()) {
                CodeUtils.addComments(methodBuilder, returnNode.getComments());
            }
            methodBuilder.addStatement("$T $L = ($T) $L.$L($L)",
                    returnType,
                    returnName,
                    returnType,
                    source,
                    userMethodCall.getMethodName(),
                    paramString);
        }
    }

}
