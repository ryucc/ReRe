package org.parrot.core.listener;

import org.junit.jupiter.api.Test;
import org.parrot.core.data.objects.Node;

public class ListenerRecordTests {
    public record RecordA (int a, RecordB recordB){}
    public record RecordB (int a, int b){}


    @Test
    public void test() {
        RecordB b = new RecordB(1,3);
        RecordA a = new RecordA(1, b);
        Listener listener = new Listener();
        Node root = new Node(Void.class);

        listener.handleRecordRecursive(root, a);
        return;

    }
}
