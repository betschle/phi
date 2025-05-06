package com.neutronio.phi.util;

import java.io.Serializable;
import java.util.Objects;

/**
 * A vector consisting of 2 integer coordinates
 */
public class Vector2i implements Serializable {

    private static final long serialVersionUID = 2821784259219133272L;

    public int x = 0;
    public int y = 0;

    public Vector2i() {
    }

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set( int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2i copy() {
        return new Vector2i(x, y);
    }

    @Override
    public String toString() {
        return "Vector2i{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2i vector2i = (Vector2i) o;
        return x == vector2i.x && y == vector2i.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
