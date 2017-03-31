package com.github.ryjen.kata.heap.iterators;

import com.github.ryjen.kata.heap.Heap;

import java.util.Iterator;

/**
 * iterate a max heap copy
 */
public class HeapIterator<E extends Comparable<E>> implements Iterator<E>, Iterable<E> {

    private Heap<E> heap;

    public HeapIterator(Heap<E> heap) {
        this.heap = heap.copy();
    }

    public boolean hasNext() {
        return !heap.isEmpty();
    }

    public E next() {
        return heap.remove();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return this;
    }
}