package org.ingko.core.synthesizer;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.LocalSymbol;
import org.ingko.core.data.methods.UserMethodCall;
import org.ingko.core.data.objects.EnvironmentNode;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ParameterModSynthesizer {


    private static String symbolNamer(LocalSymbol s) {
        return switch (s.getSource()) {
            case LOCAL_ENV -> "local";
            case PARAMETER -> "param";
            case RETURN_VALUE -> "return";
            default -> "";
        } + s.getIndex();
    }

    public static void generateUserMethodCall(TypeSpec.Builder typeBuilder,
                                              MethodSpec.Builder methodBuilder,
                                              UserMethodCall userMethodCall,
                                              LocalSymbol returnSymbol) {

        Class<?> returnType = userMethodCall.getReturnType();

        if (returnType.equals(void.class) || returnType.equals(Void.class)) {
            methodBuilder.addStatement(userMethodCall.getMethodName());
        } else {
            String source = symbolNamer(userMethodCall.getSource());
            String returnName = symbolNamer(returnSymbol);
            List<EnvironmentNode> locals = userMethodCall.getLocalParameters();
            // Generate parameter string
            List<String> params = new ArrayList<>();
            for (int i = 0; i < locals.size(); i++) {
                LocalSymbol symbol = userMethodCall.getParameters().get(i);
                if (symbol.getSource() == LocalSymbol.Source.LOCAL_ENV && locals.get(symbol.getIndex()).isTerminal()) {
                    params.add(locals.get(symbol.getIndex()).getValue());
                } else {
                    params.add(symbolNamer(symbol));
                }
            }
            String paramString = String.join(", ", params);
            methodBuilder.addStatement("$T $L = $L.$L($L)",
                    returnType,
                    returnName,
                    source,
                    userMethodCall.getMethodName(),
                    paramString);
        }
    }

    public static void generateMethod(TypeSpec.Builder typeBuilder, EnvironmentMethodCall rootMethodCall) {
        TypeVariableName typeVariableName = TypeVariableName.get("?");
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("testMethodName")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(ParameterizedTypeName.get(Answer.class, Object.class));
        methodBuilder.beginControlFlow("return ($T invocation) -> ", InvocationOnMock.class);
        List<Class<?>> paramTypes = rootMethodCall.getParamTypes();
        for (int i = 0; i < paramTypes.size(); i++) {
            String paramName = symbolNamer(new LocalSymbol(LocalSymbol.Source.PARAMETER, i));
            Class<?> type = paramTypes.get(i);
            methodBuilder.addStatement("$T $L = invocation.getArgument($L)", type, paramName, String.valueOf(i));
        }

        List<UserMethodCall> userMethodCalls = rootMethodCall.getUserMethodCalls();
        for (int i = 0; i < userMethodCalls.size(); i++) {
            generateUserMethodCall(typeBuilder,
                    methodBuilder,
                    userMethodCalls.get(i),
                    new LocalSymbol(LocalSymbol.Source.RETURN_VALUE, i));
        }
        methodBuilder.addStatement("return $L", symbolNamer(rootMethodCall.getReturnSymbol()));
        methodBuilder.endControlFlow();
        typeBuilder.addMethod(methodBuilder.build());
    }

    public static String generateMockito(EnvironmentNode root) {

        String fileName = "Mock" + "Creator";
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(fileName).addModifiers(Modifier.PUBLIC);

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("testMethodName")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(root.getRuntimeClass());


        generateMethod(typeBuilder, root.getMethodCalls().get(0));
        methodBuilder.addStatement("return $L", "returnValue");
        typeBuilder.addMethod(methodBuilder.build());


        JavaFile javaFile = JavaFile.builder("test.package", typeBuilder.build())
                .addStaticImport(ArgumentMatchers.class, "*")
                .addStaticImport(Mockito.class, "doReturn")
                .build();
        System.out.println(javaFile);
        return javaFile.toString();
    }
}
