package org.ingko.core.synthesizer.mockito.nodes;

import com.palantir.javapoet.TypeSpec;
import org.ingko.core.data.objects.EnvironmentNode;

public interface EnvironmentNodeSynthesizer {
    SynthResult generateEnvironmentNode(TypeSpec.Builder typeBuilder, EnvironmentNode root);
    record SynthResult(String methodName) {
    }
}
