package org.ingko.examples.arrayExample;

import org.ingko.api.Parrot;

import java.util.Random;

public class ArrayExample {
    public static void main(String[] args) {

        Dice[] twoDice = {new Dice(), new Dice()};

        Parrot parrot = Parrot.newSession();
        Dice[] wrappedDice = parrot.createRoot(twoDice, twoDice.getClass());

        System.out.println("/*");
        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedDice[0].roll());
            System.out.println("Rolled " + wrappedDice[1].roll());
        }
        System.out.println("*/");
        String code = parrot.createMockito("org.ingko.examples.arrayExample", "create", "ArrayExampleExpected");
        System.out.println(code);
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
