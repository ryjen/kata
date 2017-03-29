package com.github.ryjen.kata.graph;

import com.github.ryjen.kata.graph.formatters.Formatter;
import com.github.ryjen.kata.graph.model.Edge;
import com.github.ryjen.kata.graph.search.Search;

/**
 * Created by ryan on 2017-03-19.
 */
public interface Graph<Vertex extends Comparable<Vertex>> {
    void addVertex(Vertex vertex);

    boolean removeVertex(Vertex vertex);

    void addVertices(Vertex... list);

    int size();

    void addEdge(Vertex v, Vertex u);

    void dfs(Search.OnVisit<Vertex> callback);

    void bfs(Search.OnVisit<Vertex> callback);

    Iterable<Vertex> adjacent(Vertex v);

    Iterable<Vertex> vertices();

    Iterable<Edge> edges();

    int degree(Vertex vertex);

    int inDegree(Vertex vertex);

    int outDegree(Vertex vertex);

    boolean isEdge(Vertex a, Vertex b);

    Edge getEdge(Vertex a, Vertex b);

    Edge getEdgeOrDefault(Vertex a, Vertex b);

    boolean isDirected();

    String toString(Formatter formatter);
}
