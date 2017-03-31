package com.github.ryjen.kata.graph.model;

import java.util.Comparator;
import java.util.List;

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
     * initializes the vertices
     *
     * @return the list of vertices or null
     */
    List<V> initialVertices();

    /**
     * creates a comparator for vertices
     *
     * @return the comparator or null if default comparison should be used
     */
    Comparator<V> createComparator();
}
