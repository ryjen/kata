package com.github.ryjen.kata.graph;

import java.util.Collection;

/**
 * Created by ryan on 2017-04-10.
 */
public interface Vertexable<Vertex extends Comparable<Vertex>> {

    Vertexable<Vertex> addVertex(Vertex vertex);

    Vertexable<Vertex> addVertices(Collection<Vertex> vertices);

    boolean removeVertex(Vertex vertex);

    boolean containsVertex(Vertex vertex);

    Iterable<Vertex> adjacent(Vertex v);

    Iterable<Vertex> vertices();
}
