package com.neutronio.phi.ui.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper for a {@link com.badlogic.gdx.scenes.scene2d.ui.SelectBox}
 * Can be sorted by display name
 * @param <T>
 */
public class SelectBoxWrapper<T> implements Comparable<SelectBoxWrapper<T>> { // rename to DisplayText ?

    private String displayName;
    private T payload;

    public SelectBoxWrapper(String displayName, T payload) {
        this.displayName = displayName;
        this.payload = payload;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return displayName;
    }

    // TODO do I have this method somewhere already?

    /**
     * Wraps a list of objects into a list of select box wrappers. Uses toString
     * as display value by default.
     * @param objects
     * @param <I>
     * @return
     */
    public static <I> List<SelectBoxWrapper<I>> wrap(List<I> objects) {
        List<SelectBoxWrapper<I>> wrappers = new ArrayList<>();
        for( I object : objects) {
            wrappers.add( new SelectBoxWrapper<I>( object.toString(), object));
        }
        return wrappers;
    }

    @Override
    public int compareTo(SelectBoxWrapper<T> o) {
        if(o == null) return this.displayName.compareTo(null);
        return this.displayName.compareTo(o.displayName);
    }
}
