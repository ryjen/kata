package com.github.ryjen.kata.graph.matrix;

import com.github.ryjen.kata.graph.model.Edge;
import com.github.ryjen.kata.graph.model.Endpoint;


/**
 * an iterable to find adjacent vertices for a vertex
 */
class AdjacentEntryIterator<Vertex extends Comparable<Vertex>> extends AdjacentIterator<Endpoint<Vertex>, Vertex> {

    /**
     * construct an adjacent iterator
     *
     * @param graph  the graph to search
     * @param vertex the vertex index to search from
     */
    AdjacentEntryIterator(AdjacencyMatrix<Vertex> graph, int vertex) {
        super(graph, vertex);
    }

    @Override
    protected Endpoint<Vertex> getPropertyForEntry(AdjacencyMatrix<Vertex> graph, int col, int row) {
        Vertex v = graph.getVertexByRow(row);
        Edge e = graph.getEdgeByIndices(col, row);
        return new Endpoint<>(v, e);
    }
}
