package com.neutronio.phi.util;

import com.neutronio.phi.PhiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CounterTest {

    private enum Coffee {
        LATTE,
        EXPRESSO,
        CAPPUCCINO;
    }

    @Test
    public void string() {
        Counter enumCounter = new Counter();
        enumCounter.addOne(Coffee.LATTE);
        enumCounter.addOne(Coffee.LATTE);
        enumCounter.subtractOne(Coffee.EXPRESSO);
        String counterString = enumCounter.toString();

        Assertions.assertTrue( counterString.contains("-1") );
        Assertions.assertTrue( counterString.contains("2") );
        Assertions.assertTrue( counterString.contains( Coffee.LATTE.name() ) );
        Assertions.assertTrue( counterString.contains( Coffee.EXPRESSO.name() ) );

    }

    @Test
    public void addSubtractResetTest() {
        Counter enumCounter = new Counter();
        enumCounter.addOne(Coffee.LATTE);
        enumCounter.addOne(Coffee.LATTE);

        int totalCounts = enumCounter.getTotalCounts();
        float percent = enumCounter.getPercent(Coffee.LATTE);

        Assertions.assertEquals( 2, totalCounts);
        Assertions.assertEquals( 100f, percent);
        Assertions.assertEquals( 0, enumCounter.get(Coffee.CAPPUCCINO) );
        Assertions.assertEquals( 0, enumCounter.get(Coffee.EXPRESSO) );

        enumCounter.subtractOne(Coffee.LATTE);
        Assertions.assertEquals( 1, enumCounter.get(Coffee.LATTE) );

        enumCounter.reset();
        Assertions.assertEquals( 0, enumCounter.get(Coffee.LATTE) );
        Assertions.assertEquals( 0, enumCounter.get(Coffee.CAPPUCCINO) );
    }

    @Test
    public void addSubtractManyResetTest() {
        Counter enumCounter = new Counter();
        enumCounter.addMany(Coffee.LATTE, 1);
        enumCounter.subtractMany(Coffee.LATTE, 1);

        enumCounter.addMany(Coffee.CAPPUCCINO, 10);
        enumCounter.subtractMany(Coffee.CAPPUCCINO, 1);

        int totalCounts = enumCounter.getTotalCounts();
        float percent = enumCounter.getPercent(Coffee.LATTE);

        Assertions.assertEquals( 9, totalCounts);
        Assertions.assertEquals( 0, enumCounter.getPercent(Coffee.LATTE) );
        Assertions.assertEquals( 100, enumCounter.getPercent(Coffee.CAPPUCCINO) );
        System.out.println( enumCounter );
    }

    @Test
    public void addingNullValue() {
        Counter enumCounter = new Counter(true);
        enumCounter.addOne(null);

        int totalCounts = enumCounter.getTotalCounts();

        Assertions.assertEquals( 1, totalCounts);
    }

    @Test
    public void nullValueOnInit() {
        try {
            Counter enumCounter = new Counter(false);
            enumCounter.addOne(null);
        } catch (PhiException e) {
            e.printStackTrace();
            return; //terminate
        }
        Assertions.fail("Exception expected");
    }

    @Test
    public void desiredNullValueOnInit() {
        try {
            Counter enumCounter = new Counter(true);
            enumCounter.addOne(null);
        } catch (PhiException e) {
            e.printStackTrace();
            Assertions.fail("Exception not expected");
        }
    }
}
