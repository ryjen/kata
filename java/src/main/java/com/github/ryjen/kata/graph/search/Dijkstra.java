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

        queue = new Heap<>(graph.size(), Collections.reverseOrder(this));

        visited = new TreeSet<>(Comparator.comparingInt(o -> dist.getOrDefault(o, Integer.MAX_VALUE)));
    }

    public void search(V source) {

        dist.put(source, 0);

        queue.add(source);

        while (!queue.isEmpty()) {
            V u = queue.remove();

            for (V v : adjacent(u)) {
                int alt = dist.get(u) + length(u, v);

                if (alt >= 0 && alt < dist.getOrDefault(v, Integer.MAX_VALUE)) {
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
        Integer i = getGraph().getEdge(u, v).getLabel();
        return i == null ? 0 : i;
    }

    @Override
    public int compare(V o1, V o2) {
        if (!dist.containsKey(o1) || !dist.containsKey(o2)) {
            return Integer.MAX_VALUE;
        }
        return Integer.compare(dist.get(o1), dist.get(o2));
    }
}