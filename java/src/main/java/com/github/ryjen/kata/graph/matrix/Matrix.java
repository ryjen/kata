package com.github.ryjen.kata.graph.matrix;

import java.lang.reflect.Array;

/**
 * Created by ryanjennings on 2017-03-20.
 */
public class Matrix<T> {

    private final Class<T> type;
    private T[][] value;

    public Matrix(Class<T> type) {
        this.type = type;
    }

    public void set(int row, int col, T value) {

        resize(Math.max(row, col));

        this.value[row][col] = value;
    }

    private boolean isValid(int row, int col) {
        return value != null && Math.min(row, col) >= 0 && Math.max(row, col) < value.length;
    }

    private boolean isEmpty(int row, int col) {
        return !isValid(row, col) || value[row][col] == null;
    }

    public T get(int row, int col) {
        if (!isValid(row, col)) {
            return null;
        }
        return value[row][col];
    }

    public boolean remove(int row, int col) {
        if (!isEmpty(row, col)) {
            value[row][col] = null;
            return true;
        }
        return false;
    }

    public void resize(int size) {

        if (value == null) {
            value = allocate(size + 1);
            for (int i = 0; i < value.length; i++) {
                value[i] = allocate();
            }
            return;
        }

        if (size >= value.length) {
            T[][] temp = value;
            value = allocate(size + 1);
            for (int i = 0; i < value.length; i++) {
                value[i] = allocate();

                if (i < temp.length) {
                    System.arraycopy(temp[i], 0, value[i], 0, temp.length);
                }
            }
        }
    }

    private T[][] allocate(int size) {
        return (T[][]) Array.newInstance(type, size, size);
    }

    private T[] allocate() {
        return (T[]) Array.newInstance(type, value.length);
    }

}
