package com.github.ryjen.kata.graph;

import com.github.ryjen.kata.graph.formatters.Formatter;
import com.github.ryjen.kata.graph.model.Edge;
import com.github.ryjen.kata.graph.search.Ordering;
import com.github.ryjen.kata.graph.search.Search;

/**
 * Created by ryan on 2017-03-19.
 */
public interface Graph<Vertex extends Comparable<Vertex>> extends Cloneable {

    Graph<Vertex> clone();

    void addVertex(Vertex vertex);

    boolean removeVertex(Vertex vertex);

    void addVertices(Vertex... list);

    void addEdge(Vertex a, Vertex b);

    void addEdge(Vertex a, Vertex b, Edge edge);

    boolean isEdge(Vertex a, Vertex b);

    Edge getEdge(Vertex a, Vertex b);

    Edge getEdgeOrDefault(Vertex a, Vertex b);

    boolean removeEdge(Vertex a, Vertex b);

    int size();

    void dfs(Vertex start, Search.OnVisit<Vertex> callback, Ordering ordering);

    void bfs(Vertex start, Search.OnVisit<Vertex> callback);

    Iterable<Vertex> adjacent(Vertex v);

    Iterable<Vertex> vertices();

    Iterable<Edge> edges();

    int degree(Vertex vertex);

    int inDegree(Vertex vertex);

    int outDegree(Vertex vertex);

    boolean isDirected();

    boolean isCyclic();

    String toString(Formatter formatter);

    boolean isConnected();
}
