package org.ingko.testData;

import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.methods.LocalSymbol;
import org.ingko.core.data.methods.MethodResult;
import org.ingko.core.data.methods.UserMethodCall;
import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.listener.EnvironmentObjectListener;
import org.ingko.core.listener.testUtils.GraphCompare;
import org.ingko.core.synthesizer.mockito.javafile.ParameterModSynthesizer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MathMagic is a class containing 1 method, demonstrating
 * ((x + 1) * 2 + 4) / 2 - 3 = x
 *
 * This is used to test a method that modifies the input parameters.
 *
 * In MagicNumbersSimple, the parameters passed to the userMethodCalls are primitives.
 * In MagicNumbers, the parameters passed to the userMethodCalls are classes.
 *
 */
public class MagicNumbersSimple {

    public static MathMagic getMagicNumbers() {
        return new MathMagic();
    }

    public static EnvironmentNode getExpectedNode() {
        try {
            EnvironmentNode root = EnvironmentNode.ofInternal(MathMagic.class);
            EnvironmentMethodCall methodCall = new EnvironmentMethodCall(MathMagic.class.getMethod("magic",
                    MyInt.class));
            /**
             * Add block
             */
            EnvironmentNode userAddValue = EnvironmentNode.ofPrimitive(Integer.class, "1");
            UserMethodCall add = new UserMethodCall(LocalSymbol.param(0),
                    "add",
                    List.of(userAddValue),
                    List.of(LocalSymbol.local(0)),
                    MyInt.class);
            methodCall.addUserMethodCall(add);
            /**
             * Times block
             */
            EnvironmentNode userTimesValue = EnvironmentNode.ofPrimitive(Integer.class, "2");
            UserMethodCall times = new UserMethodCall(LocalSymbol.returnValue(0),
                    "times",
                    List.of(userTimesValue),
                    List.of(LocalSymbol.local(0)),
                    MyInt.class);
            methodCall.addUserMethodCall(times);
            /**
             * 2nd Add block
             */
            EnvironmentNode userAdd2Value = EnvironmentNode.ofPrimitive(Integer.class, "4");
            UserMethodCall add2 = new UserMethodCall(LocalSymbol.returnValue(1),
                    "add",
                    List.of(userAdd2Value),
                    List.of(LocalSymbol.local(0)),
                    MyInt.class);
            methodCall.addUserMethodCall(add2);

            /**
             * Divide block
             */
            EnvironmentNode userDivideValue = EnvironmentNode.ofPrimitive(Integer.class, "2");
            UserMethodCall divide = new UserMethodCall(LocalSymbol.returnValue(2),
                    "divide",
                    List.of(userDivideValue),
                    List.of(LocalSymbol.local(0)),
                    MyInt.class);
            methodCall.addUserMethodCall(divide);

            /**
             * Divide block
             */
            EnvironmentNode userMinusValue = EnvironmentNode.ofPrimitive(Integer.class, "3");
            UserMethodCall minus = new UserMethodCall(LocalSymbol.returnValue(3),
                    "minus",
                    List.of(userMinusValue),
                    List.of(LocalSymbol.local(0)),
                    MyInt.class);
            methodCall.addUserMethodCall(minus);

            methodCall.setReturnSymbol(new LocalSymbol(LocalSymbol.Source.RETURN_VALUE, 4));
            methodCall.setResult(MethodResult.RETURN);
            methodCall.setReturnNode(EnvironmentNode.ofInternal(MyInt.class));
            root.addMethodCall(methodCall);

            return root;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void test() {
        MathMagic magic = new MathMagic();

        MyInt a = new MyInt(100);

        EnvironmentObjectListener environmentObjectListener = new EnvironmentObjectListener();
        MathMagic wrapped = environmentObjectListener.createRoot(magic, MathMagic.class);

        wrapped.magic(a);

        EnvironmentNode node = environmentObjectListener.getRoot();
        ParameterModSynthesizer synthesizer = new ParameterModSynthesizer("asd", "asdf");
        synthesizer.generateMockito(getExpectedNode());
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
