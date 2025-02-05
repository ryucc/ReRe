package org.rere.testData;

import org.rere.core.data.methods.EnvironmentMethodCall;
import org.rere.core.data.objects.LocalSymbol;
import org.rere.core.data.methods.UserMethodCall;
import org.rere.core.data.objects.EnvironmentNode;

import java.io.PrintStream;
import java.util.List;

public class GraphCompare {
    PrintStream printStream = System.out;

    public boolean diffEnvironmentMethod(EnvironmentMethodCall a, EnvironmentMethodCall b) {
        if (!a.getSignature().equals(b.getSignature())) {
            printStream.println("MethodCall Signatures differ");
            printStream.println(a.getSignature());
            printStream.println("and");
            printStream.println(b.getSignature());

            return false;
        } else if (!b.getResult().equals(a.getResult())) {
            printStream.println("MethodCall Result differ");
            return false;
        }
        if (!diffNode(a.getDest(), b.getDest())) {
            printStream.println("from " + a.getSignature());
            return false;
        }
        if (a.getUserMethodCalls().size() != b.getUserMethodCalls().size()) {
            printStream.printf("UserMethodCall.size() differ, a has %d, b has %d%n",
                    a.getUserMethodCalls().size(),
                    b.getUserMethodCalls().size());
            return false;
        }
        for (int i = 0; i < a.getUserMethodCalls().size(); i++) {
            UserMethodCall userCallA = a.getUserMethodCalls().get(i);
            UserMethodCall userCallB = b.getUserMethodCalls().get(i);
            if (!diffUserMethodCall(userCallA, userCallB)) {
                printStream.println("from " + a.getSignature() + " " + "environmentNode " + i);
                return false;

            }
        }
        return true;
    }

    public boolean diffUserMethodCall(UserMethodCall a, UserMethodCall b) {
        if (!a.getMethodName().equals(b.getMethodName())) {
            printStream.println("UserMethodCall.methodName differ " + a.getMethodName() + " " + b.getMethodName());
            return false;
        }
        if (!a.getSource().equals(b.getSource())) {
            printStream.println("UserMethodCall sources differ " + a.getSource() + " " + b.getSource());
            return false;
        }
        if (a.getParameters().size() != b.getParameters().size()) {
            printStream.println("UserMethodCall parameterSize differ " + a.getMethodName() + " " + b.getMethodName());
            return false;
        } else {
            for (int i = 0; i < a.getParameters().size(); i++) {
                LocalSymbol localSymbola = a.getParameters().get(i);
                LocalSymbol localSymbolb = b.getParameters().get(i);
                if (!localSymbolb.equals(localSymbola)) {
                    printStream.println("LocalSymbol diff: " + localSymbola + " " + localSymbolb);
                    return false;
                }
            }
        }
        if (a.getLocalParameters().size() != b.getLocalParameters().size()) {
            printStream.println("UserMethodCall parameterSize differ " + a.getMethodName() + " " + b.getMethodName());
            return false;
        } else {
            for (int i = 0; i < a.getLocalParameters().size(); i++) {
                EnvironmentNode environmentNodea = a.getLocalParameters().get(i);
                EnvironmentNode environmentNodeb = b.getLocalParameters().get(i);
                if (!diffNode(environmentNodea, environmentNodeb)) {
                    printStream.println("from userMethodCall" + a.getMethodName());
                    return false;
                }
            }
        }
        return true;

    }

    public boolean diffNode(EnvironmentNode a, EnvironmentNode b) {
        if (b.isTerminal() != a.isTerminal()) {
            printStream.println("One node is terminal while the other is not.");
            return false;
        }
        if (!a.getRuntimeClass().equals(b.getRuntimeClass())) {
            printStream.println("Nodes have different runtime classes: ");
            printStream.println(a.getRuntimeClass());
            printStream.println("and");
            printStream.println(b.getRuntimeClass());
            return false;
        }
        if (a.isTerminal() && b.isTerminal()) {
            if (a.isSerialized() && b.isSerialized()) {
                return true;
            }
            if (a.getValue().equals(b.getValue())) {
                return true;
            } else {
                printStream.println("Nodes have different values: ");
                printStream.println(a.getValue());
                printStream.println(b.getValue());
                printStream.println("at " + a.getRuntimeClass());
                return false;
            }
        }
        List<EnvironmentMethodCall> callsA = a.getMethodCalls();
        List<EnvironmentMethodCall> callsB = b.getMethodCalls();
        if (callsB.size() != callsA.size()) {
            printStream.println("Number of method calls differ:");
            printStream.println(callsA.size());
            printStream.println("and");
            printStream.println(callsB.size());
            printStream.println("at " + a.getRuntimeClass());
            return false;
        }
        for (int i = 0; i < callsB.size(); i++) {
            if (!diffEnvironmentMethod(callsA.get(i), callsB.get(i))) {
                printStream.println("from " + a + " getMethod " + i);
                printStream.println("from " + a.getRuntimeClass());
                return false;
            }
        }
        return true;
    }
}
