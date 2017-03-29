package com.github.ryjen.kata.graph.formatters;

import com.github.ryjen.kata.graph.Graph;

/**
 * a simple format that doesn't display vertices
 */
public class SimpleFormatter<Vertex extends Comparable<Vertex>> implements Formatter {
    final Graph<Vertex> graph;

    /**
     * constructs a new simple formatter
     *
     * @param graph the graph to format
     */
    public SimpleFormatter(Graph<Vertex> graph) {
        this.graph = graph;
    }

    @Override
    public void format(StringBuilder buf) {

        for (Vertex a : graph.vertices()) {
            for (Vertex b : graph.vertices()) {
                buf.append(graph.getEdgeOrDefault(a, b));
                buf.append(' ');
            }
            buf.append('\n');
        }
    }
}
