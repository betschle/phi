package com.neutronio.phi.util;

import com.neutronio.phi.util.metrics.UsageChecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class UsageCheckerTest {

    @Test
    public void unusedString() {
        List<String> ids = Arrays.asList("test", "test2", "test3");

        UsageChecker usageChecker = new UsageChecker(ids);

        usageChecker.record("test2");
        usageChecker.record("test9");
        usageChecker.record("test");

        List<String> unusedStrings = usageChecker.getUnusedStrings();
        Assertions.assertTrue( unusedStrings.size() == 1);
        Assertions.assertTrue( unusedStrings.contains("test3"));
        Assertions.assertFalse( unusedStrings.contains("test2"));
    }

    @Test
    public void nothingRecorded() {
        List<String> ids = Arrays.asList("test", "test2", "test3");

        UsageChecker usageChecker = new UsageChecker(ids);

        usageChecker.record("Dada");
        usageChecker.record("lalal");
        usageChecker.record("lalal");

        List<String> unusedStrings = usageChecker.getUnusedStrings();
        Assertions.assertTrue( unusedStrings.size() == 3);
        Assertions.assertTrue( unusedStrings.contains("test3"));
        Assertions.assertTrue( unusedStrings.contains("test2"));
    }
}
