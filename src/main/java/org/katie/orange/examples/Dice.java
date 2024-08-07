package org.katie.orange.examples;

import java.util.Random;

public class Dice {

    private final Random rand;
    public Dice(int i) {
        rand = new Random();
    }
    public int roll() {
        return rand.nextInt(6) + 1;
    }
    public void something() {}

    public DiceDice getDiceDice() {
        return new DiceDice();
    }

    public class DiceDice {
            public int roll() {
                return rand.nextInt(6) + 1;
            }
    }
}
