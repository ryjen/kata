package com.github.ryjen.kata.graph.search;

import com.github.ryjen.kata.graph.Graph;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * A Breadth-First implementation of search
 */
public class BreadthFirstSearch<E extends Comparable<E>, V extends Comparable<V>> extends Search<E, V> {

    private final Set<V> visited = new LinkedHashSet<>();

    public BreadthFirstSearch(Graph<E, V> graph, OnVisit<V> callback) {
        super(graph, callback);
    }

    @Override
    public void search(V v) {
        Queue<V> queue = new LinkedList<>();
        queue.add(v);

        while (!queue.isEmpty()) {

            v = queue.remove();

            if (isVisited(v)) {
                continue;
            }

            visit(v);

            callback(v);

            for (V w : adjacent(v)) {
                queue.add(w);
            }
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
}
