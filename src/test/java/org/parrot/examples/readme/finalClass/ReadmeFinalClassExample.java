package org.parrot.examples.readme.finalClass;

import org.junit.jupiter.api.Test;
import org.parrot.core.listener.Listener;
import org.parrot.core.synthesizer.CodeSynthesizer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadmeFinalClassExample {
    public static final class FinalDice {

        private final Random rand;
        public FinalDice() {
            rand = new Random();
        }
        public int roll() {
            return rand.nextInt(6) + 1;
        }

    }

    @Test
    public void testMain() throws Exception{

        FinalDice dice = new FinalDice();
        Listener listener = new Listener();
        FinalDice wrappedDice = listener.createRoot(dice);

        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedDice.roll());
        }

        CodeSynthesizer codeSynthesizer = new CodeSynthesizer("org.parrot.examples.readme.finalClass", "create");
        String generatedCode = codeSynthesizer.generateMockito(listener);
        System.out.println(generatedCode);
        Path expectedCodePath = Path.of("src/test/java/org/parrot/examples/readme/finalClass/MockFinalDiceCreatorjava");
        String expectedCode = Files.readString(expectedCodePath);
        assertThat(generatedCode).isEqualTo(expectedCode);
    }
}