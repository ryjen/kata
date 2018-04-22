package com.github.ryjen.kata.heap;

import java.util.Comparator;

/**
 * max heap implementation
 *
 * @param <E> the heap type
 */
public class MaxHeap<E extends Comparable<E>> extends Heap<E> {
    public MaxHeap(int capacity) {
        super(capacity, Comparator.naturalOrder());
    }

    public MaxHeap(MaxHeap<E> other) {
        super(other);
    }

    @Override
    public Heap<E> copy() {
        return new MaxHeap<>(this);
    }
}
