package com.github.ryjen.kata.graph.matrix;

import com.github.ryjen.kata.graph.model.Edge;

/**
 * an iterable to find adjacent vertices for a vertex
 */
class AdjacentEdgeIterator<E extends Comparable<E>, V extends Comparable<V>> extends AdjacentIterator<Edge<E, V>, E, V> {

    /**
     * construct an adjacent iterator
     *
     * @param graph  the graph to search
     * @param vertex the vertex index to search from
     */
    AdjacentEdgeIterator(AdjacencyMatrix<E, V> graph, V vertex) {
        super(graph, vertex);
    }

    @Override
    protected Edge<E, V> getPropertyForEntry(AdjacencyMatrix<E, V> graph, int col, int row) {
        return graph.getEdgeByIndices(col, row);
    }
}
