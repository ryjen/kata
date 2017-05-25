package com.github.ryjen.kata.graph.model;

/**
 * Created by ryan on 2017-04-19.
 */
public class Connection<Vertex extends Comparable<Vertex>> implements Comparable<Connection<Vertex>> {
    private final Vertex from;
    private final Vertex to;
    private final Edge edge;

    public Connection(Vertex from, Vertex to, Edge edge) {
        this.from = from;
        this.to = to;
        this.edge = edge;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }

    public Edge getEdge() {
        return edge;
    }

    @Override
    public int compareTo(Connection<Vertex> o) {
        int rval = to.compareTo(o.to);
        return rval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Connection<?> that = (Connection<?>) o;

        return to.equals(that.to);
    }

    @Override
    public int hashCode() {
        int result = to.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s -%s- %s", from, edge, to);
    }
}
