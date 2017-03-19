package com.github.ryjen.kata.graph.formatters;

import com.github.ryjen.kata.graph.Graph;
import com.github.ryjen.kata.graph.model.Edge;

/**
 * a simple format that doesn't display vertices
 */
public class GraphSimpleFormatter implements Formatter {
    final Graph<?> graph;

    /**
     * constructs a new simple formatter
     *
     * @param graph the graph to format
     */
    public GraphSimpleFormatter(Graph<?> graph) {
        this.graph = graph;
    }

    @Override
    public void format(StringBuilder buf) {

        for (Iterable<Edge> row : graph.edges()) {
            for (Edge edge : row) {
                buf.append(edge).append(' ');
            }
            buf.append('\n');
        }
    }
}
