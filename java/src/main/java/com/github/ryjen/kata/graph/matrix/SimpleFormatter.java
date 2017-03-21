package com.github.ryjen.kata.graph.matrix;

import com.github.ryjen.kata.graph.Formatter;

/**
 * a simple format that doesn't display vertices
 */
public class SimpleFormatter implements Formatter {
    final MatrixGraph<?> graph;

    /**
     * constructs a new simple formatter
     *
     * @param graph the graph to format
     */
    public SimpleFormatter(MatrixGraph<?> graph) {
        this.graph = graph;
    }

    @Override
    public void format(StringBuilder buf) {

        for (int row = 0; row < graph.size(); row++) {
            for (int col = 0; col < graph.size(); col++) {
                if (graph.isEdge(row, col)) {
                    buf.append(graph.getEdge(row, col));
                } else {
                    buf.append(graph.emptyEdge());
                }
                buf.append(' ');
            }
            buf.append('\n');
        }
    }
}
