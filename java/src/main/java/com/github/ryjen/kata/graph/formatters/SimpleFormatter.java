package com.github.ryjen.kata.graph.formatters;

import com.github.ryjen.kata.graph.Graph;
import com.github.ryjen.kata.graph.model.Edge;

/**
 * a simple format that doesn't display vertices and empty edges
 */
public class SimpleFormatter<E extends Comparable<E>, V extends Comparable<V>> extends EdgeFormatter<E, V> {

    /**
     * constructs a new simple formatter
     *
     * @param graph the graph to format
     */
    public SimpleFormatter(Graph<E, V> graph) {
        super(graph);
    }

    @Override
    public void format(StringBuilder buf) {

        for (V a : graph.vertices()) {
            for (V b : graph.vertices()) {
                buf.append(formatWidth(formatEdge(graph.getEdge(a, b))));
                buf.append(' ');
            }
            buf.append('\n');
        }
    }

    @Override
    protected String formatEdge(Edge<E, V> edge) {
        if (edge == null || edge.getLabel() == null) {
            return " ";
        }

        return String.valueOf(edge.getLabel());
    }
}
