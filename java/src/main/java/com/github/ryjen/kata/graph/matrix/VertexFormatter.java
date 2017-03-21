package com.github.ryjen.kata.graph.matrix;

import com.github.ryjen.kata.graph.Formatter;

import java.util.OptionalInt;
import java.util.stream.StreamSupport;

/**
 * a formatter that displays vertices
 */
public class VertexFormatter<Vertex extends Comparable<Vertex>> implements Formatter {
    final int width;
    final MatrixGraph<Vertex> graph;

    /**
     * construct a new graph vertex formatter
     *
     * @param graph the graph to format
     */
    public VertexFormatter(MatrixGraph<Vertex> graph) {
        assert graph != null;
        this.graph = graph;
        OptionalInt vertexWidth = StreamSupport.stream(graph.vertices().spliterator(), false)
                .mapToInt(value -> String.valueOf(value).length()).max();
        OptionalInt edgeWidth = StreamSupport.stream(graph.edges().spliterator(), false)
                .mapToInt(edge -> String.valueOf(edge).length()).max();
        width = Math.max(vertexWidth.getAsInt(), edgeWidth.getAsInt());
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

        for (int row = 0; row < graph.size(); row++) {

            buf.append(format(graph.getVertex(row)));

            buf.append(" │ ");

            for (int col = 0; col < graph.size(); col++) {
                if (graph.isEdge(row, col)) {
                    buf.append(format(graph.getEdge(row, col)));
                } else {
                    buf.append(format(graph.emptyEdge()));
                }
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
        for (int i = 0; i < graph.size(); i++) {
            buf.append(format(graph.getVertex(i))).append(' ');
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
