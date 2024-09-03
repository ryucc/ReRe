package org.parrot.examples.readme.helloworld;

import org.junit.jupiter.api.Test;
import org.parrot.core.listener.Listener;
import org.parrot.core.synthesizer.MockitoSynthesizer;
import org.parrot.examples.testObjects.Dice;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadmeBasicExample {

    @Test
    public void testMain() throws Exception {

        Dice dice = new Dice();
        Listener listener = new Listener();
        Dice wrappedDice = listener.createRoot(dice, Dice.class);

        List<Integer> actualValues = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            int roll = wrappedDice.roll();
            System.out.println("Rolled " + roll);
            actualValues.add(roll);
        }

        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer("org.katie.orange.examples", "create");
        String generatedCode = mockitoSynthesizer.generateMockito(listener);
        Path expectedCodePath = Path.of("src/test/java/org/parrot/examples/readme/helloworld/expectedJavaTemplate");
        String expectedCode = Files.readString(expectedCodePath);
        for (int i = 0; i < 5; i++) {
            expectedCode = expectedCode.replace("{ret" + i + "}", Integer.toString(actualValues.get(i)));
        }
        assertThat(generatedCode).isEqualTo(expectedCode);
    }
}