package com.neutronio.phi.math;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.neutronio.phi.PhiException;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A random number generator that outputs numbers.
 * Outputs the same values if methods are invoked in the same order with the same seed.
 */
public class NumberGenerator {

    private Random random;

    public NumberGenerator(long seed) {
        this.random = new Random(seed);
    }

    public void setSeed(long seed) {
        this.random = new Random(seed);
    }

    public float getRandomFloat(float max, float min) {
        if ( max < min) throw new PhiException(PhiException.ErrorCode.E0004,"Min can't be larger than max!");
        return random.nextFloat() * (max - min) + min;
    }

    /**
     * Checks if a probability value returns true based on the current seed
     * @param probability value 0-100
     */
    public boolean isTrue(float probability) {
        probability = MathUtils.clamp(probability, 0, 100);
        float randomFloat = getRandomFloat(100, 0);
        return probability >= randomFloat;
    }

    /**
     * Gets a random integer within min and max bounds using the underlying random seed.
     * @param max
     * @param min
     * @return
     */
    public int getRandomInteger(int max, int min) {
        if ( max < min) throw new PhiException(PhiException.ErrorCode.E0004,"Min can't be larger than max!");
        return (int) (random.nextFloat() * (max - min) + min);
    }

    /**
     * Gets a random entry from an Array class using the generator-provided seed
     * @param array the gdx array
     * @param <T> the type of object
     * @return a random entry from array
     */
    public <T> T getRandomEntry(Array<T> array) {
        if( array.isEmpty()) return null;
        if( array.size == 1) return array.get(0);
        int randomInteger = getRandomInteger(array.size, 0);
        return array.get(randomInteger);
    }

    /**
     * Gets a random entry from an array using the generator-provided seed
     * @param array the gdx array
     * @param <T> the type of object
     * @return a random entry from array
     */
    public <T> T getRandomEntry(T[] array) {
        if( array.length == 0) return null;
        if( array.length == 1) return array[0];
        int randomInteger = getRandomInteger(array.length, 0);
        return array[randomInteger];
    }

    /**
     * Gets a random entry from a list using the generator-provided seed
     * @param array the gdx array
     * @param <T> the type of object
     * @return a random entry from array
     */
    public <T> T getRandomEntry(List<T> array) {
        if( array.isEmpty()) return null;
        if( array.size() == 1) return array.get(0);
        int randomInteger = getRandomInteger(array.size(), 0);
        return array.get(randomInteger);
    }

    /**
     * Gets a random entry from a map using the generator-provided seed
     * @param map
     * @param <T>
     * @return
     */
    public <T> T getRandomEntry(Map<?, T> map) {
        if( map.isEmpty()) return null;
        if( map.size() == 1) return (T) map.values().toArray()[0];
        int randomInteger = getRandomInteger(map.size(), 0);
        return map.get( map.keySet().toArray()[randomInteger] );
    }
}
