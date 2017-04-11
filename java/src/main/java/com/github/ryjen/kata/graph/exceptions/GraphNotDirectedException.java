package com.github.ryjen.kata.graph.exceptions;

/**
 * Created by ryan on 2017-04-01.
 */
public class GraphNotDirectedException extends Exception {

    private static final String MESSAGE = "Graph must directed";

    public GraphNotDirectedException() {
        super(MESSAGE);
    }

    public GraphNotDirectedException(Throwable throwable) {
        super(MESSAGE, throwable);
    }
}
