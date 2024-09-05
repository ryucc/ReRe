package org.parrot.examples;

import org.parrot.core.listener.Listener;
import org.parrot.core.synthesizer.MockitoSynthesizer;

import java.util.Random;

public class TemplateExample {
    public static void main(String[] args) {

        TemplateDice<Integer> dice = new TemplateDice<>(1);
        Listener listener = new Listener();
        TemplateDice wrappedDice = listener.createRoot(dice, TemplateDice.class);

        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedDice.roll());
            System.out.println("goos " + wrappedDice.getObject());

        }

        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer("org.katie.orange.examples", "create");
        System.out.println(mockitoSynthesizer.generateMockito(listener));
    }

    public static class TemplateDice<T> {
        private final Random rand;
        private final T object;

        public TemplateDice(T o) {
            this.object = o;
            rand = new Random();
        }

        public T getObject() {
            return object;
        }

        public int roll() {
            return rand.nextInt(6) + 1;
        }
    }
}
