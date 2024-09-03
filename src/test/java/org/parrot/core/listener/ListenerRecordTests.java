package org.parrot.core.listener;

import org.junit.jupiter.api.Test;
import org.parrot.core.data.objects.Node;

import static org.assertj.core.api.Assertions.assertThat;

public class ListenerRecordTests {
    public record RecordA (int a, RecordB recordB){}
    public record RecordB (int a, int b){}


    @Test
    public void test() {
        RecordB b = new RecordB(1,3);
        RecordA a = new RecordA(1, b);
        Listener listener = new Listener();

        Listener.ListenResult<?> aa = listener.handleRecord(a, RecordA.class);
        Node node = aa.dataNode();
        RecordA wrappedA = (RecordA) aa.wrapped();
        assertThat(wrappedA).isEqualTo(a);

        assertThat(node.getRuntimeClass()).isEqualTo(RecordA.class);
        assertThat(node.getDirectChildren())
                .hasSize(2);
        assertThat(node.getDirectChildren().getFirst().getRuntimeClass())
                .isEqualTo(int.class);
        assertThat(node.getDirectChildren().getFirst().getValue())
                .isEqualTo(Integer.valueOf(a.a()).toString());
        Node bNode = node.getDirectChildren().getLast();
        assertThat(bNode.getRuntimeClass())
                .isEqualTo(RecordB.class);
        assertThat(bNode.getDirectChildren().getFirst().getRuntimeClass())
                .isEqualTo(int.class);
        assertThat(bNode.getDirectChildren().getFirst().getValue())
                .isEqualTo(Integer.valueOf(b.a()).toString());
        assertThat(bNode.getDirectChildren().getLast().getRuntimeClass())
                .isEqualTo(int.class);
        assertThat(bNode.getDirectChildren().getLast().getValue())
                .isEqualTo(Integer.valueOf(b.b()).toString());
    }
}
