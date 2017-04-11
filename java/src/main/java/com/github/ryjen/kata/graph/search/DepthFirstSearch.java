package com.github.ryjen.kata.graph.search;

import com.github.ryjen.kata.graph.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * A Depth-First implementation of search
 */
public class DepthFirstSearch<Vertex extends Comparable<Vertex>> extends Search<Vertex> {

    Ordering ordering;

    public DepthFirstSearch(Graph<Vertex> graph, OnVisit<Vertex> callback) {
        this(graph, callback, Ordering.Pre);
    }

    public DepthFirstSearch(Graph<Vertex> graph, OnVisit<Vertex> callback, Ordering ordering) {
        super(graph, callback);
        this.ordering = ordering;
    }

    @Override
    public void search(Vertex v) {
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

    /**
     * starts the search
     */
    private void searchPreOrder(Vertex v) {
        Stack<Vertex> stack = new Stack<>();
        stack.add(v);

        while (!stack.isEmpty()) {

            v = stack.pop();

            if (isVisited(v)) {
                continue;
            }

            visit(v);

            callback(v);

            for (Vertex w : adjacent(v)) {
                stack.push(w);
            }
        }
    }

    private void searchPostOrder(Vertex v) {

        if (isVisited(v)) {
            return;
        }

        visit(v);

        for (Vertex w : adjacent(v)) {
            searchPostOrder(w);
        }

        callback(v);
    }


    private void searchReversePostOrder(Vertex v, List<Vertex> scratch) {
        if (isVisited(v)) {
            return;
        }

        visit(v);

        for (Vertex w : adjacent(v)) {
            searchReversePostOrder(w, scratch);
        }

        scratch.add(v);
    }

    private void searchReversePostOrder(Vertex v) {

        List<Vertex> scratch = new ArrayList<>();

        searchReversePostOrder(v, scratch);

        Collections.reverse(scratch);

        for (Vertex w : scratch) {
            callback(w);
        }
    }
}
