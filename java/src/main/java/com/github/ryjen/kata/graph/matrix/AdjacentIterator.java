package com.github.ryjen.kata.graph.matrix;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * an iterable to find adjacent vertices for a vertex
 */
abstract class AdjacentIterator<T, Vertex extends Comparable<Vertex>> implements Iterator<T>, Iterable<T> {

    private final AdjacencyMatrix<Vertex> graph;
    private final int x;
    private int y;

    /**
     * construct an adjacent iterator
     *
     * @param graph  the graph to search
     * @param vertex the vertex index to search from
     */
    AdjacentIterator(AdjacencyMatrix<Vertex> graph, int vertex) {
        assert graph != null;
        assert vertex >= 0 && vertex < graph.size();
        this.graph = graph;
        this.x = vertex;
        this.y = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {

        while (this.y < graph.size()) {
            if (graph.isEdgeByRowColumn(this.x, this.y)) {
                return true;
            }
            this.y++;
        }

        return false;
    }

    protected abstract T getPropertyForEntry(AdjacencyMatrix<Vertex> graph, int row, int col);

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }


        return getPropertyForEntry(graph, this.x, this.y++);
    }
}
