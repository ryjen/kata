package com.github.ryjen.kata.graph;

import com.github.ryjen.kata.graph.model.Edge;
import com.github.ryjen.kata.graph.search.Search;

/**
 * Created by ryan on 2017-03-19.
 */
public interface Graph<Vertex extends Comparable<Vertex>> {
    void addVertex(Vertex vertex);

    boolean removeVertex(Vertex vertex);

    int size();

    void addEdge(Vertex v, Vertex u);

    void dfs(Search.OnVisit<Vertex> callback);

    void bfs(Search.OnVisit<Vertex> callback);

    Iterable<Vertex> adjacent(Vertex v);

    Iterable<Vertex> vertices();

    Iterable<Edge> edges();

    int degree(Vertex vertex);

    boolean isDirected();
}
