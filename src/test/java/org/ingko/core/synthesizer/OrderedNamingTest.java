package org.ingko.core.synthesizer;

import org.ingko.core.data.objects.EnvironmentNode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderedNamingTest {

    class MyClass {
    }

    @Test
    public void testArray() {
        OrderedNaming orderedNaming = new OrderedNaming();
        EnvironmentNode environmentNode3 = EnvironmentNode.ofInternal(Integer[].class);
        String i3 = orderedNaming.getUniqueMockName(environmentNode3);
        assertThat(i3).isEqualTo("mockIntegerArray1");
    }
    @Test
    public void testNested() {
        OrderedNaming orderedNaming = new OrderedNaming();
        EnvironmentNode environmentNode3 = EnvironmentNode.ofInternal(MyClass.class);
        String i3 = orderedNaming.getUniqueMockName(environmentNode3);
        assertThat(i3).isEqualTo("mockMyClass1");

    }
    @Test
    public void test() {
        OrderedNaming orderedNaming = new OrderedNaming();
        EnvironmentNode environmentNode1 = EnvironmentNode.ofInternal(EnvironmentNode.class);
        EnvironmentNode environmentNode2 = EnvironmentNode.ofInternal(EnvironmentNode.class);
        EnvironmentNode environmentNode4 = EnvironmentNode.ofPrimitive(int.class, "10");

        String i1 = orderedNaming.getUniqueMockName(environmentNode1);
        String i11 = orderedNaming.getUniqueMockName(environmentNode1);
        String i2 = orderedNaming.getUniqueMockName(environmentNode2);
        String i4 = orderedNaming.getUniqueMockName(environmentNode4);

        assertThat(i1).isEqualTo("mockEnvironmentNode1");
        assertThat(i11).isEqualTo("mockEnvironmentNode1");
        assertThat(i2).isEqualTo("mockEnvironmentNode2");
        assertThat(i4).isEqualTo("int1");
    }

}