package com.github.ryjen.kata.graph.search;

import com.github.ryjen.kata.graph.Graph;

import java.util.HashSet;
import java.util.Set;

/**
 * an abstract class to implement a search on an graph
 */
public abstract class Search<Vertex extends Comparable<Vertex>> {
    private Set<Vertex> visited;
    private Graph<Vertex> graph;
    private OnVisit<Vertex> callback;

    /**
     * construct a new search
     *
     * @param graph the graph to search
     */
    public Search(Graph<Vertex> graph, OnVisit<Vertex> callback) {
        assert graph != null;
        assert callback != null;

        this.graph = graph;
        this.callback = callback;

        visited = new HashSet<>(graph.size());
    }

    /**
     * gets an iterable to adjacent vertices
     *
     * @param v the vertex
     * @return an iterable of adjacent vertices
     */
    public Iterable<Vertex> adjacent(Vertex v) {
        return graph.adjacent(v);
    }

    /**
     * Method to perform a recursive search on a vertex
     *
     * @param v the vertex
     */
    protected abstract void search(Vertex v);

    /**
     * starts the search
     */
    public void search() {
        for (Vertex v : graph.vertices()) {
            if (!isVisited(v)) {
                search(v);
            }
        }
    }

    /**
     * tests if a vertex has been visited
     *
     * @param v the vertex
     * @return true if the vertex has been visited
     */
    protected boolean isVisited(Vertex v) {
        assert v != null;
        return visited.contains(v);
    }

    /**
     * flags a vertex as visited
     *
     * @param v the vertex
     */
    protected void visit(Vertex v) {
        assert v != null;
        visited.add(v);
        callback.onSearchVisit(v);
    }

    /**
     * callback for searching
     *
     * @param <V> the type of vertex
     */
    public interface OnVisit<V> {
        void onSearchVisit(V value);
    }

}
