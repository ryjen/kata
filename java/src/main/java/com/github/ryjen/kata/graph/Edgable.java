package com.github.ryjen.kata.graph;

import com.github.ryjen.kata.graph.model.Edge;

/**
 * Interface for an entity that supports edges
 *
 * @param <E> edge type
 * @param <V> vertex type
 */
public interface Edgable<E extends Comparable<E>, V extends Comparable<V>> {

    void addEdge(V a, V b, Edge<E, V> edge);

    boolean isEdge(V a, V b);

    Edge<E, V> getEdge(V a, V b);

    boolean removeEdge(V a, V b);

    Iterable<Edge<E, V>> edges();

    Iterable<Edge<E, V>> edges(V v);
}
