package org.ingko.core.listener.testUtils;

import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.objects.EnvironmentNode;

import java.io.PrintStream;
import java.util.List;

public class GraphCompare {
    PrintStream printStream = System.out;
    public boolean diffMethod(EnvironmentMethodCall a, EnvironmentMethodCall b) {
        if(!a.getSignature().equals(b.getSignature())) {
            printStream.println("MethodCall Signatures differ");
            printStream.println(a.getSignature());
            printStream.println("and");
            printStream.println(b.getSignature());

            return false;
        } else if(!a.getResult().equals(b.getResult())) {
            printStream.println("MethodCall Result differ");
            return false;
        } else {
            if(diffNode(a.getDest(),b.getDest())) {
                return true;
            } else {
                printStream.println("from " + a.getSignature());
                return false;
            }
        }
    }
    public boolean diffNode(EnvironmentNode a, EnvironmentNode b) {
        if(a.isTerminal() != b.isTerminal()) {
            printStream.println("One node is terminal while the other is not.");
            return false;
        }
        if(!a.getRuntimeClass().equals(b.getRuntimeClass())) {
            printStream.println("Nodes have different runtime classes: ");
            printStream.println(a.getRuntimeClass());
            printStream.println("and");
            printStream.println(b.getRuntimeClass());
            return false;
        }
        if (a.isTerminal() && b.isTerminal()) {
            if(a.isSerialized() && b.isSerialized()) {
                return true;
            }
            if(a.getValue().equals(b.getValue())) {
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
        if(callsB.size() != callsA.size()) {
            printStream.println("Number of method calls differ:");
            printStream.println(callsA.size());
            printStream.println("and");
            printStream.println(callsB.size());
            printStream.println("at " + a.getRuntimeClass());
            return false;
        }
        for (int i = 0; i < callsB.size(); i++) {
            if(!diffMethod(callsA.get(i), callsB.get(i))) {
                printStream.println("from " + a + " getMethod " + i);
                printStream.println("from " + a.getRuntimeClass());
                return false;
            }
        }
        return true;
    }
}
