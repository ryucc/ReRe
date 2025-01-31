package org.ingko.testData;

import org.ingko.api.Parrot;
import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.objects.LocalSymbol;
import org.ingko.core.data.methods.MethodResult;
import org.ingko.core.data.methods.UserMethodCall;
import org.ingko.core.data.objects.EnvironmentNode;
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
public class MagicNumbers {

    public static EnvironmentNode getExpectedNode() {
        try {
            EnvironmentNode root = EnvironmentNode.ofInternal(MathMagic.class);
            EnvironmentMethodCall methodCall = new EnvironmentMethodCall(MathMagic.class.getMethod("magic",
                    MyInt.class));
            /**
             * Add block
             */
            EnvironmentNode userAddValue = EnvironmentNode.ofPrimitive(Integer.class, "1");
            EnvironmentNode userAddParam = EnvironmentNode.ofInternal(MyInt.class);
            EnvironmentMethodCall addEnvGetValue = new EnvironmentMethodCall(MyInt.class.getMethod("getValue"));
            addEnvGetValue.setReturnSymbol(LocalSymbol.local(0));
            addEnvGetValue.setReturnNode(userAddValue);
            addEnvGetValue.setResult(MethodResult.RETURN);
            userAddParam.addMethodCall(addEnvGetValue);
            UserMethodCall add = new UserMethodCall(LocalSymbol.param(0),
                    "add",
                    List.of(userAddParam),
                    List.of(LocalSymbol.local(0)),
                    MyInt.class);
            methodCall.addUserMethodCall(add);
            /**
             * Times block
             */
            EnvironmentNode userTimesValue = EnvironmentNode.ofPrimitive(Integer.class, "2");
            EnvironmentNode userTimesParam = EnvironmentNode.ofInternal(MyInt.class);
            EnvironmentMethodCall timesEnvGetValue = new EnvironmentMethodCall(MyInt.class.getMethod("getValue"));
            timesEnvGetValue.setReturnSymbol(LocalSymbol.local(0));
            timesEnvGetValue.setReturnNode(userTimesValue);
            timesEnvGetValue.setResult(MethodResult.RETURN);
            userTimesParam.addMethodCall(timesEnvGetValue);
            UserMethodCall times = new UserMethodCall(LocalSymbol.returnValue(0),
                    "times",
                    List.of(userTimesParam),
                    List.of(LocalSymbol.local(0)),
                    MyInt.class);
            methodCall.addUserMethodCall(times);
            /**
             * 2nd Add block
             */
            EnvironmentNode userAdd2Value = EnvironmentNode.ofPrimitive(Integer.class, "4");
            EnvironmentNode userAdd2Param = EnvironmentNode.ofInternal(MyInt.class);
            EnvironmentMethodCall add2EnvGetValue = new EnvironmentMethodCall(MyInt.class.getMethod("getValue"));
            add2EnvGetValue.setReturnSymbol(LocalSymbol.local(0));
            add2EnvGetValue.setReturnNode(userAdd2Value);
            add2EnvGetValue.setResult(MethodResult.RETURN);
            userAdd2Param.addMethodCall(add2EnvGetValue);
            UserMethodCall add2 = new UserMethodCall(LocalSymbol.returnValue(1),
                    "add",
                    List.of(userAdd2Param),
                    List.of(LocalSymbol.local(0)),
                    MyInt.class);
            methodCall.addUserMethodCall(add2);

            /**
             * Divide block
             */
            EnvironmentNode userDivideValue = EnvironmentNode.ofPrimitive(Integer.class, "2");
            EnvironmentNode userDivideParam = EnvironmentNode.ofInternal(MyInt.class);
            EnvironmentMethodCall divideEnvGetValue = new EnvironmentMethodCall(MyInt.class.getMethod("getValue"));
            divideEnvGetValue.setReturnSymbol(LocalSymbol.local(0));
            divideEnvGetValue.setReturnNode(userDivideValue);
            divideEnvGetValue.setResult(MethodResult.RETURN);
            userDivideParam.addMethodCall(divideEnvGetValue);
            UserMethodCall divide = new UserMethodCall(LocalSymbol.returnValue(2),
                    "divide",
                    List.of(userDivideParam),
                    List.of(LocalSymbol.local(0)),
                    MyInt.class);
            methodCall.addUserMethodCall(divide);

            /**
             * Divide block
             */
            EnvironmentNode userMinusValue = EnvironmentNode.ofPrimitive(Integer.class, "3");
            EnvironmentNode userMinusParam = EnvironmentNode.ofInternal(MyInt.class);
            EnvironmentMethodCall minusEnvGetValue = new EnvironmentMethodCall(MyInt.class.getMethod("getValue"));
            minusEnvGetValue.setReturnSymbol(LocalSymbol.returnValue(3));
            minusEnvGetValue.setReturnNode(userMinusValue);
            minusEnvGetValue.setResult(MethodResult.RETURN);
            userMinusParam.addMethodCall(minusEnvGetValue);
            UserMethodCall minus = new UserMethodCall(LocalSymbol.returnValue(3),
                    "minus",
                    List.of(userMinusParam),
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

        Parrot parrot = Parrot.newSession();
        MathMagic wrapped = parrot.createRoot(magic, MathMagic.class);

        wrapped.magic(a);

        EnvironmentNode node = parrot.getParrotIntermediateData().roots().getFirst();
        GraphCompare graphCompare = new GraphCompare();

        assertThat(graphCompare.diffNode(getExpectedNode(), node)).isTrue();

    }

    public static class MathMagic {

        public MyInt magic(MyInt a) {
            return a.add(new MyInt(1)).times(new MyInt(2)).add(new MyInt(4)).divide(new MyInt(2)).minus(new MyInt(3));
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

        public MyInt add(MyInt rhs) {
            this.value += rhs.getValue();
            return this;
        }

        public MyInt minus(MyInt rhs) {
            this.value -= rhs.getValue();
            return this;
        }

        public MyInt times(MyInt rhs) {
            this.value *= rhs.getValue();
            return this;
        }

        public MyInt divide(MyInt rhs) {
            this.value /= rhs.getValue();
            return this;
        }

    }
}
