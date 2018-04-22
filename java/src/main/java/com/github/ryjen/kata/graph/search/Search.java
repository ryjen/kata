package com.github.ryjen.kata.graph.search;

import com.github.ryjen.kata.graph.Graph;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * an abstract class to implement a search on an graph
 */
public abstract class Search<E extends Comparable<E>, V extends Comparable<V>> {
    private final Set<V> visited;
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

        visited = new LinkedHashSet<>(graph.size());
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
    boolean isVisited(V v) {
        assert v != null;
        return visited.contains(v);
    }

    /**
     * flags a vertex as visited
     *
     * @param v the vertex
     */
    void visit(V v) {
        assert v != null;
        visited.add(v);
    }

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
