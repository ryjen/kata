package com.github.ryjen.kata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class to generate permuations
 */
public final class Permutation<T> extends ArrayList<List<T>> {

    private Permutation() {
        super();
    }

    private Permutation(Permutation<T> other) {
        super(other);
    }

    public static <T> Permutation<T> generate(Collection<T> elements) {
        Permutation<T> result = new Permutation<>();

        result.add(new ArrayList<>());

        for (T element : elements) {
            Permutation<T> current = new Permutation<>();

            for (List<T> list : result) {
                for (int i = 0; i < list.size() + 1; i++) {
                    list.add(i, element);

                    List<T> temp = new ArrayList<>(list);
                    current.add(temp);

                    list.remove(i);
                }
            }

            result = new Permutation<>(current);
        }

        return result;
    }
}
