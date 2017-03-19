package com.github.ryjen.kata.graph.model;

import java.lang.reflect.Array;
import java.util.Comparator;

public class TypeFactory<V extends Comparable<V>> implements Factory<V> {
    /**
     * single reference to an empty edge
     */
    private static final Edge EMPTY_EDGE = new Edge() {
        @Override
        public String display() {
            return "○";
        }

        @Override
        public int getWeight() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    };
    /**
     * single reference to a filled edge
     */
    private static final Edge FILLED_EDGE = new Edge() {

        @Override
        public String display() {
            return "●";
        }

        @Override
        public int getWeight() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    };
    Class<V> type;

    public TypeFactory(Class<V> type) {
        this.type = type;
    }

    @Override
    public Edge createEdge() {
        return FILLED_EDGE;
    }

    @Override
    public Edge emptyEdge() {
        return EMPTY_EDGE;
    }

    @Override
    public V[] createVertices(int size) {
        return (V[]) Array.newInstance(type, size);
    }

    @Override
    public Comparator<V> createComparator() {
        return null;
    }

}
