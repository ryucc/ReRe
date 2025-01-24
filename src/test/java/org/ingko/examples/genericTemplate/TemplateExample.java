package org.ingko.examples.genericTemplate;

import org.ingko.core.listener.interceptor.EnvironmentObjectListener;
import org.ingko.core.synthesizer.mockito.javafile.ParameterModSynthesizer;

import java.util.Random;

public class TemplateExample {
    public static void main(String[] args) {

        TemplateDice<Integer> dice = new TemplateDice<>(1);
        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        TemplateDice wrappedDice = environmentObjectListener.createRoot(dice, TemplateDice.class);
        System.out.println("/*");

        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedDice.roll());
            System.out.println("goos " + wrappedDice.getObject());

        }
        System.out.println("*/");

        ParameterModSynthesizer mockitoSynthesizer = new ParameterModSynthesizer("org.ingko.examples.templateMatching",
                "create",
                "TemplateExampleExpected");
        System.out.println(mockitoSynthesizer.generateMockito(environmentObjectListener.getRoot()));
    }

    public static class TemplateDice<T> {
        private final Random rand;
        private final T object;

        public TemplateDice(T o) {
            this.object = o;
            rand = new Random(0);
        }

        public T getObject() {
            return object;
        }

        public int roll() {
            return rand.nextInt(6) + 1;
        }
    }
}
