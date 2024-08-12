package org.parrot.examples.readme.finalClass;

import org.parrot.core.listener.Listener;
import org.parrot.core.synthesizer.CodeSynthesizer;

import java.util.Random;

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

    public static void main(String[] args) {

        FinalDice dice = new FinalDice();
        Listener listener = new Listener();
        FinalDice wrappedDice = listener.createRoot(dice);

        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedDice.roll());
        }

        CodeSynthesizer codeSynthesizer = new CodeSynthesizer("org.katie.orange.examples", "create");
        System.out.println(codeSynthesizer.generateMockito(listener));
    }
}