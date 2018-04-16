package com.github.ryjen.kata.graph.formatters;

import com.github.ryjen.kata.graph.Graph;

import java.util.StringJoiner;

/**
 * Created by ryan on 2017-03-29.
 */
public class ListFormatter<E extends Comparable<E>, V extends Comparable<V>> implements Formatter {

    private final Graph<E, V> graph;

    public ListFormatter(Graph<E, V> graph) {
        this.graph = graph;
    }

    public void format(StringBuilder buf) {
        for (V v : graph.vertices()) {
            buf.append(v).append(graph.isDirected() ? " → " : " : ");
            StringJoiner sj = new StringJoiner(", ");
            for (V a : graph.adjacent(v)) {
                sj.add(a.toString());
            }
            buf.append(sj).append('\n');
        }
    }

}
