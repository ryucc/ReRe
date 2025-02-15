/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere;

import org.junit.jupiter.api.Test;
import org.rere.api.ReRe;
import org.rere.api.ReReSettings;

public class TempTest {
    @Test
    public void test() {
        ReRe reRe = new ReRe(new ReReSettings().withNoParameterModding(true));
        SomeCalculation someCalculation = new SomeCalculation();
        SomeCalculation rereCalc = reRe.createSpiedObject(someCalculation, SomeCalculation.class);
        rereCalc.ans(new Person(29, "Katie", "Alaska"));
        String code = reRe.exportMockito("aaa", "asd", "aaa");
        System.out.println(code);

    }
    public class SomeCalculation {
        public String ans(Person person) {
            for(int i =0; i < 10; i++) {
                int seed = (i * i + i + 3 * i * i * i )% 3;
                if (seed == 0) {
                    person.age();
                } else if(seed == 1) {
                    person.book();
                } else {
                    person.name();
                }
            }
            return "Good person";
        }

    }

    public class Person {
        private final int age;
        private final String name;
        private final String book;

        public Person(int age, String name, String book) {
            this.age = age;
            this.name = name;
            this.book = book;
        }

        public int age() {
            return age;
        }

        public String name() {
            return name;
        }

        public String book() {
            return book;
        }
    }
}
