package com.github.ryjen.kata.graph.formatters;

import com.github.ryjen.kata.graph.Graph;
import com.github.ryjen.kata.graph.model.Edge;

/**
 * a symbol formatter that displays a symbol for valid and invalid edges
 *
 * @param <E> the edge type
 * @param <V> the vertex type
 */
public class SymbolFormatter<E extends Comparable<E>, V extends Comparable<V>> extends VertexFormatter<E, V> {
    private static final char VALID = '●';
    private static final char INVALID = '○';

    private final String valid;
    private final String invalid;

    public SymbolFormatter(Graph<E, V> graph, String val, String inval) {
        super(graph);
        this.valid = val;
        this.invalid = inval;
    }

    public SymbolFormatter(Graph<E, V> graph) {
        this(graph, String.valueOf(VALID), String.valueOf(INVALID));
    }

    @Override
    public String formatEdge(Edge<E, V> edge) {
        if (edge == null || edge.getLabel() == null) {
            return invalid;
        }

        return valid;
    }
}
