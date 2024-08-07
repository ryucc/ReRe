package org.katie.orange.examples;

import  org.katie.orange.core.MockFactory;
import org.katie.orange.core.MockFactory2;
import org.katie.orange.core.data.objects.InternalNode;
import org.katie.orange.core.data.objects.Node;
import org.katie.orange.core.synthesizer.DFS;
import org.katie.orange.core.synthesizer.DFS2;
import org.katie.orange.core.synthesizer.SimpleTreePrinter;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        System.out.println(Void.class.getName());
        Dice dice = new Dice(1);
        System.out.println(UUID.randomUUID().toString().substring(0,4));


        MockFactory mockFactory = new MockFactory();
        InternalNode root = new InternalNode(Dice.class);

        Dice c = mockFactory.createInternal(dice, root);
        DFS dfs = new DFS();

        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + c.roll());
        }
        c.something();

        for (int i = 1; i <= 5; i++) {
            System.out.println("Rolled " + c.getDiceDice().roll());
        }
        dfs.generateMockito(root);

    }
}