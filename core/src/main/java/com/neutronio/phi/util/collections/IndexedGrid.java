package com.neutronio.phi.util.collections;


import com.neutronio.phi.PhiException;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A compact Grid with object indexing. Instead of saving an object per grid cell,
 * stores the object indices and keeps the objects themselves in a map.
 * If each cell is required to be filled with a different object,
 * it's better to use {@link Grid} directly.
 *
 * @param <T> the cell type
 */
public class IndexedGrid<T> implements Serializable {

    /** Current map ID */
    private long currentID = Long.MIN_VALUE;
    /** A grid of IDs that points to an object in {@link #objectIndex} */
    private Grid<Long> data;
    /** An index of objects located in {@link #data} */
    private Map<Long, T> objectIndex = new LinkedHashMap<>();

    public IndexedGrid(int width, int heigth) {
        this.data = new Grid<>(width, heigth);
    }
    /**
     * Gets the current ID and forwards it.
     * @return
     */
    public Long getNewID() {
        return ++this.currentID;
    }

    /**
     * Puts object at location
     * @param object
     * @param id the id to use, use {@link #getNewID()} to obtain one
     * @param x local coordinates with 0/0 origin
     * @param y local coordinates with 0/0 origin
     */
    public void put(T object, Long id, int x, int y) {
        this.data.place(x, y, id);
        if( !this.objectIndex.containsKey(id)) {
            this.objectIndex.put(id, object);
        }
    }

    /**
     * Puts the object with Id at location
     * @param id the id to put
     * @param x local coordinates with 0/0 origin
     * @param y local coordinates with 0/0 origin
     */
    public void putTile(Long id, int x, int y) {
        if( !this.objectIndex.containsKey(id)) {
            throw new PhiException(PhiException.ErrorCode.E3000, "Error placing tile with id: " + id);
        }
        this.data.place(x, y, id);

    }

    /**
     * Registers a tile to the index. Does not do anything if the object was already registered.
     * @param object the object to register
     * @param id the ID to use
     */
    public void registerTile(T object, Long id) {
        if( !this.objectIndex.containsKey(id)) {
            this.objectIndex.put(id, object);
        }
    }

    /**
     * Obtains a cell by its local location. Use this to iterate over map data.
     *
     * @param x local coordinates with 0/0 origin
     * @param y local coordinates with 0/0 origin
     * @return
     */
    public T getByLocalCoordinates( int x, int y) {
        Long tileId = this.data.get(x, y);
        return this.objectIndex.get(tileId);
    }

    /**
     * Translates input world coordinates (with origin middle of map)
     * to local coordinates (with origin bottom right)
     *
     * @param x world coordinates with center origin
     * @param y world coordinates with center origin
     * @return
     */
    public T get( int x, int y) {
        Long tileId = this.data.get(x + getWidth()/2, y + getHeight()/2);
        return this.objectIndex.get(tileId);
    }

    /**
     * Checks if there is an object at the given location. Translates input world coordinates (with origin middle of galaxy)
     * to local coordinates (with origin bottom right)
     *
     * @param x local coordinates with 0/0 origin
     * @param y local coordinates with 0/0 origin
     * @return null if none exist
     */
    public boolean isEmptyLocalCoordinates(int x, int y) {
        Long tileId = this.data.get(x, y);
        return this.objectIndex.get(tileId) == null;
    }

    /**
     * Checks if there is an object at the given location. Translates input world coordinates (with origin middle of galaxy)
     * to local coordinates (with origin bottom right)
     *
     * @param x world coordinates with center origin
     * @param y world coordinates with center origin
     * @return null if none exist
     */
    public boolean isEmpty(int x, int y) {
        Long tileId = this.data.get(x + getWidth()/2, y + getHeight()/2);
        return this.objectIndex.get(tileId) == null;
    }

    public int getWidth() {
        return this.data.getWidth();
    }

    public int getHeight() {
        return this.data.getHeight();
    }
}
