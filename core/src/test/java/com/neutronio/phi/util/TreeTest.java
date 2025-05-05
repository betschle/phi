package com.neutronio.phi.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TreeTest {

    @Test
    public void addChild() {
        Tree<Integer> tree = new Tree<>();
        tree.getRoot().setObject(0);
        tree.add(new Tree.Node<Integer>(1));

        Assertions.assertEquals(1, tree.childrenCount());
        Assertions.assertEquals(0, tree.getRoot().getObject());
    }

    @Test
    public void removeChild() {
        Tree<Integer> tree = new Tree<>();
        tree.getRoot().setObject(0);
        Tree.Node<Integer> node = new Tree.Node<>(1);

        tree.add(node);
        tree.remove(node);

        Assertions.assertFalse(tree.getRoot().containsChild(node));
        Assertions.assertEquals(0, tree.childrenCount());

        tree.add(node);
        tree.getRoot().removeChild(0);

        Assertions.assertFalse(tree.getRoot().containsChild(node));
        Assertions.assertEquals(0, tree.childrenCount());
    }

    @Test
    public void deepTreeAndString() {
        Tree<String> tree = new Tree<>();
        tree.getRoot().setObject("root");
        Tree.Node<String> node = new Tree.Node<>("level1-1");
        Tree.Node<String> node1 = new Tree.Node<>("level1-2");
        Tree.Node<String> node2 = new Tree.Node<>("level2");

        node.addChild(node2);

        tree.add(node);
        tree.add(node1);

        String str = tree.toString();

        Assertions.assertTrue(tree.getRoot().containsChild(node));
        Assertions.assertTrue(tree.getRoot().containsChild(node1));
        Assertions.assertTrue(node.containsChild(node2));
        Assertions.assertEquals(2, tree.childrenCount());
        Assertions.assertTrue(str.contains("level1-1"));
        Assertions.assertTrue(str.contains("level1-2"));
        Assertions.assertTrue(str.contains("level2"));
    }
}

