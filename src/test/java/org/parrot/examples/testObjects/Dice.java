package org.parrot.examples.testObjects;

import java.util.Random;

public class Dice {
    private final Random rand;

    public Dice() {
        rand = new Random();
    }
    public int roll() {
        return rand.nextInt(6) + 1;
    }
}
