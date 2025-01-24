package org.ingko.examples.readme;

import org.ingko.core.listener.interceptor.EnvironmentObjectListener;
import org.ingko.core.synthesizer.mockito.MockitoSynthesizer;

import java.util.Random;

/*
This code prints out the following:

package org.katie.orange.examples;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

import org.mockito.Mockito;
import org.ingko.examples.examples.ReadmeExample;

public class MockPrivateDiceCreator {
  public static ReadmeExample.PrivateDice create() {
    ReadmeExample.PrivateDice mockPrivateDice1 = Mockito.mock(ReadmeExample.PrivateDice.class);
    doReturn(1).doReturn(6).doReturn(5).doReturn(1).doReturn(3).when(mockPrivateDice1).roll();
    return mockPrivateDice1;
  }
}

 */
public class ReadmeExample {
    public static void main(String[] args) {

        PrivateDice dice = new PrivateDice();
        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        PrivateDice wrappedDice = environmentObjectListener.createRoot(dice, PrivateDice.class);


        System.out.println("/*");
        wrappedDice.chill();
        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + wrappedDice.roll());
        }
        System.out.println("*/");

        MockitoSynthesizer mockitoSynthesizer = new MockitoSynthesizer("org.ingko.examples.readme",
                "create",
                "ReadmeExampleExpected");
        //ParameterModSynthesizer parameterModSynthesizer = new ParameterModSynthesizer("org.katie.orange.examples", "create");
        System.out.println(mockitoSynthesizer.generateMockito(environmentObjectListener.getRoot()));
        //System.out.println(parameterModSynthesizer.generateMockito(environmentObjectListener.getRoot()));
    }

    public static class PrivateDice {
        private final Random rand;

        public PrivateDice() {
            rand = new Random(1);
        }

        public void chill(){}

        public int roll() {
            return rand.nextInt(6) + 1;
        }

    }
}