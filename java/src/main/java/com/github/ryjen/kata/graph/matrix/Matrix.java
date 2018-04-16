package com.github.ryjen.kata.graph.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Wrapper for a 2d array for a type
 */
class Matrix<T> {

    private List<List<Optional<T>>> value;

    public Matrix() {
    }

    public Matrix(Matrix<T> other) {

        value = new ArrayList<>();

        for (List<Optional<T>> l : other.value) {
            List<Optional<T>> n = new ArrayList<>();
            n.addAll(l);
            value.add(n);
        }
    }

    public void clear() {
        if (value == null) {
            return;
        }
        value.clear();
    }

    public void set(int row, int col, T value) {

        resize(Math.max(row, col) + 1);

        this.value.get(row).set(col, Optional.of(value));
    }

    private boolean isValid(int row, int col) {
        return value != null && Math.min(row, col) >= 0 && Math.max(row, col) < value.size();
    }

    private boolean isEmpty(int row, int col) {
        return !isValid(row, col) || !value.get(row).get(col).isPresent();
    }

    public T get(int row, int col) {
        if (!isValid(row, col)) {
            return null;
        }
        Optional<T> val = value.get(row).get(col);

        return val.orElse(null);
    }

    public boolean remove(int row, int col) {
        if (!isEmpty(row, col)) {
            value.get(row).set(col, Optional.empty());
            return true;
        }
        return false;
    }

    public void resize(int size) {

        if (value == null) {

            value = allocate(size);

            for (int i = 0; i < value.size(); i++) {
                value.set(i, allocate());
            }
            return;
        }

        if (size >= value.size()) {
            List<List<Optional<T>>> temp = value;

            value = allocate(size);

            for (int i = 0; i < value.size(); i++) {
                List<Optional<T>> row = allocate();

                if (row == null) {
                    break;
                }

                value.set(i, row);

                if (i < temp.size()) {
                    List<Optional<T>> old = temp.get(i);
                    for (int j = 0; j < old.size(); j++) {
                        row.set(j, old.get(j));
                    }
                }
            }
        }
    }

    private List<List<Optional<T>>> allocate(int size) {

        List<List<Optional<T>>> list = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            list.add(new ArrayList<>());
        }
        return list;
    }

    private List<Optional<T>> allocate() {
        if (value == null) {
            return null;
        }
        List<Optional<T>> list = new ArrayList<>(value.size());

        for (int i = 0; i < value.size(); i++) {
            list.add(Optional.empty());
        }
        return list;
    }

}
