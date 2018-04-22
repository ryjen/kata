package com.github.ryjen.kata.graph.model;

import java.util.Objects;

/**
 * represents and edge between two vertices.
 */
public class Edge<T extends Comparable<T>, V extends Comparable<V>> implements Comparable<Edge<T, V>> {

    private V from;
    private V to;
    private final T label;

    /**
     * create an edge with a label
     *
     * @param label
     */
    public Edge(T label) {
        this.label = label;
        this.to = null;
    }

    public Edge() {
        this.label = null;
        this.to = null;
    }

    public Edge(Edge<T, V> other) {
        this.label = other.label;
        this.to = null;
    }

    /**
     * gets the weight of the edge
     *
     * @return the weight value
     */
    public T getLabel() {
        return label;
    }

    public Edge<T, V> setFrom(V vertex) {
        this.from = vertex;
        return this;
    }

    public V getFrom() {
        return from;
    }

    public Edge<T, V> setTo(V vertex) {
        this.to = vertex;
        return this;
    }

    public V getTo() {
        return to;
    }

    @Override
    public String toString() {
        if (label == null) {
            return "";
        }
        return String.valueOf(label);
    }

    @Override
    public int compareTo(Edge<T, V> o) {
        if (o == this) {
            return 0;
        }

        if (label == null) {
            return 1;
        }

        if (o.label == null) {
            return -1;
        }

        int t = label.compareTo(o.label);

        if (from != null && o.from != null) {
            t += from.compareTo(o.from);
        }

        if (to != null && o.to != null) {
            t += to.compareTo(o.to);
        }

        return t;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Edge<?, ?>) {
            Edge<?, ?> o = (Edge<?, ?>) obj;

            return Objects.equals(o.label, label) && Objects.equals(o.from, from) && Objects.equals(o.to, to);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        if (label != null) {
            int code = label.hashCode();

            if (from != null) {
                code += from.hashCode();
            }
            if (to != null) {
                code += to.hashCode();
            }
            return code;
        }
        return super.hashCode();
    }
}
