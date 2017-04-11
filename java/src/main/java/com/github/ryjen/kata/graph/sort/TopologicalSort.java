package com.github.ryjen.kata.graph.sort;

import com.github.ryjen.kata.graph.Graph;
import com.github.ryjen.kata.graph.exceptions.GraphIsCyclicException;
import com.github.ryjen.kata.graph.exceptions.GraphNotDirectedException;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by ryan on 2017-04-01.
 */
public class TopologicalSort<T extends Comparable<T>> {

    private static final int MARK_TEMP = 1;
    private static final int MARK_PERM = 2;

    private final Graph<T> graph;
    private final Map<T, Integer> marked;

    public TopologicalSort(Graph<T> graph) throws GraphNotDirectedException {
        if (!graph.isDirected()) {
            throw new GraphNotDirectedException();
        }
        this.graph = graph;
        this.marked = new HashMap<>();
    }

    public Collection<T> sort() throws GraphIsCyclicException {
        LinkedList<T> result = new LinkedList<>();

        for (T v : graph.vertices()) {

            sort(v, result);
        }

        return result;
    }

    private boolean isMarked(T vertex) throws GraphIsCyclicException {
        int mark = marked.getOrDefault(vertex, 0);

        if (mark == MARK_TEMP) {
            throw new GraphIsCyclicException();
        }

        return mark == MARK_PERM;
    }

    private void sort(T vertex, LinkedList<T> result) throws GraphIsCyclicException {
        if (!isMarked(vertex)) {
            marked.put(vertex, MARK_TEMP);

            for (T w : graph.adjacent(vertex)) {
                sort(w, result);
            }

            marked.put(vertex, MARK_PERM);
            result.addFirst(vertex);
        }
    }
}
