/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.synthesizer.mockito.nodes;

import com.squareup.javapoet.TypeSpec;
import org.rere.core.data.objects.EnvironmentNode;

import java.lang.reflect.Type;

public interface EnvironmentNodeSynthesizer {
    SynthResult generateEnvironmentNode(TypeSpec.Builder typeBuilder, EnvironmentNode root);

    class SynthResult {
        private final String methodName;
        private final Type declaredClass;

        public SynthResult(String methodName, Type declaredClass) {
            this.methodName = methodName;
            this.declaredClass = declaredClass;
        }

        public Type getDeclaredClass() {
            return declaredClass;
        }

        public String methodName() {
            return methodName;
        }
    }
}
