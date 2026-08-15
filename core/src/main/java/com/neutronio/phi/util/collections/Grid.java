package com.neutronio.phi.util.collections;


import java.io.Serializable;

/**
 * A storage grid based on a 2 dimensional array, meant for maps or similar.
 * @param <C>
 */
public class Grid<C> implements Serializable {

    private static final long serialVersionUID = 453540746598647840L;
    private int width;
    private int height;
    private Object[][] cells;

    /**
     * @param width  in tiles
     * @param height in tiles
     */
    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new Object[width][height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // TODO also place a set of tiles as brush of the sort

    /**
     * @param x
     * @param y
     * @return true if placing was successful
     */
    public void place(int x, int y, C object) {
        if(this.isInBounds(x, y)) {
            this.cells[x][y] = object;
        }
    }

    /**
     * @param x
     * @param y
     * @return null if not in bounds or empty
     */
    public C get(int x, int y) {
        if(this.isInBounds(x, y)) {
            return (C) this.cells[x][y];
        }
        return null;
    }

    /**
     * @param x
     * @param y
     * @return true if erasing was successful
     */
    public boolean erase(int x, int y) {
        if(this.isInBounds(x, y)) {
            this.cells[x][y] = null;
            return true;
        }
        return false;
    }

    /**
     * Checks if a coordinate is in bounds.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isInBounds(int x, int y) {
        if(x < 0 || y < 0) return false;
        if(x < this.cells.length) {
            if(y < this.cells[x].length) return true;
        }
        return false;
    }

    /**
     * @param x
     * @param y
     * @return true if the cell at x, y is not null or if out of bounds.
     */
    public boolean isEmpty(int x, int y) {
        if(this.isInBounds(x, y)) {
            return this.cells[x][y] == null;
        }
        return true;
    }

    public void clear() {
        for(int x = 0; x < this.cells.length; x++) {
            for(int y = 0; y < this.cells[x].length; y++) {
                this.cells[x][y] = null;
            }
        }
    }
}
