package com.github.ryjen.kata.heap.iterators;

import com.github.ryjen.kata.heap.Heap;

import java.util.Iterator;

/**
 * iterate a max heap copy
 */
public class HeapIterator<E extends Comparable<E>> implements Iterator<E>, Iterable<E> {

    private final Heap<E> heap;

    /**
     * default constructor makes a copy of the heap
     *
     * @param heap the heap to iterate
     */
    public HeapIterator(Heap<E> heap) {
        this.heap = heap.copy();
    }

    @Override
    public boolean hasNext() {
        return !heap.isEmpty();
    }

    @Override
    public E next() {
        return heap.remove();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return this;
    }
}