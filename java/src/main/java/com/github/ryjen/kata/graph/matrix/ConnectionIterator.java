package com.github.ryjen.kata.graph.matrix;

import com.github.ryjen.kata.graph.model.Connection;
import com.github.ryjen.kata.graph.model.Edge;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates edges in a graph
 */
class ConnectionIterator<Vertex extends Comparable<Vertex>> implements Iterator<Connection<Vertex>>, Iterable<Connection<Vertex>> {

    private final AdjacencyMatrix<Vertex> graph;
    private int row;
    private int col;

    public ConnectionIterator(AdjacencyMatrix<Vertex> graph) {
        assert graph != null;
        this.graph = graph;
        this.row = 0;
        this.col = 0;
    }

    @Override
    public Iterator<Connection<Vertex>> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        while (row < graph.size()) {
            while (col < graph.size()) {
                if (graph.isEdgeByRowColumn(row, col)) {
                    return true;
                }
                col++;
            }
            row++;
            col = 0;
        }
        return false;
    }

    @Override
    public Connection next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        Vertex a = graph.getVertexByRow(row);
        Vertex b = graph.getVertexByRow(col);
        Edge edge = graph.getEdgeByIndices(row, col++);

        return new Connection(a, b, edge);
    }
}
