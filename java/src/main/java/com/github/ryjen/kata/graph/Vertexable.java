package com.github.ryjen.kata.graph;

/**
 * Created by ryan on 2017-04-10.
 */
public interface Vertexable<Vertex extends Comparable<Vertex>> {

    void addVertex(Vertex vertex);

    boolean removeVertex(Vertex vertex);

    Iterable<Vertex> adjacent(Vertex v);

    Iterable<Vertex> vertices();
}
