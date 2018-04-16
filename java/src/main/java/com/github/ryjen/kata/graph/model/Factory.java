package com.github.ryjen.kata.graph.model;

import com.github.ryjen.kata.graph.Graphable;

/**
 * Describes how to create vertices and edges in a graph
 *
 * @param <V> the type of vertex
 */
public interface Factory<E extends Comparable<E>, V extends Comparable<V>> {

    Graphable<E,V> getImplementation();

    Edge<E,V> getDefaultEdge();

    boolean isEmptyEdge(Edge<E,V> other);

    Edge<E,V> getEmptyEdge();
}
