package com.github.ryjen.kata.graph.matrix;

import com.github.ryjen.kata.graph.model.Edge;

/**
 * an iterable to find adjacent vertices for a vertex
 */
class AdjacentEdgeIterator<Vertex extends Comparable<Vertex>> extends AdjacentIterator<Edge, Vertex> {

    /**
     * construct an adjacent iterator
     *
     * @param graph  the graph to search
     * @param vertex the vertex index to search from
     */
    AdjacentEdgeIterator(AdjacencyMatrix<Vertex> graph, int vertex) {
        super(graph, vertex);
    }

    @Override
    protected Edge getPropertyForEntry(AdjacencyMatrix<Vertex> graph, int col, int row) {
        return graph.getEdgeByIndices(col, row);
    }
}
