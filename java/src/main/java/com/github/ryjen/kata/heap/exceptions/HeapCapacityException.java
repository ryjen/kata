package com.github.ryjen.kata.heap.exceptions;

/**
 * Created by ryan on 2017-03-19.
 */
public class HeapCapacityException extends RuntimeException {
    private static final String MESSAGE = "Heap at capacity";

    public HeapCapacityException() {
        super(MESSAGE);
    }

    public HeapCapacityException(Throwable throwable) {
        super(MESSAGE, throwable);
    }
}
