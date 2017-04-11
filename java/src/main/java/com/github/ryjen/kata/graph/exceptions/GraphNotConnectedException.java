package com.github.ryjen.kata.graph.exceptions;

/**
 * Created by ryan on 2017-04-01.
 */
public class GraphNotConnectedException extends Exception {

    private static final String MESSAGE = "Graph must be connected";

    public GraphNotConnectedException() {
        super(MESSAGE);
    }

    public GraphNotConnectedException(Throwable throwable) {
        super(MESSAGE, throwable);
    }
}
