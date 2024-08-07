package org.katie.orange.core.synthesizer;

import org.katie.orange.core.data.methods.InternalMethodCall;
import org.katie.orange.core.data.objects.InternalNode;

import java.util.Stack;

public class SimpleTreePrinter {

    public void printTree(InternalNode root) {
        Stack<Integer> depth = new Stack<>();
        Stack<InternalMethodCall> edges = new Stack<>();
        System.out.println(root.getDisplayName());
        for (InternalMethodCall e : root.getInternalMethodCalls()) {
            edges.push(e);
            depth.push(1);
        }

        while (!edges.empty()) {
            InternalMethodCall e = edges.pop();
            int d = depth.pop();
            for (int i = 0; i < d; i++) {
                System.out.print("    ");
            }
            System.out.print(e.getName());
            InternalNode dest = e.getDest();
            System.out.println("->" + dest.getDisplayName());
            for (InternalMethodCall child : dest.getInternalMethodCalls()) {
                edges.push(child);
                depth.push(d + 1);
            }
        }
    }
}
