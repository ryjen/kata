package com.github.ryjen.kata.graph.model;

import com.github.ryjen.kata.graph.Vertexable;

import java.util.*;

/**
 * represents and edge between two vertices.
 * @see Factory
 */
public class Edge<T extends Comparable<T>, V extends Comparable<V>> implements Comparable<Edge<T, V>>, Vertexable<V> {

    private V from;
    private Set<V> endpoints;
    private final T label;

    /**
     * create an edge with a label
     * @param label
     */
    public Edge(T label) {
        this.label = label;
        this.endpoints = null;
    }

    public Edge() {
        this.label = null;
        this.endpoints = null;
    }

    public Edge(Edge<T,V> other) {
        this.label = other.label;
        this.endpoints = null;
    }

    /**
     * gets the weight of the edge
     *
     * @return the weight value
     */
    public T getLabel() {
        return label;
    }

    public Set<V> getEndpoints() {
        return Collections.unmodifiableSet(endpoints());
    }

    public Edge<T,V> setFrom(V vertex) {
        this.from = vertex;
        return this;
    }

    public V getFrom() {
        return from;
    }

    @Override
    public Vertexable<V> addVertex(V vertex) {
        endpoints().add(vertex);
        return this;
    }

    @Override
    public Vertexable<V> addVertices(Collection<V> vertices) {
        endpoints().addAll(vertices);
        return this;
    }

    @Override
    public boolean removeVertex(V vertex) {
        return endpoints().remove(vertex);
    }

    @Override
    public boolean containsVertex(V vertex) {
        return endpoints().contains(vertex);
    }

    @Override
    public Iterable<V> adjacent(V v) {
        return Collections.emptySet();
    }

    @Override
    public Iterable<V> vertices() {
        return Collections.unmodifiableSet(endpoints());
    }

    @Override
    public String toString() {
        if (label == null) {
            return "";
        }
        return String.valueOf(label);
    }

    @Override
    public int compareTo(Edge<T,V> o) {
        if (o == this) {
            return 0;
        }

        if (from == null || label == null) {
            return 1;
        }

        if (o.from == null || o.label == null) {
            return -1;
        }

        int t = from.compareTo(o.from);

        if (t != 0) {
            return t;
        }

        return label.compareTo(o.label);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Edge<?,?>) {
            Edge<?,?> o = (Edge<?,?>) obj;

            return Objects.equals(o.from, from) && Objects.equals(o.label, label);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        if (label != null && from != null) {
            return from.hashCode() + label.hashCode();
        }
        return super.hashCode();
    }

    private Set<V> endpoints() {
        if (endpoints == null) {
            endpoints = new HashSet<>();
        }
        return endpoints;
    }
}
