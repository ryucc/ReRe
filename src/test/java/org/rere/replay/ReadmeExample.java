/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.replay;

import org.junit.jupiter.api.Test;
import org.rere.api.ReRe;
import org.rere.api.ReReMode;
import org.rere.api.ReReSettings;
import org.rere.core.data.objects.EnvironmentNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadmeExample {
    @Test
    public void test() {

        Dice dice = new Dice();
        ReRe reRe = new ReRe();
        Dice wrappedDice = reRe.createReReObject(dice, Dice.class);


        List<Integer> rollResult = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            rollResult.add(wrappedDice.roll());
        }
        // replay
        ReRe replayReRe = new ReRe(new ReReSettings().withReReMode(ReReMode.REPLAY)
                .withReReplayData(reRe.getReReRecordData()));
        Dice replayDice = replayReRe.createReReObject(null, Dice.class);
        List<Integer> replayResult = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            replayResult.add(replayDice.roll());
        }
        assertThat(replayResult).usingRecursiveComparison().isEqualTo(rollResult);
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