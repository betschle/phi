package com.neutronio.phi.util;

import com.neutronio.phi.PhiException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Counts occurrences of objects for validation/statistic/debug purposes.
 * Does not allow null values to be counted by default. It can be enabled
 * via {@link #setAllowNull(boolean)}.
 */
public class Counter<T> {

    private static final Logger logger = Logger.getLogger(Counter.class.getCanonicalName());

    private boolean allowNull = false;
    private int totalCounts = 0;
    private Map<T, Integer> counterMap = new LinkedHashMap<>();
    /**
     * Assigns a display key for each entry, in case non-string values are counted.
     * This is for debugging purposes only
     */
    private Map<T, String> displayKey;


    public Counter(boolean allowNull) {
        this.allowNull = allowNull;
    }

    /**
     * Serialization only
     */
    public Counter() {
    }

    /**
     * Adds an optional display name for a counted object or key.
     * Once invoked and enabled, there must be a mapping for all possible entries,
     * otherwise null values are displayed.
     *
     * @param forKey
     * @param displayName
     */
    public void addDisplayName(T forKey, String displayName) {
        if (this.displayKey == null) {
            this.displayKey = new LinkedHashMap<>();
        }
        this.displayKey.put(forKey, displayName);
    }

    /**
     * Adds one count assigned to object. If object could not be found, this method does not do anything.
     *
     * @param object the object to count
     */
    public void addOne(T object) {
        this.addMany(object, 1);
    }

    /**
     * Adds multiple counts assigned to object. If object could not be found, this method does not do anything.
     *
     * @param object the object to count
     * @param count  how many objects were counted, this is an absolute value
     */
    public void addMany(T object, int count) {
        if (!allowNull && object == null)
            throw new PhiException(PhiException.ErrorCode.E0004, "Null values not allowed");
        if (this.counterMap.containsKey(object)) {
            this.totalCounts = totalCounts + count;
            this.counterMap.put(object, this.counterMap.get(object) + count);
        } else {
            // first time add
            this.totalCounts = totalCounts + count;
            this.counterMap.put(object, count);
        }
    }

    /**
     * Subtracts one counts assigned to object. If object could not be found, it is being added.
     *
     * @param object the object to count
     */
    public void subtractOne(T object) {
        this.subtractMany(object, 1);
    }

    /**
     * Subtracts many counts assigned to object. If object could not be found, it is being added.
     *
     * @param object how many objects tu subtract, this is an absolute value
     */
    public void subtractMany(T object, int count) {
        if (!allowNull && object == null)
            throw new PhiException(PhiException.ErrorCode.E0004, "Null values not allowed");
        if (this.counterMap.containsKey(object)) {
            this.totalCounts = this.totalCounts - count;
            this.counterMap.put(object, this.counterMap.get(object) - count);
        } else {
            // first time add
            this.totalCounts = this.totalCounts - count;
            this.counterMap.put(object, -count);
        }
    }

    /**
     * Obtains the tracked count for object.
     *
     * @param object the object to count
     * @return a number of how many objects were counted, 0 if none were found.
     * Therefore, it does not indicate if a key is present/was counted.
     */
    public Integer get(T object) {
        if (this.counterMap.containsKey(object)) {
            return this.counterMap.get(object);
        } else {
            return 0;
        }
    }

    /**
     * Checks if a key is present (that means, added to this counter)
     *
     * @param object the key to check
     * @return true if the object was counted at least once
     */
    public boolean containsKey(T object) {
        return this.counterMap.containsKey(object);
    }

    /**
     * Allows null values to be counted. Default is false.
     *
     * @return if null value is allowed to be counted
     */
    public boolean isAllowNull() {
        return this.allowNull;
    }

    /**
     * Sets whether null values should be counted.
     *
     * @param allowNull
     */
    public void setAllowNull(boolean allowNull) {
        this.allowNull = allowNull;
    }

    /**
     * Gets the percentage recorded in this counter.
     *
     * @param object the object that was counted
     * @return a percentage ranging from 0-100%
     */
    public float getPercent(T object) {
        if (this.totalCounts == 0 || this.counterMap.get(object) == 0) return 0;
        return this.counterMap.get(object) / (float) this.totalCounts * 100f;
    }

    /**
     * @return how any objects were counted in total
     */
    public int getTotalCounts() {
        return this.totalCounts;
    }

    /**
     * Resets all values to 0
     */
    public void reset() {
        this.totalCounts = 0;
        for (Object key : this.counterMap.keySet()) {
            this.counterMap.put((T) key, 0);
        }
    }

    /**
     * Gets a shallow copy of internal counter map.
     *
     * @return
     */
    public Map<T, Integer> getCounterMap() {
        Map<T, Integer> copy = new HashMap<>();
        for (Map.Entry<T, Integer> entries : this.counterMap.entrySet()) {
            copy.put(entries.getKey(), entries.getValue());
        }
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\n================================\n");
        builder.append("TOTAL COUNT: ").append(this.totalCounts);
        builder.append("\n================================\n");

        for (Map.Entry<?, ?> entry : counterMap.entrySet()) {
            Integer value = (Integer) entry.getValue();
            builder.append(this.displayKey == null ? entry.getKey() : this.displayKey.get(entry.getKey()))
                .append(":\t")
                .append(value)
                .append(" (")
                .append(getPercent((T) entry.getKey()))
                .append(" % )\n");

        }
        builder.append("================================\n");
        return builder.toString();
    }
}
