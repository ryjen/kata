package com.github.ryjen.kata.graph;

import com.github.ryjen.kata.graph.list.AdjacencyList;
import com.github.ryjen.kata.graph.model.Factory;

/**
 * Created by ryan on 2017-04-10.
 */
public class AdjacencyListTest extends GraphTest {
    @Override
    public <T extends Comparable<T>> Graph<T> newGraph(Factory<T> factory, boolean directed) {
        return new AdjacencyList<T>(factory, directed);
    }
}
