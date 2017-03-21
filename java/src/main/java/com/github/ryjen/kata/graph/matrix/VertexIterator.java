package com.github.ryjen.kata.graph.matrix;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * iterate through all vertices
 */
class VertexIterator<Vertex extends Comparable<Vertex>> implements Iterator<Vertex>, Iterable<Vertex> {
    final MatrixGraph<Vertex> graph;
    int v;

    /**
     * constructs a vertex iterator
     *
     * @param graph the graph to iterate
     */
    public VertexIterator(MatrixGraph<Vertex> graph) {
        this.graph = graph;
        this.v = 0;
    }

    @Override
    public Iterator<Vertex> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        while (v < graph.size()) {
            if (graph.getVertex(v) != null) {
                return true;
            }
            v++;
        }
        return false;
    }

    @Override
    public Vertex next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return graph.getVertex(v++);
    }
}
