package com.github.ryjen.kata.graph.sort;

import com.github.ryjen.kata.graph.Graph;
import com.github.ryjen.kata.graph.exceptions.GraphCyclicException;
import com.github.ryjen.kata.graph.exceptions.GraphDirectedException;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Topological sort for a graph
 */
public class TopologicalSort<E extends Comparable<E>, V extends Comparable<V>> {

    private static final int MARK_TEMP = 1;
    private static final int MARK_PERM = 2;

    private final Graph<E, V> graph;
    private final Map<V, Integer> marked;

    public TopologicalSort(Graph<E, V> graph) throws GraphDirectedException {
        if (!graph.isDirected()) {
            throw new GraphDirectedException();
        }
        this.graph = graph;
        this.marked = new HashMap<>();
    }

    public Collection<V> sort() throws GraphCyclicException {
        LinkedList<V> result = new LinkedList<>();

        for (V v : graph.vertices()) {

            sort(v, result);
        }

        return result;
    }

    private boolean isMarked(V vertex) throws GraphCyclicException {
        int mark = marked.getOrDefault(vertex, 0);

        if (mark == MARK_TEMP) {
            throw new GraphCyclicException();
        }

        return mark == MARK_PERM;
    }

    private void sort(V vertex, LinkedList<V> result) throws GraphCyclicException {
        if (!isMarked(vertex)) {
            marked.put(vertex, MARK_TEMP);

            for (V w : graph.adjacent(vertex)) {
                sort(w, result);
            }

            marked.put(vertex, MARK_PERM);
            result.addFirst(vertex);
        }
    }
}
