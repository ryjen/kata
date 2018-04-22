package com.github.ryjen.kata.heap;

import java.util.Comparator;

/**
 * minimum heap implementation
 *
 * @param <E> the heap type
 */
public class MinHeap<E extends Comparable<E>> extends Heap<E> {
    public MinHeap(int capacity) {
        super(capacity, Comparator.reverseOrder());
    }

    public MinHeap(MinHeap<E> other) {
        super(other);
    }

    @Override
    public Heap<E> copy() {
        return new MinHeap<>(this);
    }
}
