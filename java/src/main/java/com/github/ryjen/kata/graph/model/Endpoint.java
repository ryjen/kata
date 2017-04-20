package com.github.ryjen.kata.graph.model;

/**
 * Created by ryan on 2017-04-19.
 */
public class Endpoint<Vertex extends Comparable<Vertex>> implements Comparable<Endpoint<Vertex>> {
    private final Vertex vertex;
    private final Edge edge;

    public Endpoint(Vertex vertex, Edge edge) {
        this.vertex = vertex;
        this.edge = edge;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public Edge getEdge() {
        return edge;
    }

    @Override
    public int compareTo(Endpoint<Vertex> o) {
        return vertex.compareTo(o.vertex);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Endpoint) {
            Endpoint<Vertex> endpoint = (Endpoint<Vertex>) obj;
            return vertex.equals(endpoint.vertex);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return vertex.hashCode();
    }
}
