package com.neutronio.phi.math;

import com.badlogic.gdx.utils.Array;
import com.neutronio.phi.PhiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NumberGeneratorTest {

    @Test
    public void ensureSameSeedOutputsSameValues() {
        NumberGenerator generator1 = new NumberGenerator(333);
        NumberGenerator generator2 = new NumberGenerator(333);

        List strings = List.of("Lolol", "Lalala", "herpaderpa");
        String[] stringArray = new String[] { "Same", "Seed", "Same", "Values"};
        Array<String> gdxArray = new Array<>();
        gdxArray.add("Entry1");
        gdxArray.add("Entry2");
        gdxArray.add("Entry3");

        Assertions.assertEquals( generator1.getRandomFloat(1, 0),  generator2.getRandomFloat(1, 0));
        Assertions.assertEquals( generator1.getRandomInteger(5, 0),  generator2.getRandomInteger(5, 0));
        Assertions.assertEquals( generator1.getRandomEntry(strings),  generator2.getRandomEntry(strings));
        Assertions.assertEquals( generator1.getRandomEntry(stringArray),  generator2.getRandomEntry(stringArray));
        Assertions.assertEquals( generator1.getRandomEntry(gdxArray),  generator2.getRandomEntry(gdxArray));
        Assertions.assertEquals( generator1.isTrue(50),  generator2.isTrue(50));
    }

    @Test
    public void throwExceptionOnMaxSmallerThanMin() {

        Assertions.assertThrows(PhiException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                NumberGenerator generator = new NumberGenerator(333);
                generator.getRandomFloat(0f, 30f);
            }
        });

        Assertions.assertThrows(PhiException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                NumberGenerator generator = new NumberGenerator(333);
                generator.getRandomInteger(0, 30);
            }
        });
    }

    @Test
    public void returnNullOnEmptyList() {
        NumberGenerator generator = new NumberGenerator(333);
        Assertions.assertNull(generator.getRandomEntry( new String[0]));
        Assertions.assertNull(generator.getRandomEntry( new ArrayList<>()));
        Assertions.assertNull(generator.getRandomEntry( new HashMap<>()));
    }
}
