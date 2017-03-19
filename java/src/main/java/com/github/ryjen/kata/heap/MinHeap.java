package com.github.ryjen.kata.heap;

import java.util.Comparator;

public class MinHeap<E extends Comparable<E>> extends Heap<E> {
    public MinHeap(int capacity) {
        super(capacity, Comparator.reverseOrder());
    }

    MinHeap(MinHeap<E> other) {
        super(other);
    }

    @Override
    public Heap<E> copy() {
        return new MinHeap<>(this);
    }
}
