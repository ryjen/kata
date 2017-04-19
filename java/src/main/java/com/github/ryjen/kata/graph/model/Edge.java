package com.github.ryjen.kata.graph.model;

/**
 * represents and edge between two vertices.
 *
 * @see Factory
 */
public abstract class Edge implements Comparable<Edge> {

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

    @Override
    public int compareTo(Edge o) {
        return Integer.compare(getWeight(), o.getWeight());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Edge) {
            Edge o = (Edge) obj;
            return getWeight() == o.getWeight();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getWeight());
    }
}
