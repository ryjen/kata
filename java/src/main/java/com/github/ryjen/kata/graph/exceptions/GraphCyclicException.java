package com.github.ryjen.kata.graph.exceptions;

/**
 * Created by ryan on 2017-04-01.
 */
public class GraphCyclicException extends Exception {

    private static final String CYCLIC = "Graph is cyclic";
    private static final String ACYCLIC = "Graph is acyclic";

    public GraphCyclicException() {
        this(true);
    }

    public GraphCyclicException(Throwable throwable) {
        this(true, throwable);
    }

    public GraphCyclicException(boolean value) {
        super(value ? CYCLIC : ACYCLIC);
    }

    public GraphCyclicException(boolean value, Throwable throwable) {
        super(value ? CYCLIC : ACYCLIC, throwable);
    }
}
