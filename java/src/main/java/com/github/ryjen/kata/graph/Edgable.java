package com.github.ryjen.kata.graph;

import com.github.ryjen.kata.graph.model.Edge;

/**
 * Created by ryan on 2017-04-10.
 */
public interface Edgable<Vertex extends Comparable<Vertex>> {

    void addEdge(Vertex a, Vertex b, Edge edge);

    boolean isEdge(Vertex a, Vertex b);

    Edge getEdge(Vertex a, Vertex b);

    boolean removeEdge(Vertex a, Vertex b);

    Iterable<Edge> edges();

    Iterable<Edge> edges(Vertex v);

    int numberOfEdges();
}
