package org.ingko.core.synthesizer.mockito.methods;

import com.palantir.javapoet.TypeSpec;
import org.ingko.core.data.methods.EnvironmentMethodCall;

public interface EnvironmentAnswerSynthesizer {
    SynthResult generateAnswer(TypeSpec.Builder typeBuilder, EnvironmentMethodCall root);

    record SynthResult(String methodName) {
    }
}
