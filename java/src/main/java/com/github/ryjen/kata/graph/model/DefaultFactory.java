package com.github.ryjen.kata.graph.model;

import com.github.ryjen.kata.graph.Graphable;

public class DefaultFactory<E extends Comparable<E>, V extends Comparable<V>> implements Factory<E,V> {
    private final Edge<E,V> empty;
    private final Graphable<E,V> impl;

    public DefaultFactory(Graphable<E, V> impl) {
        this.empty = new Edge<>();
        this.impl = impl;
    }

    @Override
    public Graphable<E, V> getImplementation() {
        return impl;
    }

    @Override
    public Edge<E,V> getDefaultEdge() {
        return new Edge<>();
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
