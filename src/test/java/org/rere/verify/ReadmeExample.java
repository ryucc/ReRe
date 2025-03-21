/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.verify;

import org.junit.jupiter.api.Test;
import org.rere.api.ReRe;
import org.rere.api.ReReMode;
import org.rere.api.ReReSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadmeExample {

    @Test
    public void test() {
        ReRe reRe = new ReRe();
        System.out.println("Record Run");
        run(reRe);

        ReRe replayReRe = new ReRe(new ReReSettings().withReReMode(ReReMode.REPLAY)
                .withReReplayData(reRe.getReReData()));
        System.out.println("Replay Run");
        run(replayReRe);
    }
    public void run(ReRe reRe) {

        Dice dice = new Dice();
        Dice wrappedDice = reRe.createReReObject(dice, Dice.class);

        List<Integer> rollResult = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            int val = wrappedDice.roll();
            System.out.println(val);
            reRe.verify(val);
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