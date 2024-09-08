package org.ingko.core.synthesizer;

import org.ingko.core.synthesizer.OrderedNaming;
import org.junit.jupiter.api.Test;
import org.ingko.core.data.objects.Node;

import static org.assertj.core.api.Assertions.assertThat;

class OrderedNamingTest {

    class MyClass {
    }
    @Test
    public void test() {
        OrderedNaming orderedNaming = new OrderedNaming();
        Node node1 = Node.ofInternal(Node.class);
        Node node2 = Node.ofInternal(Node.class);
        Node node3 = Node.ofInternal(MyClass.class);
        Node node4 = Node.ofPrimitive(int.class, "10");

        String i1 = orderedNaming.getUniqueMockName(node1);
        String i11 = orderedNaming.getUniqueMockName(node1);
        String i2 = orderedNaming.getUniqueMockName(node2);
        String i3 = orderedNaming.getUniqueMockName(node3);
        String i4 = orderedNaming.getUniqueMockName(node4);

        assertThat(i1).isEqualTo("mockNode1");
        assertThat(i11).isEqualTo("mockNode1");
        assertThat(i2).isEqualTo("mockNode2");
        assertThat(i3).isEqualTo("mockMyClass1");
        assertThat(i4).isEqualTo("int1");
    }

}