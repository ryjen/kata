package com.github.ryjen.kata.graph.list;

import com.github.ryjen.kata.graph.model.Edge;


class Entry<T extends Comparable<T>> implements Comparable<Entry<T>> {
    private T vertex;
    private Edge edge;

    public Entry(T vertex, Edge edge) {
        this.vertex = vertex;
        this.edge = edge;
    }

    public T getVertex() {
        return vertex;
    }

    public Edge getEdge() {
        return edge;
    }

    @Override
    public int compareTo(Entry<T> o) {
        return vertex.compareTo(o.vertex);
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (o instanceof Entry) {
            Entry<T> entry = (Entry<T>) o;
            return vertex.equals(entry.vertex);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return vertex.hashCode();
    }

}
