package com.github.ryjen.kata.graph.formatters;

import com.github.ryjen.kata.graph.Graph;

import java.util.StringJoiner;

/**
 * Created by ryan on 2017-03-29.
 */
public class ListFormatter<Vertex extends Comparable<Vertex>> implements Formatter {

    private Graph<Vertex> graph;

    public ListFormatter(Graph<Vertex> graph) {
        this.graph = graph;
    }

    public void format(StringBuilder buf) {
        for (Vertex v : graph.vertices()) {
            buf.append(v).append(graph.isDirected() ? " → " : " : ");
            StringJoiner sj = new StringJoiner(", ");
            for (Vertex a : graph.adjacent(v)) {
                sj.add(a.toString());
            }
            buf.append(sj).append('\n');
        }
    }

}
