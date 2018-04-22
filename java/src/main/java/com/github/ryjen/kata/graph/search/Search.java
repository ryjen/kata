package com.github.ryjen.kata.graph.search;

import com.github.ryjen.kata.graph.Graph;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * an abstract class to implement a search on an graph
 */
public abstract class Search<E extends Comparable<E>, V extends Comparable<V>> {
    private final Graph<E, V> graph;
    private final OnVisit<V> callback;

    /**
     * construct a new search
     *
     * @param graph the graph to search
     */
    Search(Graph<E, V> graph, OnVisit<V> callback) {
        assert graph != null;
        assert callback != null;

        this.graph = graph;
        this.callback = callback;
    }

    /**
     * gets an iterable to adjacent vertices
     *
     * @param v the vertex
     * @return an iterable of adjacent vertices
     */
    Iterable<V> adjacent(V v) {
        return graph.adjacent(v);
    }

    public abstract void search(V v);

    /**
     * tests if a vertex has been visited
     *
     * @param v the vertex
     * @return true if the vertex has been visited
     */
    public abstract boolean isVisited(V v);

    /**
     * flags a vertex as visited
     *
     * @param v the vertex
     */
    public abstract void visit(V v);


    void callback(V v) {
        callback.onSearchVisit(v);
    }

    public Graph<E, V> getGraph() {
        return graph;
    }

    /**
     * callback for searching
     *
     * @param <V> the type of vertex
     */
    public interface OnVisit<V extends Comparable<V>> {
        void onSearchVisit(V value);
    }
}
