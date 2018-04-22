package com.github.ryjen.kata.graph;

/**
 * An interface to indicate an entity has degrees for a vertex
 *
 * @param <V> vertex type
 */
public interface Degreable<V extends Comparable<V>> {

    /**
     * test for the number of connections into a vertex
     *
     * @param vertex the vertex to check
     * @return the number of in connections
     */
    long inDegree(V vertex);

    /**
     * test for the number of connections coming from a vertex
     *
     * @param vertex the vertex to test
     * @return the number of out connections
     */
    long outDegree(V vertex);

}
