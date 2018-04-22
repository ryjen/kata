package com.github.ryjen.kata;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SortingTest {

    @Test
    public void testMergeSort() {

        List<Integer> a = Arrays.asList(3, 5, 1, 6, 8, 2, 4);

        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 6, 8);

        Sorting.mergesort(a);

        Assert.assertEquals(a, expected);
    }
}
