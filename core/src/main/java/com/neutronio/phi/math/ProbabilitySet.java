package com.neutronio.phi.math;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A set of probabilities to shuffle values from. Unlike
 * TechTree and Tree Implementations, this set is flat and
 * has no depth.
 */
public class ProbabilitySet<T> implements Serializable {

    /**
     * Single mode means the set
     * stops outputting results once a single result
     * was created. Otherwise in Multimode, the list is iterated
     * to the end to create multiple results.
     */
    private boolean singleMode;
    private List<T> items = new ArrayList<>();
    private List<Float> probability = new ArrayList<>();
    private float sum = 0;

    public void addProbability(T object, float probability) {
        if (object == null) return;
        this.items.add(object);
        this.probability.add(probability);
        this.sum += probability;
    }

    /**
     * @return The sum of all added probability weights
     */
    public Float sum() {
        return sum;
    }

    public int size() {
        return items.size();
    }

    public T getItemAt(int index) {
        return items.get(index);
    }

    public float getProbabilityAt(int index) {
        return probability.get(index);
    }

    /**
     * @return true if in single mode. Single mode means the set
     * stops outputting results once a single result
     * was created. Otherwise in Multimode, the list is iterated
     * to the end to create multiple results.
     */
    public boolean isSingleMode() {
        return singleMode;
    }

    /**
     * @param singleMode true if single mode should be used.
     *                   Single mode means the set stops outputting
     *                   results once a single result was created.
     *                   Otherwise in Multimode, the list is iterated
     *                   to the end to create multiple results.
     */
    public void setSingleMode(boolean singleMode) {
        this.singleMode = singleMode;
    }

    private void writeObject(ObjectOutputStream oos)
        throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(this.singleMode);
        oos.writeObject(this.items.size());
        for (int i = 0; i < this.items.size(); i++) {
            oos.writeObject(this.items.get(i));
            oos.writeObject(this.probability.get(i));
        }
    }

    private void readObject(ObjectInputStream ois)
        throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        this.singleMode = (boolean) ois.readObject();
        int size = (int) ois.readObject();
        for (int i = 0; i < size; i++) {
            T item = (T) ois.readObject();
            float probability = (float) ois.readObject();
            this.addProbability(item, probability);
        }
    }
}
