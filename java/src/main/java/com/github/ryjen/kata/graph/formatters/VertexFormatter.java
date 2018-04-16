package com.github.ryjen.kata.graph.formatters;

import com.github.ryjen.kata.graph.Graph;

import java.util.OptionalInt;
import java.util.stream.StreamSupport;

/**
 * a formatter that displays vertices
 */
public class VertexFormatter<E extends Comparable<E>, V extends Comparable<V>> implements Formatter {
    private final int width;
    private final Graph<E, V> graph;

    /**
     * construct a new graph vertex formatter
     *
     * @param graph the graph to format
     */
    public VertexFormatter(Graph<E, V> graph) {
        assert graph != null;
        this.graph = graph;
        OptionalInt vertexWidth = StreamSupport.stream(graph.vertices().spliterator(), false)
                .mapToInt(value -> String.valueOf(value).length()).max();
        OptionalInt edgeWidth = StreamSupport.stream(graph.edges().spliterator(), false)
                .mapToInt(edge -> String.valueOf(edge).length()).max();
        width = Math.max(vertexWidth.isPresent() ? vertexWidth.getAsInt() : 1,
                edgeWidth.isPresent() ? edgeWidth.getAsInt() : 1);
    }

    /**
     * formats an object to string with the calculated width
     *
     * @param object the object to format
     * @param <T>    the type of object
     * @return a string representation of the object that is at maximum width characters wide
     */
    private <T> String format(T object) {
        return String.format("%-" + width + "s", object);
    }

    @Override
    public void format(StringBuilder buf) {
        header(buf);

        for (V a : graph.vertices()) {

            buf.append(format(a));

            buf.append(" │ ");

            for (V b : graph.vertices()) {
                buf.append(format(graph.getEdgeOrEmpty(a, b)));
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
            buf.append(format(v)).append(' ');
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
}
