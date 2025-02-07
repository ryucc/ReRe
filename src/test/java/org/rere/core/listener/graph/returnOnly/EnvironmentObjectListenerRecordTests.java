/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.core.listener.graph.returnOnly;

import org.rere.api.ReRe;
import org.rere.core.data.objects.EnvironmentNode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EnvironmentObjectListenerRecordTests {
    public record RecordA (int a, RecordB recordB){}
    public record RecordB (int a, int b){
    }


    @Test
    public void test() {
        RecordB b = new RecordB(1,3);
        RecordA a = new RecordA(1, b);
        ReRe rere = ReRe.newSession();

        RecordA wrappedA = rere.createRoot(a, RecordA.class);
        assertThat(wrappedA).isEqualTo(a);

        EnvironmentNode environmentNode = rere.getReReIntermediateData().roots().get(0);

        assertThat(environmentNode.getRuntimeClass()).isEqualTo(RecordA.class);
        assertThat(environmentNode.getDirectChildren())
                .hasSize(2);
        assertThat(environmentNode.getDirectChildren().get(0).getRuntimeClass())
                .isEqualTo(int.class);
        assertThat(environmentNode.getDirectChildren().get(0).getValue())
                .isEqualTo(Integer.valueOf(a.a()).toString());
        EnvironmentNode bEnvironmentNode = environmentNode.getDirectChildren().getLast();
        assertThat(bEnvironmentNode.getRuntimeClass())
                .isEqualTo(RecordB.class);
        assertThat(bEnvironmentNode.getDirectChildren().get(0).getRuntimeClass())
                .isEqualTo(int.class);
        assertThat(bEnvironmentNode.getDirectChildren().getFirst().getValue())
                .isEqualTo(Integer.valueOf(b.a()).toString());
        assertThat(bEnvironmentNode.getDirectChildren().getLast().getRuntimeClass())
                .isEqualTo(int.class);
        assertThat(bEnvironmentNode.getDirectChildren().getLast().getValue())
                .isEqualTo(Integer.valueOf(b.b()).toString());
    }
}
