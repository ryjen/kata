package com.github.ryjen.kata.graph.model;

import com.github.ryjen.kata.graph.Graphable;

public class SimpleFactory<E extends Comparable<E>, V extends Comparable<V>> implements Factory<E,V> {
    private final E label;
    private final Edge<E,V> empty;
    private final Graphable<E,V> impl;

    public SimpleFactory(Graphable<E, V> impl, E label, E empty) {
        this.label = label;
        this.empty = new Edge<>(empty);
        this.impl = impl;
    }

    @Override
    public Graphable<E, V> getImplementation() {
        return impl;
    }

    @Override
    public Edge<E,V> getDefaultEdge() {
        return new Edge<>(label);
    }

    @Override
    public Edge<E, V> getEmptyEdge() {
        return empty;
    }

    @Override
    public boolean isEmptyEdge(Edge<E, V> other) {
        return empty == other;
    }
}
