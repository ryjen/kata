package com.github.ryjen.kata.graph.search;

import com.github.ryjen.kata.graph.Graph;

import java.util.*;

/**
 * A Depth-First implementation of search
 */
public class DepthFirstSearch<E extends Comparable<E>, V extends Comparable<V>> extends Search<E, V> {

    private final Ordering ordering;
    private final Set<V> visited = new LinkedHashSet<>();

    public DepthFirstSearch(Graph<E, V> graph, OnVisit<V> callback) {
        this(graph, callback, Ordering.Pre);
    }

    public DepthFirstSearch(Graph<E, V> graph, OnVisit<V> callback, Ordering ordering) {
        super(graph, callback);
        this.ordering = ordering;
    }

    @Override
    public void search(V v) {
        switch (ordering) {
            default:
            case Pre:
                searchPreOrder(v);
                break;
            case Post:
                searchPostOrder(v);
                break;
            case ReversePost:
                searchReversePostOrder(v);
                break;
        }
    }

    @Override
    public boolean isVisited(V v) {
        return visited.contains(v);
    }

    @Override
    public void visit(V v) {
        visited.add(v);
    }

    /**
     * starts the search
     */
    private void searchPreOrder(V v) {

        if (isVisited(v)) {
            return;
        }

        visit(v);
        callback(v);

        for (V w : adjacent(v)) {
            searchPreOrder(w);
        }
    }

    private void searchPostOrder(V v) {

        if (isVisited(v)) {
            return;
        }

        visit(v);

        for (V w : adjacent(v)) {
            searchPostOrder(w);
        }

        callback(v);
    }


    private void searchReversePostOrder(V v, List<V> scratch) {
        if (isVisited(v)) {
            return;
        }

        visit(v);

        for (V w : adjacent(v)) {
            searchReversePostOrder(w, scratch);
        }

        scratch.add(v);
    }

    private void searchReversePostOrder(V v) {

        List<V> scratch = new ArrayList<>();

        searchReversePostOrder(v, scratch);

        Collections.reverse(scratch);

        for (V w : scratch) {
            callback(w);
        }
    }
}
