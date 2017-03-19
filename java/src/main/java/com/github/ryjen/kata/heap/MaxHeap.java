package com.github.ryjen.kata.heap;

import java.util.Comparator;

public class MaxHeap<E extends Comparable<E>> extends Heap<E> {
    public MaxHeap(int capacity) {
        super(capacity, Comparator.naturalOrder());
    }

    MaxHeap(MaxHeap<E> other) {
        super(other);
    }

    @Override
    public Heap<E> copy() {
        return new MaxHeap<>(this);
    }
}
