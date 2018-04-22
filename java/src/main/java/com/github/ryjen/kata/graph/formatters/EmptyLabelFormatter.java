package com.github.ryjen.kata.graph.formatters;

import com.github.ryjen.kata.graph.Graph;
import com.github.ryjen.kata.graph.model.Edge;

public class EmptyLabelFormatter<E extends Comparable<E>, V extends Comparable<V>> extends VertexFormatter<E, V> {
    private static final char INVALID = '○';
    private final String empty;

    public EmptyLabelFormatter(Graph<E, V> graph, String empty) {
        super(graph);
        this.empty = empty;
    }

    public EmptyLabelFormatter(Graph<E, V> graph) {
        this(graph, String.valueOf(INVALID));
    }

    @Override
    protected Object getEdgeLabel(Edge<E, V> edge) {
        if (edge == null || edge.getLabel() == null) {
            return empty;
        }

        return edge.getLabel();
    }
}
