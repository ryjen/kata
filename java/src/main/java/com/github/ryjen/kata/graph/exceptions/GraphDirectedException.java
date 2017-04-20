package com.github.ryjen.kata.graph.exceptions;

/**
 * Created by ryan on 2017-04-01.
 */
public class GraphDirectedException extends Exception {

    private static final String DIRECTED = "Graph is directed";
    private static final String UNDIRECTED = "Graph is undirected";

    public GraphDirectedException() {
        this(true);
    }

    public GraphDirectedException(Throwable throwable) {
        this(true, throwable);
    }


    public GraphDirectedException(boolean directed) {
        super(directed ? DIRECTED : UNDIRECTED);
    }

    public GraphDirectedException(boolean directed, Throwable throwable) {
        super(directed ? DIRECTED : UNDIRECTED, throwable);
    }
}
