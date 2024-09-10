package org.ingko.core.listener;

import org.junit.jupiter.api.Test;
import org.ingko.core.data.objects.EnvironmentNode;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class ListenerRecordTests {
    public record RecordA (int a, RecordB recordB){}
    public record RecordB (int a, int b){
    }


    @Test
    public void test() {
        RecordB b = new RecordB(1,3);
        RecordA a = new RecordA(1, b);
        Listener listener = new Listener();

        Listener.ListenResult<?> aa = listener.handleRecord(a, RecordA.class, new HashMap<>());
        EnvironmentNode environmentNode = aa.dataEnvironmentNode();
        RecordA wrappedA = (RecordA) aa.wrapped();
        assertThat(wrappedA).isEqualTo(a);

        assertThat(environmentNode.getRuntimeClass()).isEqualTo(RecordA.class);
        assertThat(environmentNode.getDirectChildren())
                .hasSize(2);
        assertThat(environmentNode.getDirectChildren().getFirst().getRuntimeClass())
                .isEqualTo(int.class);
        assertThat(environmentNode.getDirectChildren().getFirst().getValue())
                .isEqualTo(Integer.valueOf(a.a()).toString());
        EnvironmentNode bEnvironmentNode = environmentNode.getDirectChildren().getLast();
        assertThat(bEnvironmentNode.getRuntimeClass())
                .isEqualTo(RecordB.class);
        assertThat(bEnvironmentNode.getDirectChildren().getFirst().getRuntimeClass())
                .isEqualTo(int.class);
        assertThat(bEnvironmentNode.getDirectChildren().getFirst().getValue())
                .isEqualTo(Integer.valueOf(b.a()).toString());
        assertThat(bEnvironmentNode.getDirectChildren().getLast().getRuntimeClass())
                .isEqualTo(int.class);
        assertThat(bEnvironmentNode.getDirectChildren().getLast().getValue())
                .isEqualTo(Integer.valueOf(b.b()).toString());
    }
}
