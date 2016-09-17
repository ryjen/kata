package com.github.ryjen;

import junit.framework.TestCase;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by ryan on 2016-08-13.
 */
public class RyJenTest extends TestCase {

    private static final Random rand = new SecureRandom();

    public Integer[] randomIntArray(int length)
    {
        Integer[] values = new Integer[length];

        for(int i = 0; i < values.length; i++) {
            values[i] = rand.nextInt();
        }

        return values;
    }
}
