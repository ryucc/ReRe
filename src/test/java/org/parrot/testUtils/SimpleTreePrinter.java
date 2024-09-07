package org.parrot.testUtils;


import org.parrot.core.data.methods.MethodCall;
import org.parrot.core.data.objects.Node;
import org.parrot.core.synthesizer.SimpleUUIDNaming;

import java.util.Stack;

public class SimpleTreePrinter {

    private final SimpleUUIDNaming namer;
    public SimpleTreePrinter() {
        namer = new SimpleUUIDNaming();

    }


    public void printTree(Node root) {
        Stack<Integer> depth = new Stack<>();
        Stack<MethodCall> edges = new Stack<>();
        System.out.println(namer.getUniqueMockName(root));
        for (MethodCall e : root.getMethodCalls()) {
            edges.push(e);
            depth.push(1);
        }

        while (!edges.empty()) {
            MethodCall e = edges.pop();
            int d = depth.pop();
            for (int i = 0; i < d; i++) {
                System.out.print("    ");
            }
            System.out.print(e.getName());
            Node dest = e.getDest();
            System.out.println("->" + namer.getUniqueMockName(dest));
            for (MethodCall child : dest.getMethodCalls()) {
                edges.push(child);
                depth.push(d + 1);
            }
        }
    }
}
