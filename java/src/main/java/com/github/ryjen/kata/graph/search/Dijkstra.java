package com.github.ryjen.kata.graph.search;

import com.github.ryjen.kata.graph.Graph;
import com.github.ryjen.kata.heap.Heap;

import java.util.*;

public class Dijkstra<V extends Comparable<V>> extends Search<Integer, V> implements Comparator<V> {
    private final Map<V, Integer> dist = new HashMap<>();
    private final Set<V> visited;
    private final Heap<V> queue;

    public Dijkstra(Graph<Integer, V> graph, OnVisit<V> callback) {
        super(graph, callback);

        // a heap min queue based on distance
        queue = new Heap<>(graph.size(), Collections.reverseOrder(this));

        // a set based on distance of a vector
        visited = new TreeSet<>(this);
    }

    @Override
    public void search(V source) {

        dist.put(source, 0);

        queue.add(source);

        while (!queue.isEmpty()) {
            V u = queue.remove();

            for (V v : adjacent(u)) {
                int alt = length(u, v);

                if (alt < dist.getOrDefault(v, Integer.MAX_VALUE)) {
                    dist.put(v, alt);
                    queue.add(v);
                }
            }
            visit(u);
        }

        for (V v : visited) {
            callback(v);
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

    private int length(V u, V v) {
        Integer d = dist.getOrDefault(u, Integer.MAX_VALUE);
        if (d == Integer.MAX_VALUE) {
            return d;
        }
        Integer i = getGraph().getEdge(u, v).getLabel();
        return i == null ? d : d+i;
    }

    @Override
    public int compare(V o1, V o2) {
        return Integer.compare(dist.getOrDefault(o1, Integer.MAX_VALUE), dist.getOrDefault(o2, Integer.MAX_VALUE));
    }
}