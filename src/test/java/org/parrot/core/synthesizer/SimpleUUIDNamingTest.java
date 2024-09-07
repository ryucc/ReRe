package org.parrot.core.synthesizer;

import org.junit.jupiter.api.Test;
import org.parrot.core.data.objects.Node;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class SimpleUUIDNamingTest {

    private SimpleUUIDNaming simpleUUIDNaming = new SimpleUUIDNaming();
    @Test
    public void test() throws Exception{
        UUID uuid = UUID.fromString("5d094348-92d3-4681-bca9-8cfc2a6c1beb");
        Node node = Node.ofInternal(Integer.class);
        // Setting the uuid for testing
        Field uuidField = Node.class.getDeclaredField("uuid");
        uuidField.setAccessible(true);
        uuidField.set(node, uuid);

        //
        assertThat(simpleUUIDNaming.getUniqueMockName(node))
                .isEqualTo("mockInteger_5d09");
    }

}