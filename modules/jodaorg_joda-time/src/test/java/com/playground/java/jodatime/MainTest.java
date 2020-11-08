package com.playground.java.jodatime;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class MainTest {

    @Test
    void testItParseEnUsPatternByDefault() {
        var dateTime = DateTime.parse("2019-08-05");

        assertEquals(5, dateTime.dayOfMonth().get());
        assertEquals(8, dateTime.monthOfYear().get());
        assertEquals(2019, dateTime.year().get());
    }

    @Test
    void testItCanBeConfiguredToParsePtBrPattern() {
        var pattern = DateTimeFormat.forPattern("dd-MM-YYYY");
        var dateTime = DateTime.parse("05-05-2019", pattern);

        assertEquals(5, dateTime.dayOfMonth().get());
        assertEquals(5, dateTime.monthOfYear().get());
        assertEquals(2019, dateTime.year().get());
    }
}
