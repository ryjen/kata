package com.github.ryjen.kata.graph.tree;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ryan on 2017-04-23.
 */
public class DisjointedSubsetTest {

    @Test
    public void testMakeSet() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

        DisjointedSet<Integer> djs = new DisjointedSet<>();

        for (Integer i : list) {
            djs.makeSet(i);
        }

        Assert.assertEquals(8, djs.size());
    }

    @Test
    public void testFind() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

        DisjointedSet<Integer> djs = new DisjointedSet<>();

        for (Integer i : list) {
            djs.makeSet(i);
        }

        Set<Integer> actual = djs.find(4);

        Set<Integer> expected = new HashSet<>();
        expected.add(4);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testUnion() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

        DisjointedSet<Integer> djs = new DisjointedSet<>();

        for (Integer i : list) {
            djs.makeSet(i);
        }

        Set<Integer> a = djs.find(3);

        Assert.assertEquals(1, a.size());

        Set<Integer> b = djs.find(6);

        Assert.assertEquals(1, b.size());

        djs.union(a, b);

        Assert.assertEquals(7, djs.size());

        a = djs.find(3);

        Assert.assertEquals(2, a.size());
    }

    @Test
    public void testEquals() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

        DisjointedSet<Integer> djs = new DisjointedSet<>();

        for (Integer i : list) {
            djs.makeSet(i);
        }

        Set<Integer> a = djs.find(3);

        Assert.assertEquals(1, a.size());

        Set<Integer> b = djs.find(6);

        Assert.assertEquals(1, b.size());

        Assert.assertNotEquals(a, b);

        djs.union(a, b);

        b = djs.find(3);

        Assert.assertEquals(a, b);
    }
}
