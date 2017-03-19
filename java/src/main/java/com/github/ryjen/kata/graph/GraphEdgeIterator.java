package com.github.ryjen.kata.graph;

import com.github.ryjen.kata.graph.model.Edge;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterates edges in a graph
 */
class GraphEdgeIterator implements Iterator<Iterable<Edge>>, Iterable<Iterable<Edge>> {

    final AdjacencyGraph<?> graph;
    int row;

    public GraphEdgeIterator(AdjacencyGraph<?> graph) {
        assert graph != null;
        this.graph = graph;
        this.row = 0;
    }

    @Override
    public Iterator<Iterable<Edge>> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return row < graph.size();
    }

    @Override
    public Iterable<Edge> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return new RowIterator(graph, row++);
    }

    private static class RowIterator implements Iterator<Edge>, Iterable<Edge> {

        final int row;
        AdjacencyGraph<?> graph;
        int col;

        public RowIterator(AdjacencyGraph<?> graph, int row) {
            assert graph != null;
            assert row >= 0 && row < graph.size();
            this.graph = graph;
            this.row = row;
            this.col = 0;
        }

        @Override
        public Iterator<Edge> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            return col < graph.size();
        }

        @Override
        public Edge next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return graph.getEdge(row, col++);
        }
    }
}
