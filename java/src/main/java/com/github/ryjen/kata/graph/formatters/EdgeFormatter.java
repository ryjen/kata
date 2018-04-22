package com.github.ryjen.kata.graph.formatters;


import com.github.ryjen.kata.graph.Graph;
import com.github.ryjen.kata.graph.model.Edge;

import java.util.OptionalInt;
import java.util.stream.StreamSupport;

public abstract class EdgeFormatter<E extends Comparable<E>, V extends Comparable<V>> implements Formatter {
    protected final int width;
    protected final Graph<E, V> graph;

    protected EdgeFormatter(Graph<E, V> graph) {
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
     * @return a string representation of the object that is at maximum width characters wide
     */
    protected String format(Object object) {
        return String.format("%-" + width + "s", object);
    }

    protected abstract Object getEdgeLabel(Edge<E, V> edge);
}
