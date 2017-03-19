package com.github.ryjen.kata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ryan on 2017-03-12.
 */
public final class Sorting {


    /**
     * ALGORITHM merge(B[0..p-1], C[0..q-1], A[0..p+q-1])
     * Merges two sorted arrays into one sorted array
     *
     * @param B   sorted array
     * @param C   sorted array
     * @param A   the array to merge into
     * @param <T> the comparable type contained in the arrays
     */
    private static <T extends Comparable<T>> void merge(List<T> B, List<T> C, List<T> A) {
        int i = 0;
        int j = 0;
        int k = 0;

        assert B != null;
        assert C != null;
        assert A != null;

        while (i < B.size() && j < C.size()) {
            if (B.get(i).compareTo(C.get(j)) <= 0) {
                A.set(k++, B.get(i++));
            } else {
                A.set(k++, C.get(j++));
            }
        }

        if (i == B.size()) {
            Collections.copy(A.subList(k, A.size()), C.subList(j, C.size()));
        } else {
            Collections.copy(A.subList(k, A.size()), B.subList(i, B.size()));
        }
    }

    /**
     * ALGORITHM mergesort(A[0..n-1])
     * Divide and Conquer algorithm to sort a list
     *
     * @param A   An array A[0..n-1] of orderable elements
     * @param <T> the comparable type the array contains
     */
    public static <T extends Comparable<T>> void mergesort(List<T> A) {
        assert A != null;

        if (A.size() > 1) {
            int middle = (A.size() / 2);

            List<T> B = new ArrayList<>(A.subList(0, middle));
            List<T> C = new ArrayList<>(A.subList(middle, A.size()));

            mergesort(B);
            mergesort(C);

            merge(B, C, A);
        }
    }
}
