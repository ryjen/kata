package com.github.ryjen.kata.graph.search;

import com.github.ryjen.kata.graph.Graph;

/**
 * A Depth-First implementation of search
 */
public class DepthFirstSearch<Vertex extends Comparable<Vertex>> extends Search<Vertex> {

    public DepthFirstSearch(Graph<Vertex> graph, OnVisit<Vertex> callback) {
        super(graph, callback);
    }

    protected void search(Vertex v) {
        visit(v);

        for (Vertex w : adjacent(v)) {
            if (!isVisited(w)) {
                search(w);
            }
        }
    }
}
