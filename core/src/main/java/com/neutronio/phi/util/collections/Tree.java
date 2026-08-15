package com.neutronio.phi.util.collections;


import java.util.*;

/**
 * A generic, variable Tree with any amount of children.
 *
 * @param <T> the type that is stored here.
 */
public class Tree<T> {

    public static class Node<T> {
        /** The parent of this node. */
        private Node<T> parent = null ;
        /** Children nodes */
        protected List<Node<T>> children = new ArrayList<>();
        /** The object stored in this node. */
        protected T object;

        public Node(T object) {
            this.object = object;
        }

        public Node<T> addChild(Node<T> child) {
            child.parent = this;
            this.children.add( child );
            return child;
        }

        public T getObject() {
            return object;
        }

        public void setObject(T object) {
            this.object = object;
        }

        /**
         * Removes this node from its parent,
         * if it has one.
         * @return
         */
        public Node<T> remove() {
            return this.parent.removeChild(this);
        }

        public Node<T> removeChild(int index) {
            Node<T> remove = this.children.remove(index);
            remove.parent = null;
            return remove;
        }

        public boolean containsChild( Node<T> node) {
            return this.children.contains(node);
        }

        public Node<T> getChild(int index) {
            return this.children.get(index);
        }

        /**
         * Removes a child from this node, sets its parent to null.
         * @param node the node to remove
         * @return the removed node
         */
        public Node<T> removeChild(Node<T> node) {
            this.children.remove(node);
            node.parent = null;
            return node;
        }

        public int size() {
            return this.children.size();
        }

        public Node<T> getParent() {
            return parent;
        }
    }
    /** The root node, initialized with a null object. */
    private Node root = new Node(null);

    public Node<T> getRoot() {
        return this.root;
    }

    /**
     * Convenience method to add a node.
     * @param node
     * @return
     */
    public Node<T> add(Node<T> node) {
        return this.root.addChild(node);
    }

    /**
     * Convenience method to remove a node.
     * @param node
     * @return
     */
    public Node<T> remove(Node<T> node) {
        return this.root.removeChild(node);
    }

    /**
     *
     * @return # of children on the root node
     */
    public int childrenCount() {
        return this.root.size();
    }

    /**
     * Gets a child from the root node.
     * @param index the index
     * @return the child node at index
     */
    public Node<T> getChildAt(int index) {
        return this.root.getChild(index);
    }

    private void getStringRecursive(int currentDepth, Node<T> current, StringBuilder builder) {
        if (current != null) {
            builder.append("\n");
            for( int i = 0; i < currentDepth; i++) {
                builder.append("- ");
            }
            builder.append(current.object);

            for( Node<T> node : current.children) {
                getStringRecursive(currentDepth + 1, node, builder);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        getStringRecursive(0, root, builder);
        return builder.toString();
    }
}
