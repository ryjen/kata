package com.github.ryjen.kata.graph.model;

import java.util.Comparator;
import java.util.List;

/**
 * Created by ryanjennings on 2017-03-20.
 */
public class DefaultFactory<Vertex extends Comparable<Vertex>> implements Factory<Vertex> {

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

    @Override
    public Edge createEdge() {
        return FILLED_EDGE;
    }

    @Override
    public Edge createEdge(int weight) {
        return new WeightedEdge(weight);
    }

    @Override
    public Edge emptyEdge() {
        return EMPTY_EDGE;
    }

    @Override
    public List<Vertex> initialVertices() {
        return null;
    }

    @Override
    public Comparator<Vertex> createComparator() {
        return null;
    }
}
