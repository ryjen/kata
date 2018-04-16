package com.github.ryjen.kata.graph.formatters;

import com.github.ryjen.kata.graph.Graph;

/**
 * a simple format that doesn't display vertices
 */
public class SimpleFormatter<E extends Comparable<E>, V extends Comparable<V>> implements Formatter {
    private final Graph<E, V> graph;

    /**
     * constructs a new simple formatter
     *
     * @param graph the graph to format
     */
    public SimpleFormatter(Graph<E, V> graph) {
        this.graph = graph;
    }

    @Override
    public void format(StringBuilder buf) {

        for (V a : graph.vertices()) {
            for (V b : graph.vertices()) {
                buf.append(graph.getEdgeOrEmpty(a, b));
                buf.append(' ');
            }
            buf.append('\n');
        }
    }
}
