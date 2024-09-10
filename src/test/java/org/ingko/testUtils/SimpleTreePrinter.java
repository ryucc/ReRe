package org.ingko.testUtils;


import org.ingko.core.data.methods.EnvironmentMethodCall;
import org.ingko.core.data.objects.EnvironmentNode;
import org.ingko.core.synthesizer.SimpleUUIDNaming;

import java.util.Stack;

public class SimpleTreePrinter {

    private final SimpleUUIDNaming namer;
    public SimpleTreePrinter() {
        namer = new SimpleUUIDNaming();

    }


    public void printTree(EnvironmentNode root) {
        Stack<Integer> depth = new Stack<>();
        Stack<EnvironmentMethodCall> edges = new Stack<>();
        System.out.println(namer.getUniqueMockName(root));
        for (EnvironmentMethodCall e : root.getMethodCalls()) {
            edges.push(e);
            depth.push(1);
        }

        while (!edges.empty()) {
            EnvironmentMethodCall e = edges.pop();
            int d = depth.pop();
            for (int i = 0; i < d; i++) {
                System.out.print("    ");
            }
            System.out.print(e.getName());
            EnvironmentNode dest = e.getDest();
            System.out.println("->" + namer.getUniqueMockName(dest));
            for (EnvironmentMethodCall child : dest.getMethodCalls()) {
                edges.push(child);
                depth.push(d + 1);
            }
        }
    }
}
