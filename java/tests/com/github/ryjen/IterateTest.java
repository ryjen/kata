package com.github.ryjen;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ryan on 2016-08-13.
 */
public class IterateTest extends RyJenTest {

    @Test
    public void testFindLargetsNumbers()
    {
        testFindLargestNumber(new Integer[]{12, 34, 67, 6787, 123, 34, 454}, 6787);
        testFindLargestNumber(new Integer[]{1234, -1234, 2345, 234, 654, 324}, 2345);
    }

    private void testFindLargestNumber(Integer[] values, int expected) {

        int max = 0;
        for(int i = 0; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
            }
        }

        Assert.assertEquals(max, expected);
    }

    private int reverseIterator(int i, int max) {
        int rem = max - i;
        return rem - 1;
    }

    @Test
    public void testReverseIterate()
    {
        int[] reversed = new int[100];
        for(int i = 99, j = 0; i >= 0; i--, j++) {
            reversed[j] = i;
        }

        for(int i = 0; i < reversed.length; i++) {
            Assert.assertEquals(reversed[i], reverseIterator(i, reversed.length));
        }
    }
}
