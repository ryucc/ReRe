package org.katie.orange.examples;

import org.katie.orange.core.listener.Listener;
import org.katie.orange.core.synthesizer.CodeSynthesizer;

import java.util.Random;

public class ReadmeFailureNodeExample {
    private static class PrivateDice {

        private final Random rand;
        public PrivateDice() {
            rand = new Random();
        }
        public int roll() {
            return rand.nextInt(6) + 1;
        }

    }

    public static void main(String[] args) {

        PrivateDice dice = new PrivateDice();
        Listener listener = new Listener();
        PrivateDice wrappedDice = listener.createRoot(dice);

        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedDice.roll());
        }

        CodeSynthesizer codeSynthesizer = new CodeSynthesizer("org.katie.orange.examples", "create");
        System.out.println(codeSynthesizer.generateMockito(listener));
    }
}