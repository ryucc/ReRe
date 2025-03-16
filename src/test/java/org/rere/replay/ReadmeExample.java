/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.replay;

import org.rere.api.ReRe;
import org.rere.core.data.objects.EnvironmentNode;

import java.util.Random;

public class ReadmeExample {
    public static void main(String[] args) {

        Dice dice = new Dice();
        ReRe reRe = new ReRe();
        Dice wrappedDice = reRe.createSpiedObject(dice, Dice.class);


        wrappedDice.chill();
        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedDice.roll());
        }
        EnvironmentNode node = reRe.getReReRecordData().roots().get(0);
        Dice replayDice = reRe.createReplayMock(node, Dice.class);
        for (int i = 1; i <= 5; i++) {
            System.out.println("Replay Rolled " + replayDice.roll());
        }


    }

    public static class Dice {
        private final Random rand;

        public Dice() {
            rand = new Random(1);
        }

        public void chill() {
        }

        public int roll() {
            return rand.nextInt(6) + 1;
        }

    }
}