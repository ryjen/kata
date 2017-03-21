package com.github.ryjen.kata;

import com.github.ryjen.kata.heap.Heap;
import com.github.ryjen.kata.heap.MaxHeap;
import com.github.ryjen.kata.heap.MinHeap;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;

/**
 * Created by ryan on 2017-03-12.
 */
public class HeapTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testMaxHeapOffer() {
        Heap<Integer> heap = new MaxHeap<>(5);
        Assert.assertTrue(heap.offer(3));
        Assert.assertTrue(heap.offer(8));
        Assert.assertTrue(heap.offer(1));
        Assert.assertTrue(heap.offer(4));
        Assert.assertTrue(heap.offer(10));
        Assert.assertFalse(heap.offer(7));
        Assert.assertTrue(heap.offer(13));
    }

    @Test
    public void testMaxHeapRemove() {

        Heap<Integer> heap = new MaxHeap<>(5);

        Assert.assertTrue(heap.offer(3));
        Assert.assertTrue(heap.offer(8));
        Assert.assertTrue(heap.offer(1));
        Assert.assertTrue(heap.offer(4));
        Assert.assertTrue(heap.offer(10));
        Assert.assertFalse(heap.offer(7));
        Assert.assertTrue(heap.offer(13));

        Assert.assertEquals(new Integer(13), heap.remove());
        Assert.assertEquals(new Integer(10), heap.remove());
        Assert.assertEquals(new Integer(8), heap.remove());
        Assert.assertEquals(new Integer(4), heap.remove());
        Assert.assertEquals(new Integer(3), heap.remove());
        exception.expect(NoSuchElementException.class);
        heap.remove();
    }

    @Test
    public void testMinHeapRemove() {
        Heap<Integer> heap = new MinHeap<>(5);

        Assert.assertTrue(heap.offer(3));
        Assert.assertTrue(heap.offer(8));
        Assert.assertTrue(heap.offer(13));
        Assert.assertTrue(heap.offer(4));
        Assert.assertTrue(heap.offer(10));
        Assert.assertFalse(heap.offer(7));
        Assert.assertTrue(heap.offer(1));

        Assert.assertEquals(new Integer(1), heap.remove());
        Assert.assertEquals(new Integer(3), heap.remove());
        Assert.assertEquals(new Integer(4), heap.remove());
        Assert.assertEquals(new Integer(8), heap.remove());
        Assert.assertEquals(new Integer(10), heap.remove());
        exception.expect(NoSuchElementException.class);
        heap.remove();
    }
}
