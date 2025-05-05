package com.neutronio.phi.util.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GridTest {

    @Test
    public void validPlace() {
        Grid<String> grid = new Grid<>(10, 10);
        grid.place(0,0, "test1");
        Assertions.assertEquals(false, grid.isEmpty(0, 0));
        Assertions.assertEquals(true, grid.isEmpty(0, 1));
        Assertions.assertEquals("test1", grid.get(0,0));
    }

    @Test
    public void invalidPlace() {
        Grid<String> grid = new Grid<>(10, 10);
        grid.place(100,100, "test1");
        Assertions.assertEquals(null, grid.get(100,100));
        Assertions.assertEquals(true, grid.isEmpty(100, 100));
    }

    @Test
    public void erroneousPlace() {
        Grid<String> grid = new Grid<>(10, 10);
        grid.place(-100,-100, "test1");
        Assertions.assertEquals(null, grid.get(-100,-100));
    }

    @Test
    public void outOfBounds() {
        Grid<String> grid = new Grid<>(10, 10);
        Assertions.assertFalse(grid.isInBounds(100,100));
        Assertions.assertFalse(grid.isInBounds(-100,-100));
    }

    @Test
    public void erase() {
        Grid<String> grid = new Grid<>(10, 10);
        grid.place(0,0, "test1");
        grid.place(0,1, "test1");
        grid.place(0,2, "test1");
        boolean erase = grid.erase(0, 1);

        Assertions.assertEquals(true, erase);
        Assertions.assertEquals(true, grid.isEmpty(0, 1));
        Assertions.assertEquals(false, grid.isEmpty(0, 0));
        Assertions.assertNull(grid.get(0, 1));
    }
}
