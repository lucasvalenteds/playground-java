package com.playground.java.se;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.concurrent.TimeUnit;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("jdk-8")
class DateTest {

    @Test
    void testItCanParseStringWithCustomFormat() throws ParseException {
        var pattern = "dd-MM-yyyy";

        var formatter = new SimpleDateFormat(pattern);

        assertThat(formatter.parse("31-12-2009"))
            .hasDayOfMonth(31)
            .hasMonth(Month.DECEMBER.getValue())
            .hasYear(2009);
    }

    @Test
    void testItCanParseDateAndTimeTogether() throws ParseException {
        var pattern = "yyyy-MM-dd HH:mm:ss";

        var formatter = new SimpleDateFormat(pattern);

        assertThat(formatter.parse("2009-12-31 23:59:59"))
            .hasDayOfMonth(31)
            .hasMonth(Month.DECEMBER.getValue())
            .hasYear(2009)
            .hasHourOfDay(23)
            .hasMinute(59)
            .hasSecond(59);
    }

    @Test
    void testDatesCanBeSubtracted() throws ParseException {
        var pattern = "yyyy-MM-dd";
        var formatter = new SimpleDateFormat(pattern);

        var lastTransfer = formatter.parse("2019-02-20");
        var today = formatter.parse("2019-09-13");
        var difference = today.getTime() - lastTransfer.getTime();

        assertThat(TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS))
            .isEqualTo(205L);
    }
}
