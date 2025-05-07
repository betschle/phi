package com.neutronio.phi.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProbabilitySetGeneratorTest {

    @Test
    public void pickDifferentRanges() {
        ProbabilitySet<String> probabilitySet = new ProbabilitySet<>();
        probabilitySet.addProbability("A", 50);
        probabilitySet.addProbability("B", 20);
        probabilitySet.addProbability("C", 20);

        ProbabilitySetGenerator generator = new ProbabilitySetGenerator();
        Assertions.assertEquals( "A", generator.pickEntry(49, probabilitySet));
        Assertions.assertEquals( "B", generator.pickEntry(50, probabilitySet));
        Assertions.assertEquals( "B", generator.pickEntry(60, probabilitySet));
        Assertions.assertEquals( "B", generator.pickEntry(69, probabilitySet));
        Assertions.assertEquals( "C", generator.pickEntry(70, probabilitySet));
        Assertions.assertEquals( "C", generator.pickEntry(71, probabilitySet));
        Assertions.assertEquals( "C", generator.pickEntry(89, probabilitySet));
        Assertions.assertEquals( null, generator.pickEntry(90, probabilitySet));
    }

    @Test
    public void generate() {
        NumberGenerator numberGenerator = new NumberGenerator(333);
        ProbabilitySet<String> probabilitySet = new ProbabilitySet<>();
        probabilitySet.addProbability("A", 50);
        probabilitySet.addProbability("B", 20);
        probabilitySet.addProbability("C", 20);

        ProbabilitySetGenerator generator = new ProbabilitySetGenerator();
        String generated = generator.generate(numberGenerator, probabilitySet);
        Assertions.assertTrue( generated.equals("A") || generated.equals("B") || generated.equals("C") );
    }

    @Test
    public void pickNull() {
        ProbabilitySet<String> probabilitySet = new ProbabilitySet<>();
        probabilitySet.addProbability("A", 50);
        probabilitySet.addProbability("B", 20);
        probabilitySet.addProbability("C", 20);

        ProbabilitySetGenerator generator = new ProbabilitySetGenerator();
        Assertions.assertNull( generator.pickEntry(-60, probabilitySet));
    }
}
