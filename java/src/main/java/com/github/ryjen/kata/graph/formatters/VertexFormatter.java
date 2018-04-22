package com.github.ryjen.kata.graph.formatters;

import com.github.ryjen.kata.graph.Graph;
import com.github.ryjen.kata.graph.model.Edge;

/**
 * a formatter that displays vertices
 */
public class VertexFormatter<E extends Comparable<E>, V extends Comparable<V>> extends EdgeFormatter<E, V> {

    /**
     * construct a new graph vertex formatter
     *
     * @param graph the graph to format
     */
    public VertexFormatter(Graph<E, V> graph) {
        super(graph);
    }

    @Override
    public void format(StringBuilder buf) {
        header(buf);

        for (V a : graph.vertices()) {

            buf.append(formatWidth(a));

            buf.append(" │ ");

            for (V b : graph.vertices()) {
                buf.append(formatWidth(formatEdge(graph.getEdge(a, b))));
                buf.append(' ');
            }
            buf.append('\n');
        }
    }

    /**
     * formats a header of vertices
     *
     * @param buf the buffer to write to
     */
    private void header(StringBuilder buf) {
        for (int i = 0; i < width; i++) {
            buf.append(' ');
        }
        buf.append(" │ ");
        for (V v : graph.vertices()) {
            buf.append(formatWidth(v)).append(' ');
        }
        buf.append('\n');
        for (int i = 0; i < width; i++) {
            buf.append('─');
        }
        buf.append("─┼─");
        for (int i = 0; i < graph.size(); i++) {
            for (int j = 0; j < width; j++) {
                buf.append('─');
            }
            if (i + 1 < graph.size()) {
                buf.append('─');
            }
        }
        buf.append('─');
        buf.append('\n');
    }


    @Override
    protected String formatEdge(Edge<E, V> edge) {
        if (edge == null || edge.getLabel() == null) {
            return " ";
        }

        return String.valueOf(edge.getLabel());
    }
}
