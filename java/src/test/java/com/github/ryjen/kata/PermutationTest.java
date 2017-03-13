package com.github.ryjen.kata;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Created by ryan on 2017-02-04.
 */
public class PermutationTest extends TestCase {

    private static <T> void assertListEquality(List<T> expected, List<T> actual) {
        Assert.assertEquals(expected.size(), actual.size());

        for(int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void testGenerate() {
        List<Character> items = Arrays.asList('A', 'B', 'C');

        Permutation<Character> permutations = Permutation.generate(items);

        Assert.assertEquals(6, permutations.size());

        Iterator<List<Character>> it = permutations.iterator();

        assertListEquality(Arrays.asList('C', 'B', 'A'), it.next());

        assertListEquality(Arrays.asList('B', 'C', 'A'), it.next());

        assertListEquality(Arrays.asList('B', 'A', 'C'), it.next());

        assertListEquality(Arrays.asList('C', 'A', 'B'), it.next());

        assertListEquality(Arrays.asList('A', 'C', 'B'), it.next());

        assertListEquality(Arrays.asList('A', 'B', 'C'), it.next());

    }
}
