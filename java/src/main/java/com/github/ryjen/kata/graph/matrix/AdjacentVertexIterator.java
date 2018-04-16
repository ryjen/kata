package com.github.ryjen.kata.graph.matrix;

/**
 * an iterable to find adjacent vertices for a vertex
 */
class AdjacentVertexIterator<E extends Comparable<E>, Vertex extends Comparable<Vertex>> extends AdjacentIterator<Vertex, E, Vertex> {

    /**
     * construct an adjacent iterator
     *
     * @param graph  the graph to search
     * @param vertex the vertex index to search from
     */
    AdjacentVertexIterator(AdjacencyMatrix<E, Vertex> graph, Vertex vertex) {
        super(graph, vertex);
    }

    @Override
    protected Vertex getPropertyForEntry(AdjacencyMatrix<E, Vertex> graph, int col, int row) {
        return graph.getVertexByRow(row);
    }
}
