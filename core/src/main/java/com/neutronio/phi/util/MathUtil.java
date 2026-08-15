package com.neutronio.phi.util;

import java.util.Random;

public class MathUtil {

    static Random random = new Random(System.currentTimeMillis());

    /**
     * Gets a random float
     * @param max
     * @param min
     * @return
     */
    public static float getRandomFloat(float max, float min) {
        if ( max < min) throw new IllegalArgumentException("Min can't be larger than max!");
        return random.nextFloat() * (max - min) + min;
    }

    /**
     * Gets a random integer
     * @param bound
     * @return
     */
    public static int getRandomInteger(int bound) {
        return random.nextInt(bound);
    }
}
