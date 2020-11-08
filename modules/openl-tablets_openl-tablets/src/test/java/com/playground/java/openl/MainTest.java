package com.playground.java.openl;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openl.rules.runtime.RulesEngineFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MainTest {

    interface SampleRules {
        String decideGoodDayMessageBasedOnDayHour(int hour);
    }

    private final Path spreadsheet = Paths.get("src", "test", "resources", "sample.xlsx");
    private final RulesEngineFactory<SampleRules> rulesFactory = new RulesEngineFactory<>(spreadsheet.toString(), SampleRules.class);
    private final SampleRules rules = (SampleRules) rulesFactory.newInstance();

    @ParameterizedTest
    @ValueSource(ints = {0, 5, 11})
    void testGoodMorningMessage(int hour) {
        assertEquals("Good Morning", rules.decideGoodDayMessageBasedOnDayHour(hour));
    }

    @ParameterizedTest
    @ValueSource(ints = {15, 16, 17})
    void testGoodAfternoonMessage(int hour) {
        assertEquals("Good Afternoon", rules.decideGoodDayMessageBasedOnDayHour(hour));
    }

    @ParameterizedTest
    @ValueSource(ints = {18, 19, 20, 21})
    void testGoodEveningMessage(int hour) {
        assertEquals("Good Evening", rules.decideGoodDayMessageBasedOnDayHour(hour));
    }

    @ParameterizedTest
    @ValueSource(ints = {22, 23})
    void testGoodNightMessage(int hour) {
        assertEquals("Good Night", rules.decideGoodDayMessageBasedOnDayHour(hour));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 24})
    void testHourThatDoesNotMatchTheExpectedRangeReturnsNull(int hour) {
        assertNull(rules.decideGoodDayMessageBasedOnDayHour(hour));
    }
}
