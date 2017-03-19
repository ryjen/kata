package com.github.ryjen.kata.graph.model;

import java.util.Comparator;

/**
 * Describes how to create vertices and edges in a graph
 *
 * @param <V> the type of vertex
 */
public interface Factory<V extends Comparable<V>> {
    /**
     * creates an edge
     *
     * @return an edge object
     */
    Edge createEdge();

    /**
     * creates an empty edge
     *
     * @return an edge object
     */
    Edge emptyEdge();

    /**
     * creates an array of vertices
     *
     * @param size the size of the array
     * @return an array of vertices
     * NOTE: whether the implementation fills the array or not is up to the implementation
     */
    V[] createVertices(int size);

    /**
     * creates a comparator for vertices
     *
     * @return the comparator or null if default comparison should be used
     */
    Comparator<V> createComparator();
}
