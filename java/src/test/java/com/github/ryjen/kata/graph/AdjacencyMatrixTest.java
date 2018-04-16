package com.github.ryjen.kata.graph;

import com.github.ryjen.kata.graph.matrix.AdjacencyMatrix;
import com.github.ryjen.kata.graph.model.Factory;

/**
 * Created by ryan on 2017-04-10.
 */
public class AdjacencyMatrixTest extends GraphTest {

    @Override
    protected <E extends Comparable<E>, V extends Comparable<V>> Graphable<E, V> getImplementation() {
        return new AdjacencyMatrix<>();
    }
}
