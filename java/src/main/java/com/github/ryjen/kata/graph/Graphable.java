package com.github.ryjen.kata.graph;

/**
 * an interface to indicate an entity is a graph that supports vertices and edges
 *
 * @param <E> edge type
 * @param <V> vertex type
 */
public interface Graphable<E extends Comparable<E>, V extends Comparable<V>> extends Vertexable<V>, Edgable<E, V>, Degreable<V> {

    /**
     * gets the size of the graph in terms of number of vertices
     *
     * @return the size
     */
    int size();

    /**
     * clear all vertices and edges from the instance
     */
    void clear();

    /**
     * clones this instance
     *
     * @return a new cloned instance
     */
    Graphable<E, V> copy();

}
