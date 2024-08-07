package org.katie.orange.examples;

import org.katie.orange.core.Listener;
import org.katie.orange.core.synthesizer.CodeSynthesizer;

public class Main {
    public static void main(String[] args) {

        Dice dice = new Dice(1);
        Listener listener = new Listener();
        Dice wrappedDice = listener.wrap(dice);

        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedDice.roll());
        }

        CodeSynthesizer codeSynthesizer = new CodeSynthesizer("org.katie.orange.examples", "create");
        System.out.println(codeSynthesizer.generateMockito(listener));
    }
}