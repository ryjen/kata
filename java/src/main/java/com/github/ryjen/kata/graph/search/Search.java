package com.github.ryjen.kata.graph.search;

import com.github.ryjen.kata.graph.Graph;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * an abstract class to implement a search on an graph
 */
public abstract class Search<Vertex extends Comparable<Vertex>> {
    private final Set<Vertex> visited;
    private final Graph<Vertex> graph;
    private final OnVisit<Vertex> callback;

    /**
     * construct a new search
     *
     * @param graph the graph to search
     */
    Search(Graph<Vertex> graph, OnVisit<Vertex> callback) {
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
    Iterable<Vertex> adjacent(Vertex v) {
        return graph.adjacent(v);
    }

    public abstract void search(Vertex v);

    /**
     * tests if a vertex has been visited
     *
     * @param v the vertex
     * @return true if the vertex has been visited
     */
    boolean isVisited(Vertex v) {
        assert v != null;
        return visited.contains(v);
    }

    /**
     * flags a vertex as visited
     *
     * @param v the vertex
     */
    void visit(Vertex v) {
        assert v != null;
        visited.add(v);
    }

    void callback(Vertex v) {
        callback.onSearchVisit(v);
    }

    public Set<Vertex> getVisited() {
        return Collections.unmodifiableSet(visited);
    }

    public Graph<Vertex> getGraph() {
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
