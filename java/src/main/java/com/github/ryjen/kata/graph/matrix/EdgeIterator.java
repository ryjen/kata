package com.github.ryjen.kata.graph.matrix;

import com.github.ryjen.kata.graph.model.Edge;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates edges in a graph
 */
class EdgeIterator implements Iterator<Edge>, Iterable<Edge> {

    private final MatrixGraph<?> graph;
    private int row;
    private int col;

    public EdgeIterator(MatrixGraph<?> graph) {
        assert graph != null;
        this.graph = graph;
        this.row = 0;
        this.col = 0;
    }

    @Override
    public Iterator<Edge> iterator() {
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
    public Edge next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        return graph.getEdgeByIndices(row, col++);

    }
}
