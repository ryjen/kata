package com.github.ryjen.kata.graph;

import com.github.ryjen.kata.graph.list.AdjacencyList;

/**
 * Runs the test cases with an adjacency list implementation
 */
public class AdjacencyListTest extends GraphTest {

    @Override
    protected <E extends Comparable<E>, V extends Comparable<V>> Graphable<E, V> getImplementation() {
        return new AdjacencyList<>();
    }
}
