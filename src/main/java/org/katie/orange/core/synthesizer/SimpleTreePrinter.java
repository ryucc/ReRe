package org.katie.orange.core.synthesizer;


import org.katie.orange.core.data.methods.MethodCall;
import org.katie.orange.core.data.objects.Node;

import java.util.Stack;

public class SimpleTreePrinter {

    public void printTree(Node root) {
        Stack<Integer> depth = new Stack<>();
        Stack<MethodCall> edges = new Stack<>();
        System.out.println(root.getUniqueMockName());
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
            System.out.println("->" + dest.getUniqueMockName());
            for (MethodCall child : dest.getMethodCalls()) {
                edges.push(child);
                depth.push(d + 1);
            }
        }
    }
}
