package org.ingko.core.synthesizer.mockito.methods;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterizedTypeName;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.LocalSymbol;
import org.ingko.core.data.methods.UserMethodCall;
import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.data.objects.Member;
import org.ingko.core.listener.utils.ClassUtils;
import org.ingko.core.synthesizer.mockito.nodes.EnvironmentNodeSynthesizer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BasicAnswerSynthesizer implements EnvironmentAnswerSynthesizer {
    private final EnvironmentNodeSynthesizer environmentNodeSynthesizer;
    private int answerId;

    public BasicAnswerSynthesizer(EnvironmentNodeSynthesizer environmentNodeSynthesizer) {
        this.answerId = 0;
        this.environmentNodeSynthesizer = environmentNodeSynthesizer;
    }

    private String symbolNamer(LocalSymbol s) {
        return switch (s.getSource()) {
            case LOCAL_ENV -> "local";
            case PARAMETER -> "param";
            case RETURN_VALUE -> "return";
            default -> "";
        } + s.getIndex() + s.getAccessPath().stream().map(Member::getPath).collect(Collectors.joining());
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
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(answerName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ParameterizedTypeName.get(Answer.class,
                        ClassUtils.getWrapped(rootMethodCall.getSignature().getReturnClass())));
        methodBuilder.beginControlFlow("return ($T invocation) ->", InvocationOnMock.class);
        List<Type> paramTypes = rootMethodCall.getGenericParamTypes();
        for (int i = 0; i < paramTypes.size(); i++) {
            String paramName = symbolNamer(new LocalSymbol(LocalSymbol.Source.PARAMETER, i));
            Type rawType = paramTypes.get(i);
            TypeName type = ParameterizedTypeName.get(rawType);
            //TypeName type = ParameterizedTypeName.get(rawType, rawType.getTypeParameters());
            methodBuilder.addStatement("$T $L = invocation.getArgument($L)", type, paramName, String.valueOf(i));
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
        if (!rootMethodCall.isVoid()) {

            LocalSymbol symbol = rootMethodCall.getReturnSymbol();
            EnvironmentNode returnNode = rootMethodCall.getDest();
            if (symbol.getSource() == LocalSymbol.Source.LOCAL_ENV && !returnNode.isTerminal()) {
                String method = environmentNodeSynthesizer.generateEnvironmentNode(typeBuilder, rootMethodCall.getDest()).methodName();
                methodBuilder.addStatement("return $L()", method);
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

        Type returnType = userMethodCall.getReturnType();

        String source = symbolNamer(userMethodCall.getSource());
        List<EnvironmentNode> locals = userMethodCall.getLocalParameters();
        // Generate parameter string
        List<String> params = new ArrayList<>();
        for (int i = 0; i < locals.size(); i++) {
            LocalSymbol symbol = userMethodCall.getParameters().get(i);
            if (symbol.getSource() == LocalSymbol.Source.LOCAL_ENV && locals.get(symbol.getIndex()).isTerminal()) {
                params.add(locals.get(symbol.getIndex()).getValue());
            } else if (symbol.getSource() == LocalSymbol.Source.LOCAL_ENV) {
                String method = environmentNodeSynthesizer.generateEnvironmentNode(typeBuilder, locals.get(i)).methodName();
                params.add(method + "()");
            } else {
                params.add(symbolNamer(symbol));
            }
        }
        String paramString = String.join(", ", params);
        String returnName = symbolNamer(returnSymbol);
        if (returnType.equals(void.class) || returnType.equals(Void.class) ||!explored.contains(returnSymbol)) {
            methodBuilder.addStatement("$L.$L($L)", source, userMethodCall.getMethodName(), paramString);
        } else {
            methodBuilder.addStatement("$T $L = $L.$L($L)",
                    returnType,
                    returnName,
                    source,
                    userMethodCall.getMethodName(),
                    paramString);
        }
    }

}
