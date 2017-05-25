package com.github.ryjen.kata.graph.matrix;

/**
 * an iterable to find adjacent vertices for a vertex
 */
class AdjacentVertexIterator<Vertex extends Comparable<Vertex>> extends AdjacentIterator<Vertex, Vertex> {

    /**
     * construct an adjacent iterator
     *
     * @param graph  the graph to search
     * @param vertex the vertex index to search from
     */
    AdjacentVertexIterator(AdjacencyMatrix<Vertex> graph, Vertex vertex) {
        super(graph, vertex);
    }

    @Override
    protected Vertex getPropertyForEntry(AdjacencyMatrix<Vertex> graph, int col, int row) {
        return graph.getVertexByRow(row);
    }
}
