package com.github.ryjen.kata.graph.exceptions;

/**
 * thrown when the number of vertices reaches the graph size
 */
public class VertexLimitException extends RuntimeException {
    private static final String MESSAGE = "No space to add vertex in graph";

    public VertexLimitException() {
        super(MESSAGE);
    }

    public VertexLimitException(Throwable throwable) {
        super(MESSAGE, throwable);
    }
}
