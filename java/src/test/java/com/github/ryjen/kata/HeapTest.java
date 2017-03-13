package com.github.ryjen.kata;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Created by ryan on 2017-03-12.
 */
public class HeapTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private Heap<Integer> buildTestHeap() {

        Heap<Integer> heap = new Heap.Max<>(5);

        Assert.assertTrue(heap.offer(3));
        Assert.assertTrue(heap.offer(8));
        Assert.assertTrue(heap.offer(1));
        Assert.assertTrue(heap.offer(4));
        Assert.assertTrue(heap.offer(10));
        Assert.assertFalse(heap.offer(7));
        Assert.assertTrue(heap.offer( 13));

        return heap;
    }

    @Test
    public void testHeapMax(){
        buildTestHeap();
    }

    @Test
    public void testHeapMaxRemove() {

        Heap<Integer> heap = buildTestHeap();

        Assert.assertEquals(new Integer(13), heap.remove());
        Assert.assertEquals(new Integer(10), heap.remove());
        Assert.assertEquals(new Integer(8), heap.remove());
        Assert.assertEquals(new Integer(4), heap.remove());
        Assert.assertEquals(new Integer(3), heap.remove());
        exception.expect(NoSuchElementException.class);
        heap.remove();
    }
}
