package com.github.ryjen.kata.graph.exceptions;

/**
 * Created by ryan on 2017-04-01.
 */
public class GraphIsCyclicException extends Exception {

    private static final String MESSAGE = "Graph is cyclic";

    public GraphIsCyclicException() {
        super(MESSAGE);
    }

    public GraphIsCyclicException(Throwable throwable) {
        super(MESSAGE, throwable);
    }
}
