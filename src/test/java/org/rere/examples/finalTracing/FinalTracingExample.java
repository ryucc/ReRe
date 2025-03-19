/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.examples.finalTracing;

import org.rere.api.ReRe;
import org.rere.api.ReReSettings;

import java.util.Random;

public class FinalTracingExample {
    public static void main(String[] args) {

        Dice dice = new Dice();
        DiceRoller diceRoller = new DiceRoller();
        ReRe reRe = new ReRe(new ReReSettings().withParameterModding(true));
        DiceRoller wrappedRoller = reRe.createReReObject(diceRoller, DiceRoller.class);


        System.out.println("/*");
        MyInt mi = new MyInt(6);
        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedRoller.roll(dice, mi));
        }
        System.out.println("*/");

        String code = reRe.exportMockito("org.rere.examples.finalTracing", "create", "FinalTracingExampleExpected");
        System.out.println(code);
    }

    public static final class MyInt {
        private final int value;

        public MyInt(int i) {
            value = i;
        }

        public int getValue() {
            return value;
        }
    }
    public static class DiceRoller {
        public int roll(Dice dice, MyInt i) {
            return dice.roll(i);
        }
    }

    public static class Dice {
        private final Random rand;

        public Dice() {
            rand = new Random(1);
        }


        public int roll(MyInt i) {
            return rand.nextInt(i.getValue()) + 1;
        }
    }
}