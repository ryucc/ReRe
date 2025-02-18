/*
 * Copyright (c) 2025 <project contributors>
 * This program is made available under the terms of the MIT License.
 */

package org.rere.testData;

import org.rere.api.ReRe;
import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.objects.LocalSymbol;
import org.rere.core.data.methods.MethodResult;
import org.rere.core.data.methods.UserMethodCall;
import org.rere.core.data.objects.EnvironmentNode;
import org.rere.core.synthesizer.mockito.MockitoSynthesizer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MathMagic is a class containing 1 method, demonstrating
 * ((x + 1) * 2 + 4) / 2 - 3 = x
 * <p>
 * This is used to test a method that modifies the input parameters.
 * <p>
 * In MagicNumbersSimple, the parameters passed to the userMethodCalls are primitives.
 * In MagicNumbers, the parameters passed to the userMethodCalls are classes.
 */
public class MagicNumbersSimple {

    public static MathMagic getMagicNumbers() {
        return new MathMagic();
    }

    public static EnvironmentNode getExpectedNode() {
        try {
            EnvironmentNode root = EnvironmentNode.ofInternal(MathMagic.class, MagicNumbers.MathMagic.class);
            EnvironmentMethodCall methodCall = new EnvironmentMethodCall(MathMagic.class.getMethod("magic",
                    MyInt.class));
            /**
             * Add block
             */
            EnvironmentNode userAddValue = EnvironmentNode.ofPrimitive(int.class, "1");
            UserMethodCall add = new UserMethodCall(LocalSymbol.param(0),
                    "add",
                    List.of(userAddValue),
                    List.of(LocalSymbol.local(0)),
                    MyInt.class);
            methodCall.addUserMethodCall(add);
            /**
             * Times block
             */
            EnvironmentNode userTimesValue = EnvironmentNode.ofPrimitive(int.class, "2");
            UserMethodCall times = new UserMethodCall(LocalSymbol.returnValue(0),
                    "times",
                    List.of(userTimesValue),
                    List.of(LocalSymbol.local(0)),
                    MyInt.class);
            methodCall.addUserMethodCall(times);
            /**
             * 2nd Add block
             */
            EnvironmentNode userAdd2Value = EnvironmentNode.ofPrimitive(int.class, "4");
            UserMethodCall add2 = new UserMethodCall(LocalSymbol.returnValue(1),
                    "add",
                    List.of(userAdd2Value),
                    List.of(LocalSymbol.local(0)),
                    MyInt.class);
            methodCall.addUserMethodCall(add2);

            /**
             * Divide block
             */
            EnvironmentNode userDivideValue = EnvironmentNode.ofPrimitive(int.class, "2");
            UserMethodCall divide = new UserMethodCall(LocalSymbol.returnValue(2),
                    "divide",
                    List.of(userDivideValue),
                    List.of(LocalSymbol.local(0)),
                    MyInt.class);
            methodCall.addUserMethodCall(divide);

            /**
             * Divide block
             */
            EnvironmentNode userMinusValue = EnvironmentNode.ofPrimitive(int.class, "3");
            UserMethodCall minus = new UserMethodCall(LocalSymbol.returnValue(3),
                    "minus",
                    List.of(userMinusValue),
                    List.of(LocalSymbol.local(0)),
                    MyInt.class);
            methodCall.addUserMethodCall(minus);

            methodCall.setReturnSymbol(new LocalSymbol(LocalSymbol.Source.RETURN_VALUE, 4));
            methodCall.setResult(MethodResult.RETURN);
            methodCall.setReturnNode(EnvironmentNode.ofInternal(MyInt.class, MyInt.class));
            root.addMethodCall(methodCall);

            methodCall.setReturnClass(MyInt.class);
            methodCall.setParamClasses(List.of(MyInt.class));

            return root;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void test() {
        MathMagic magic = new MathMagic();

        MyInt a = new MyInt(100);

        ReRe rere = new ReRe();
        MathMagic wrapped = rere.createSpiedObject(magic, MathMagic.class);

        wrapped.magic(a);

        EnvironmentNode node = rere.getReReRecordData().roots().getFirst();
        MockitoSynthesizer synthesizer = new MockitoSynthesizer("asd", "asdf");
        synthesizer.generateMockito(getExpectedNode(), "create");
        GraphCompare graphCompare = new GraphCompare();

        assertThat(graphCompare.diffNode(getExpectedNode(), node)).isTrue();

    }

    public static class MathMagic {

        public MyInt magic(MyInt a) {
            return a.add(1).times(2).add(4).divide(2).minus(3);
        }
    }


    public static class MyInt {
        private int value;

        public MyInt(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public MyInt add(int rhs) {
            this.value += rhs;
            return this;
        }

        public MyInt minus(int rhs) {
            this.value -= rhs;
            return this;
        }

        public MyInt times(int rhs) {
            this.value *= rhs;
            return this;
        }

        public MyInt divide(int rhs) {
            this.value /= rhs;
            return this;
        }

    }
}
