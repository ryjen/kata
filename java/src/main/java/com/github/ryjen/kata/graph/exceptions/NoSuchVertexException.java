package com.github.ryjen.kata.graph.exceptions;

/**
 * thrown when a vertex is not found
 */
public class NoSuchVertexException extends RuntimeException {

    private static final String MESSAGE = "Vertex not found in graph";

    public NoSuchVertexException() {
        super(MESSAGE);
    }

    public NoSuchVertexException(Throwable throwable) {
        super(MESSAGE, throwable);
    }
}
