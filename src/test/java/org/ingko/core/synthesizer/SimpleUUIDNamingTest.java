package org.ingko.core.synthesizer;

import org.ingko.core.synthesizer.SimpleUUIDNaming;
import org.junit.jupiter.api.Test;
import org.ingko.core.data.objects.Node;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class SimpleUUIDNamingTest {

    private final SimpleUUIDNaming simpleUUIDNaming = new SimpleUUIDNaming();

    @Test
    public void test() throws Exception {
        UUID uuid = UUID.fromString("5d094348-92d3-4681-bca9-8cfc2a6c1beb");
        Node node = Node.ofInternal(Integer.class);
        // Setting the uuid for testing
        Field uuidField = Node.class.getDeclaredField("uuid");
        uuidField.setAccessible(true);
        uuidField.set(node, uuid);

        //
        assertThat(simpleUUIDNaming.getUniqueMockName(node)).isEqualTo("mockInteger_5d09");
    }

    @Test
    public void testInner() throws Exception {
        UUID uuid = UUID.fromString("5d094348-92d3-4681-bca9-8cfc2a6c1beb");
        Node node = Node.ofInternal(MyClass.class);
        // Setting the uuid for testing
        Field uuidField = Node.class.getDeclaredField("uuid");
        uuidField.setAccessible(true);
        uuidField.set(node, uuid);

        //
        assertThat(simpleUUIDNaming.getUniqueMockName(node)).isEqualTo("mockMyClass_5d09");
    }
    @Test
    public void testTerminal() throws Exception {
        UUID uuid = UUID.fromString("5d094348-92d3-4681-bca9-8cfc2a6c1beb");
        Node node = Node.ofPrimitive(Integer.class, "3");
        // Setting the uuid for testing
        Field uuidField = Node.class.getDeclaredField("uuid");
        uuidField.setAccessible(true);
        uuidField.set(node, uuid);

        //
        assertThat(simpleUUIDNaming.getUniqueMockName(node)).isEqualTo("integer_5d09");
    }

    static class MyClass {}
}