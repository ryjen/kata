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
    protected void search(Vertex v) {
        visit(v);

        Queue<Vertex> queue = new LinkedList<>();
        queue.add(v);

        while (!queue.isEmpty()) {
            for (Vertex w : adjacent(queue.peek())) {
                if (!isVisited(w)) {
                    visit(w);

                    queue.add(w);
                }
            }
            queue.remove();
        }
    }
}
