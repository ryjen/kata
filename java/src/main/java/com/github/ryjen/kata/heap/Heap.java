package com.github.ryjen.kata.heap;

import com.github.ryjen.kata.heap.exceptions.HeapCapacityException;
import com.github.ryjen.kata.heap.iterators.HeapIterator;

import java.lang.reflect.Array;
import java.util.*;

/**
 * A dynamic array heap implementation
 *
 * @param <E> the type of elements in the heap
 */
public class Heap<E extends Comparable<E>> implements Queue<E> {

    private final List<E> values;
    private final Comparator<E> comparator;
    private final int capacity;

    /**
     * constructs a new heap
     *
     * @param capacity   the capacity of the heap
     * @param comparator how to compare heap elements
     */
    public Heap(int capacity, Comparator<E> comparator) {
        this.values = new ArrayList<>(capacity);
        this.comparator = comparator;
        this.capacity = capacity;
    }

    /**
     * copy constructor
     *
     * @param other the heap to copy from
     */
    public Heap(Heap<E> other) {
        this.values = new ArrayList<>(other.values);
        this.comparator = other.comparator;
        this.capacity = other.capacity;
    }

    /**
     * copy an instance of a heap
     *
     * @return a heap copy of the type
     */
    public Heap<E> copy() {
        return new Heap<>(this);
    }

    /**
     * swap two elements in the heap
     *
     * @param a the first element
     * @param b the second element
     */
    private void swap(int a, int b) {
        assert a >= 0 && a < values.size();
        assert b >= 0 && b < values.size();
        E temp = values.get(a);
        values.set(a, values.get(b));
        values.set(b, temp);
    }

    /**
     * compare two elements in the heap
     *
     * @param a the first element
     * @param b the second element
     * @return true if a is less than b
     */
    private boolean isMin(int a, int b) {
        assert a >= 0 && a < values.size();
        assert b >= 0 && b < values.size();
        return isMin(values.get(a), values.get(b));
    }

    /**
     * compare two elements in the heap
     *
     * @param a the first element
     * @param b the second element
     * @return true if a is less than b
     */
    private boolean isMin(E a, E b) {
        assert a != null;
        assert b != null;
        if (this.comparator != null) {
            return comparator.compare(a, b) < 0;
        } else {
            return a.compareTo(b) < 0;
        }
    }

    /**
     * adds an element to the heap
     *
     * @param value the element to add
     * @return true if added
     * @throws HeapCapacityException if the heap is at capacity already
     */
    public boolean add(E value) {
        assert value != null;
        // add if less then capacity
        if (size() >= capacity) {
            throw new HeapCapacityException();
        }

        return heapify(value);
    }

    /**
     * removes an element at a position
     *
     * @param position    the position to remove
     * @param nullOnEmpty returns null if set to true and heap is empty
     * @return the element removed or the error handler return value
     * @throws NoSuchElementException if the error handler does
     */
    private E remove(int position, boolean nullOnEmpty) {
        if (values.isEmpty()) {
            if (nullOnEmpty) {
                return null;
            }
            throw new NoSuchElementException();
        }
        swap(position, values.size() - 1);
        E removeNode = values.remove(values.size() - 1);
        bubbleDown(position, values.size() - 1);
        return removeNode;
    }

    /**
     * removes an element from the heap
     *
     * @param o the element
     * @return true if removed or null
     */
    @Override
    public boolean remove(Object o) {

        int index = values.indexOf(o);

        return index != -1 && remove(index, true) != null;
    }

    /**
     * test if all elements in a collection are contained in the heap
     *
     * @param c the collection of elements
     * @return true if all of c exist in the heap
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return values.containsAll(c);
    }

    /**
     * adds a collection of elements to the heap
     *
     * @param c the collection of elements
     * @return true if all elements were added
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            if (!offer(e)) {
                return false;
            }
        }
        return true;
    }

    /**
     * remove all elements in a collection from the heap
     *
     * @param c the collection of elements
     * @return true if all elements were removed
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object o : c) {
            if (!remove(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * remove all elements not contained in a collection
     *
     * @param c the collection of elements to keep
     * @return true if all other elements were removed
     */
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

    /**
     * clear all values in the heap
     */
    @Override
    public void clear() {
        values.clear();
    }

    /**
     * offer a value to the heap
     *
     * @param value the element
     * @return true if added to the heap
     */
    @Override
    public boolean offer(E value) {
        assert value != null;
        if (size() < capacity) {
            return heapify(value);
        } else if (isMin(peek(), value)) {
            boolean success = heapify(value);
            values.remove(values.size() - 1);
            return success;
        }
        return false;
    }

    /**
     * add a value and bubble it up
     *
     * @param value the element to add
     */
    private boolean heapify(E value) {
        assert value != null;
        // append value
        if (values.add(value)) {
            // bubble it up
            int start = Util.parent(values.size() - 1);
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
     * @param start the start position
     * @param end   the end position
     */
    private void bubbleUp(int start, int end) {
        int child = end;
        while (child >= start) {
            int parent = Util.parent(child);
            if (isMin(parent, child)) {
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
     * @param start the start position
     * @param end   the end position
     */
    private void bubbleDown(int start, int end) {
        int root = start;

        int child = Util.leftChild(root);

        while (child <= end) {
            int swap = root;

            if (isMin(swap, child)) {
                swap = child;
            }
            if (child + 1 <= end && isMin(swap, child + 1)) {
                swap = child + 1;
            }
            if (swap == root) {
                return;
            }
            swap(root, swap);
            root = swap;
            child = Util.leftChild(root);
        }
    }

    /**
     * remove the top element in the heap
     *
     * @return the element removed
     * @throws NoSuchElementException if the heap is empty
     */
    public E remove() {
        return remove(0, false);
    }

    /**
     * remove the top element in the heap
     *
     * @return the top element or null
     */
    public E poll() {
        return remove(0, true);
    }

    /**
     * gets the size of the heap
     *
     * @return the heap size
     */
    public int size() {
        return values.size();
    }

    /**
     * tests if the heap is empty
     *
     * @return true if the heap is empty
     */
    public boolean isEmpty() {
        return values.isEmpty();
    }

    /**
     * tests if an element exists in the heap
     *
     * @param o the element to test
     * @return true if the element exists
     */
    @Override
    public boolean contains(Object o) {
        return values.contains(o);
    }

    /**
     * gets an element at a position
     *
     * @param position    the position of the element
     * @param nullOnEmpty return null of set to true and heap is empty
     * @return the element or the return value of the error handler
     * @throws NoSuchElementException if the error handler does
     */
    private E get(int position, boolean nullOnEmpty) {
        if (values.isEmpty()) {
            if (nullOnEmpty) {
                return null;
            }
            throw new NoSuchElementException();
        }
        int parent = Util.parent(position);
        int child = Util.leftChild(parent);
        return values.get(child);
    }

    /**
     * gets the top element
     *
     * @return the element
     * @throws NoSuchElementException if the heap is empty
     */
    public E element() {
        return get(0, false);
    }

    /**
     * looks at the top element
     *
     * @return the top element or null
     */
    public E peek() {
        return get(0, true);
    }

    @Override
    public Iterator<E> iterator() {
        return new HeapIterator<>(this);
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
     * utility methods
     */
    private interface Util {

        static int parent(int index) {
            if (index < 0) {
                return 0;
            }
            return (index - 1) / 2;
        }

        static int leftChild(int index) {
            if (index < 0) {
                return 0;
            }
            return (2 * index) + 1;
        }

        static int rightChild(int index) {
            return leftChild(index) + 1;
        }

    }
}
