package com.github.ryjen.kata.graph;

import java.util.Collection;

/**
 * An interface to support vertices on an entity
 *
 * @param <Vertex> vertex type
 */
public interface Vertexable<Vertex extends Comparable<Vertex>> {

    Vertexable<Vertex> addVertex(Vertex vertex);

    Vertexable<Vertex> addVertices(Collection<Vertex> vertices);

    boolean removeVertex(Vertex vertex);

    boolean containsVertex(Vertex vertex);

    Iterable<Vertex> adjacent(Vertex v);

    Iterable<Vertex> vertices();
}
