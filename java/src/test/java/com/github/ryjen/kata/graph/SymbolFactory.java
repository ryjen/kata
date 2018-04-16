package com.github.ryjen.kata.graph;

import com.github.ryjen.kata.graph.model.Edge;
import com.github.ryjen.kata.graph.model.Factory;

public class SymbolFactory<V extends Comparable<V>> implements Factory<Character,V> {
    private static final char VALID = '●';
    private static final char INVALID = '○';
    private final Edge<Character,V> EMPTY = new Edge<>(INVALID);
    private final Graphable<Character,V> impl;

    public SymbolFactory(Graphable<Character,V> impl) {
        this.impl = impl;
    }

    @Override
    public Graphable<Character, V> getImplementation() {
        return impl;
    }

    @Override
    public Edge<Character, V> getDefaultEdge() {
        return new Edge<>(VALID);
    }

    @Override
    public boolean isEmptyEdge(Edge<Character, V> other) {
        return EMPTY == other;
    }

    @Override
    public Edge<Character, V> getEmptyEdge() {
        return EMPTY;
    }
}
