/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.synthesizer.mockito.methods;

import com.squareup.javapoet.TypeSpec;
import org.rere.core.data.methods.EnvironmentMethodCall;

public interface EnvironmentAnswerSynthesizer {
    SynthResult generateAnswer(TypeSpec.Builder typeBuilder, EnvironmentMethodCall root);

    class SynthResult {

        final String methodName;

        public SynthResult(String methodName) {
            this.methodName = methodName;
        }

        public String methodName() {
            return methodName;
        }
    }
}
