package org.ingko.examples;

import org.ingko.core.listener.EnvironmentObjectListener;
import org.ingko.core.synthesizer.mockito.javafile.MockitoSynthesizer;

import java.util.Random;

public class ArrayExample {
    public static void main(String[] args) {

        Dice[] twoDice = {new Dice(), new Dice()};

        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        Dice[] wrappedDice = environmentObjectListener.createRoot(twoDice, twoDice.getClass());

        System.out.println(twoDice.getClass().getSimpleName());

        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedDice[0].roll());
            System.out.println("Rolled " + wrappedDice[1].roll());
        }

        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer("org.katie.orange.examples", "create");
        System.out.println(mockitoSynthesizer.generateMockito(environmentObjectListener));
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

}
