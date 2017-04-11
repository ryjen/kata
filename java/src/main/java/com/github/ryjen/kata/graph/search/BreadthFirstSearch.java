package com.github.ryjen.kata.graph.search;

import com.github.ryjen.kata.graph.Graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * A Breadth-First implementation of search
 */
public class BreadthFirstSearch<Vertex extends Comparable<Vertex>> extends Search<Vertex> {

    public BreadthFirstSearch(Graph<Vertex> graph, OnVisit<Vertex> callback) {
        super(graph, callback);
    }

    @Override
    public void search(Vertex v) {
        Queue<Vertex> queue = new LinkedList<>();
        queue.add(v);

        while (!queue.isEmpty()) {
            v = queue.remove();
            if (!isVisited(v)) {
                visit(v);
                callback(v);
                for (Vertex w : adjacent(v)) {
                    queue.add(w);
                }
            }
        }
    }
}
