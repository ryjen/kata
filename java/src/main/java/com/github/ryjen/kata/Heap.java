package com.github.ryjen.kata;

import java.lang.reflect.Array;
import java.util.*;

/**
 * A priority queue implementation using a max heap array
 * @param <T>
 */
public abstract class Heap<E extends Comparable<E>> implements Queue<E>, Cloneable {

    private final List<E> values;
    private final Comparator comparator;
    private final int capacity;

    public static class Max<E extends Comparable<E>> extends Heap<E> {
        public Max(int capacity) {
            super(capacity, Comparator.naturalOrder());
        }
    }

    public static class Min<E extends Comparable<E>> extends Heap<E> {
        public Min(int capacity) {
            super(capacity, Comparator.reverseOrder());
        }
    }

    private Heap(int capacity, Comparator<E> comparator) {
        this.values = new ArrayList<>(capacity);
        this.comparator = comparator;
        this.capacity = capacity;
    }

    private Heap(Heap<E> other) {
        this.values = new ArrayList<>(other.values);
        this.comparator = other.comparator;
        this.capacity = other.capacity;
    }

    private static int getParent(int index) {
        if (index < 0) {
            return 0;
        }
        return (index - 1) / 2;
    }

    private static int getLeftChild(int index) {
        if (index < 0) {
            return 0;
        }
        return (2 * index) + 1;
    }

    private static int getRightChild(int index) {
        return getLeftChild(index) + 1;
    }

    private void swap(int a, int b) {
        assert a >= 0 && a < values.size();
        assert b >= 0 && b < values.size();
        E temp = values.get(a);
        values.set(a, values.get(b));
        values.set(b, temp);
    }

    private boolean compare(int a, int b) {
        assert a >= 0 && a < values.size();
        assert b >= 0 && b < values.size();
        return compare(values.get(a), values.get(b));
    }

    private boolean compare(E a, E b) {
        assert a != null;
        assert b != null;
        if (this.comparator != null) {
            return comparator.compare(a, b) < 0;
        } else {
            return a.compareTo(b) < 0;
        }
    }

    public boolean add(E value) {
        assert value != null;
        // add if less then capacity
        if (size() >= capacity) {
            throw new IllegalStateException();
        }

        return heapify(value);
    }


    private E remove(int position, NullHandler<E> error) {
        if (values.isEmpty()) {
            return error.apply();
        }
        swap(position, values.size() - 1);
        E removeNode = values.remove(values.size() - 1);
        bubbleDown(position, values.size() - 1);
        return removeNode;
    }

    @Override
    public boolean remove(Object o) {

        int index = values.indexOf(o);

        if (index == -1) {
            return false;
        }
        return remove(index, () -> null) != null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return values.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            if (!offer(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object o : c) {
            if (!remove(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Collection<E> others = new ArrayList<>();
        for (E e : this) {
            if (!c.contains(e)) {
                others.add(e);
            }
        }
        return removeAll(others);
    }

    @Override
    public void clear() {
        values.clear();
    }

    public boolean offer(E value) {
        assert value != null;
        if (size() < capacity) {
            return heapify(value);
        } else if (compare(peek(), value)) {
            boolean success  =  heapify(value);
            values.remove(values.size()-1);
            return success;
        }
        return false;
    }

    /**
     * add a value and bubble it up
     *
     * @param value
     */
    private boolean heapify(E value) {
        assert value != null;
        // append value
        if (values.add(value)) {
            // bubble it up
            int start = getParent(values.size() - 1);
            while (start >= 0) {
                bubbleUp(start--, values.size() - 1);
            }

            return true;
        }
        return false;
    }

    /**
     * bubble a value from the bottom of the heap to its correct location
     *
     * @param start
     * @param end
     */
    private void bubbleUp(int start, int end) {
        int child = end;
        while (child >= start) {
            int parent = getParent(child);
            if (compare(parent, child)) {
                swap(parent, child);
                child = parent;
            } else {
                return;
            }
        }
    }

    /**
     * bubble down a value to its correct position
     *
     * @param start
     * @param end
     */
    private void bubbleDown(int start, int end) {
        int root = start;

        int child = getLeftChild(root);

        while (child <= end) {
            int swap = root;

            if (compare(swap, child)) {
                swap = child;
            }
            if (child + 1 <= end && compare(swap, child + 1)) {
                swap = child + 1;
            }
            if (swap == root) {
                return;
            }
            swap(root, swap);
            root = swap;
            child = getLeftChild(root);
        }
    }

    public E remove() {
        return remove(0, () -> {
            throw new NoSuchElementException();
        });
    }

    public E poll() {
        return remove(0, () -> null);
    }

    public int size() {
        return values.size();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return values.contains(o);
    }

    private E get(int position, NullHandler<E> error) {
        if (values.isEmpty()) {
            return error.apply();
        }
        int parent = getParent(position);
        int child = getLeftChild(parent);
        return values.get(child);
    }

    public E element() {
        return get(0, () -> {
            throw new NoSuchElementException();
        });
    }

    public E peek() {
        return get(0, () -> null);
    }

    @Override
    public Iterator iterator() {
        return new Iterator(this);
    }

    @Override
    public Object[] toArray() {
        return toArray(new Object[0]);
    }

    @Override
    public <T> T[] toArray(T[] array) {

        if (array.length < size()) {
            array = (T[]) Array.newInstance(array.getClass().getComponentType(), size());
        } else if (array.length > size()) {
            // If array is to large, set the first unassigned element to null
            array[size()] = null;
        }

        int i = 0;
        for (E e : this) {
            array[i++] = (T) e;
        }
        return array;
    }

    /**
     * iterate a max heap copy
     */
    public class Iterator implements java.util.Iterator<E> {

        Heap<E> heap;

        public Iterator(Heap<E> heap) {
            this.heap = new Heap<E>(heap) { };
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
    }

    private interface NullHandler<E> {
        E apply();
    }
}
