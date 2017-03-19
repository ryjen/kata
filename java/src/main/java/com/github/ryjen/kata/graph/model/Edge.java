package com.github.ryjen.kata.graph.model;

/**
 * represents and edge between two vertices.
 *
 * @see Factory
 */
public abstract class Edge {

    /**
     * how to display the edge
     *
     * @return a string representation
     */
    public abstract String display();

    @Override
    public String toString() {
        return display();
    }

    /**
     * gets the weight of the edge
     *
     * @return the weight value
     */
    public abstract int getWeight();

    /**
     * tests if the edge is empty
     *
     * @return true if there is no edge between vertices
     */
    public abstract boolean isEmpty();
}
