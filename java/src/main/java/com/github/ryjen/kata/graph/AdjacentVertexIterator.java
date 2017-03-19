package com.github.ryjen.kata.graph;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * an iterable to find adjacent vertices for a vertex
 */
class AdjacentVertexIterator<Vertex extends Comparable<Vertex>> implements Iterator<Vertex>, Iterable<Vertex> {

    final AdjacencyGraph<Vertex> graph;
    final int v;
    int u;

    /**
     * construct an adjacent iterator
     *
     * @param graph  the graph to search
     * @param vertex the vertex index to search from
     */
    AdjacentVertexIterator(AdjacencyGraph<Vertex> graph, int vertex) {
        assert graph != null;
        assert vertex >= 0 && vertex < graph.size();
        this.graph = graph;
        this.v = vertex;
        this.u = 0;
    }

    @Override
    public Iterator<Vertex> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        while (u < graph.size()) {
            if (graph.isEdge(v, u)) {
                return true;
            }
            u++;
        }
        return false;
    }

    @Override
    public Vertex next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return graph.getVertex(u++);
    }
}
