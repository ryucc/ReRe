package org.parrot.examples;

import org.parrot.core.listener.Listener;
import org.parrot.core.synthesizer.MockitoSynthesizer;

import java.util.Random;

public class RecordExample {
    public static class Dice {

        private final Random rand;
        public Dice() {
            rand = new Random();
        }
        public int roll() {
            return rand.nextInt(6) + 1;
        }

    }

    public record TwoDice(Dice dice1, Dice dice2){};

    public static void main(String[] args) {

        TwoDice twoDice = new TwoDice(new Dice(), new Dice());

        Listener listener = new Listener();
        TwoDice wrappedDice = listener.createRoot(twoDice, TwoDice.class);

        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedDice.dice1().roll());
            System.out.println("Rolled " + wrappedDice.dice2().roll());
        }

        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer("org.katie.orange.examples", "create");
        System.out.println(mockitoSynthesizer.generateMockito(listener));
    }
}
