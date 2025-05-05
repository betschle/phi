package com.neutronio.phi.util.collections;

import com.neutronio.phi.PhiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class IndexedGridTest {

    @Test
    public void addNewTile() {
        IndexedGrid<String> map = new IndexedGrid<>(32, 32);
        Long newID = map.getNewID();;
        map.registerTile("Test", newID);
        map.putTile(newID, 1, 1);
        String tileContent = map.getByLocalCoordinates(1, 1);
        String tileContent2 = map.get(1 - map.getWidth()/2,1 - map.getHeight()/2);

        Assertions.assertNotEquals(Short.MIN_VALUE, newID);
        Assertions.assertEquals("Test", tileContent);
        Assertions.assertEquals(tileContent, tileContent2);
        Assertions.assertFalse(map.isEmptyLocalCoordinates(1,1));
        Assertions.assertFalse(map.isEmpty(1 - map.getWidth()/2,1 - map.getHeight()/2));
    }

    @Test
    public void addExistingTile() {
        IndexedGrid<String> map = new IndexedGrid<>(32, 32);
        Long newID = map.getNewID();;
        map.registerTile("Test", newID);
        map.putTile(newID, 1, 1);
        map.putTile(newID, 1, 2);

        String tileContent1 = map.getByLocalCoordinates(1, 1);
        String tileContent2 = map.getByLocalCoordinates(1, 2);

        Assertions.assertNotEquals(Short.MIN_VALUE, newID);
        Assertions.assertEquals("Test", tileContent1);
        Assertions.assertEquals("Test", tileContent2);
    }

    @Test
    public void addUnregisteredTile() {
        Assertions.assertThrows(PhiException.class,
            new Executable() {
                @Override
                public void execute() throws Throwable {
                    IndexedGrid<String> map = new IndexedGrid<>(32, 32);
                    Long newID = map.getNewID();;
                    map.putTile(newID, 1, 1);
                }
            });
    }

    @Test
    public void removeElement() {

    }
}

