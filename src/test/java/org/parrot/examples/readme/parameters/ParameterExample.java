package org.parrot.examples.readme.parameters;

import org.junit.jupiter.api.Test;
import org.parrot.core.listener.Listener;
import org.parrot.core.synthesizer.MockitoSynthesizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class ParameterExample {

    public static class PrivateDice {
        public PrivateDice() {
        }
        public int roll(int i, double d, long l, short s, char c, byte b, boolean bl, float f, String str) {
            return 10;
        }

    }

    @Test
    public void testMain() throws Exception {

        PrivateDice dice = new PrivateDice();
        Listener listener = new Listener();
        PrivateDice wrappedDice = listener.createRoot(dice, PrivateDice.class);

        System.out.println("Rolled " + wrappedDice.roll(1, 1.0, 1L, (short) 1, 'a', (byte)0, true, 1, "abc"));

        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer("org.katie.orange.examples", "create");
        System.out.println(mockitoSynthesizer.generateMockito(listener));
        String generatedCode = mockitoSynthesizer.generateMockito(listener);
        Path expectedCodePath = Path.of("src/test/java/org/parrot/examples/readme/parameters/expectedJavaTemplate");
        // Use this line to reset new changes
        //Files.writeString(expectedCodePath, generatedCode);
        String expectedCode = Files.readString(expectedCodePath);
        assertThat(generatedCode).isEqualTo(expectedCode);
    }
}
