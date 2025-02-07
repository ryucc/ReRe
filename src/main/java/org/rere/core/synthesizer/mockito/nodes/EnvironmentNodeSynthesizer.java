/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.synthesizer.mockito.nodes;

import com.squareup.javapoet.TypeSpec;
import org.rere.core.data.objects.EnvironmentNode;

public interface EnvironmentNodeSynthesizer {
    SynthResult generateEnvironmentNode(TypeSpec.Builder typeBuilder, EnvironmentNode root);
    class SynthResult {
        public SynthResult(String methodName) {
            this.methodName = methodName;
        }

        public String methodName() {
            return methodName;
        }

        private final String methodName;
    }
}
