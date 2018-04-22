package com.github.ryjen.kata.graph.matrix;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * an iterable to find adjacent vertices for a vertex
 */
abstract class AdjacentIterator<P extends Comparable<P>, E extends Comparable<E>, V extends Comparable<V>> implements Iterator<P>, Iterable<P> {

    private final AdjacencyMatrix<E, V> graph;
    private final int x;
    private int y;
    private V vertex;

    /**
     * construct an adjacent iterator
     *
     * @param graph  the graph to search
     * @param vertex the vertex index to search from
     */
    AdjacentIterator(AdjacencyMatrix<E, V> graph, V vertex) {
        assert graph != null;
        assert vertex != null;
        this.graph = graph;
        this.x = graph.indexOf(vertex);
        this.y = 0;
        this.vertex = vertex;
    }

    @Override
    public Iterator<P> iterator() {
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

    /**
     * gets the property for a graph row and column
     *
     * @param graph the graph
     * @param row   the row
     * @param col   the column
     * @return the property of the graph
     */
    protected abstract P getPropertyForEntry(AdjacencyMatrix<E, V> graph, int row, int col);

    @Override
    public P next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }


        return getPropertyForEntry(graph, this.x, this.y++);
    }

    public V getVertex() {
        return vertex;
    }
}
