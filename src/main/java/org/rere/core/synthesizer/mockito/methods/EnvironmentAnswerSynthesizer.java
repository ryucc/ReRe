package org.rere.core.synthesizer.mockito.methods;

import com.palantir.javapoet.TypeSpec;
import org.rere.core.data.methods.EnvironmentMethodCall;

public interface EnvironmentAnswerSynthesizer {
    SynthResult generateAnswer(TypeSpec.Builder typeBuilder, EnvironmentMethodCall root);

    record SynthResult(String methodName) {
    }
}
