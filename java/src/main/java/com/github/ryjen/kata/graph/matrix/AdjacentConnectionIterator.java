package com.github.ryjen.kata.graph.matrix;

import com.github.ryjen.kata.graph.model.Connection;
import com.github.ryjen.kata.graph.model.Edge;


/**
 * an iterable to find adjacent vertices for a vertex
 */
class AdjacentConnectionIterator<Vertex extends Comparable<Vertex>> extends AdjacentIterator<Connection<Vertex>, Vertex> {

    /**
     * construct an adjacent iterator
     *
     * @param graph  the graph to search
     * @param vertex the vertex index to search from
     */
    AdjacentConnectionIterator(AdjacencyMatrix<Vertex> graph, Vertex vertex) {
        super(graph, vertex);
    }

    @Override
    protected Connection<Vertex> getPropertyForEntry(AdjacencyMatrix<Vertex> graph, int col, int row) {
        Vertex v = graph.getVertexByRow(row);
        Edge e = graph.getEdgeByIndices(col, row);
        return new Connection<>(getVertex(), v, e);
    }
}
