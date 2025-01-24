package org.ingko.examples.recordExample;

import org.ingko.core.listener.interceptor.EnvironmentObjectListener;
import org.ingko.core.synthesizer.mockito.MockitoSynthesizer;

import java.util.Random;

public class RecordExample {
    public static void main(String[] args) {

        TwoDice twoDice = new TwoDice(new Dice(), new Dice());

        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        TwoDice wrappedDice = environmentObjectListener.createRoot(twoDice, TwoDice.class);

        System.out.println("/*");
        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedDice.dice1().roll());
            System.out.println("Rolled " + wrappedDice.dice2().roll());
        }
        System.out.println("*/");

        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer("org.ingko.examples.recordExample",
                "create",
                "RecordExampleExpected");
        System.out.println(mockitoSynthesizer.generateMockito(environmentObjectListener.getRoot()));
    }

    public static class Dice {

        private final Random rand;

        public Dice() {
            rand = new Random(0);
        }

        public int roll() {
            return rand.nextInt(6) + 1;
        }

    }

    public record TwoDice(Dice dice1, Dice dice2) {
    }
}
