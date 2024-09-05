package org.parrot.examples;

import org.parrot.core.listener.Listener;
import org.parrot.core.synthesizer.MockitoSynthesizer;

import java.util.Random;

public class ParameterExample {

    public static class PrivateDice {
        public PrivateDice() {
        }
        public int roll(int i, double d, long l, short s, char c, byte b, boolean bl, float f, String str) {
            return 10;
        }

    }

    public static void main(String[] args) {

        PrivateDice dice = new PrivateDice();
        Listener listener = new Listener();
        PrivateDice wrappedDice = listener.createRoot(dice, PrivateDice.class);

        System.out.println("Rolled " + wrappedDice.roll(1, 1.0, 1L, (short) 1, 'a', (byte)0, true, 1, "abc"));

        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer("org.katie.orange.examples", "create");
        System.out.println(mockitoSynthesizer.generateMockito(listener));
    }
}
