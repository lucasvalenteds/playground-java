package com.playground.java.se;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("jdk-8")
class DateTimeTest {

    @Nested
    class LocalDateTest {

        @DisplayName("It can provides the current date")
        @Test
        void testCurrentDate() {
            assertThat(LocalDate.now())
                    .isAfter(LocalDate.of(2000, 10, 10))
                    .isInstanceOf(LocalDate.class);
        }

        @DisplayName("It can provides a specific date by passing day, month and year")
        @Test
        void testSpecifyDayMonthAndYear() {
            int year = 2003;
            int month = 9;
            int day = 27;

            LocalDate date = LocalDate.of(year, month, day);

            assertThat(date.getMonthValue()).isEqualTo(month);
            assertThat(date.getYear()).isEqualTo(year);
            assertThat(date.getDayOfMonth()).isEqualTo(day);
        }

        @DisplayName("It can provides a date given a specific timezone")
        @Test
        void testTimezone() {
            TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of("America/Sao_Paulo"));

            LocalDate date = LocalDate.now(timeZone.toZoneId());

            assertThat(date).isInstanceOf(LocalDate.class);
        }

        @DisplayName("Invalid time unit input throws DateTimeException")
        @Test
        void testThrowsExceptions() {
            int invalidDay = 55;

            assertThatThrownBy(() -> LocalDate.of(2012, Month.DECEMBER, invalidDay))
                    .isInstanceOf(DateTimeException.class);
        }
    }

    @Nested
    class LocalTimeTest {

        @DisplayName("It can provides the current time")
        @Test
        void testCurrentTime() {
            assertThat(LocalTime.now())
                    .isInstanceOf(LocalTime.class);
        }

        @DisplayName("It can be created by passing time units")
        @Test
        void testSpecifyTimeUnits() {
            int hour = 15;
            int minute = 32;

            assertThat(LocalTime.of(hour, minute)).isAfter(LocalTime.of(hour - 1, minute - 1));
        }

        @DisplayName("It can provides a time given a specific timezone")
        @Test
        void testTimezone() {
            TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of(ZoneOffset.UTC.getId()));

            LocalTime time = LocalTime.ofInstant(Instant.now(), timeZone.toZoneId());

            assertThat(time).isInstanceOf(LocalTime.class);
        }

        @DisplayName("Invalid time unit input throws DateTimeException")
        @Test
        void testThrowsExceptions() {
            int invalidHourForADay = 40;
            assertThatThrownBy(() -> LocalTime.of(invalidHourForADay, 10))
                    .isInstanceOf(DateTimeException.class);
        }
    }

    @Nested
    class LocalDateTimeTest {

        @DisplayName("It can be created using now() method")
        @Test
        void testCreatedByNowMethod() {
            assertThat(LocalDateTime.now())
                    .isInstanceOf(LocalDateTime.class)
                    .isAfter(LocalDateTime.of(2017, 1, 1, 1, 1));
        }

        @DisplayName("It can receive time and date individually using of() method")
        @Test
        void testOfMethod() {
            LocalDateTime dateTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0, 0);

            assertThat(dateTime).isInstanceOf(LocalDateTime.class);
        }

        @DisplayName("Date and Time can be retrieved")
        @Test
        void testDateAndTimeRetrieval() {
            int year = 2018;
            int month = Month.FEBRUARY.getValue();
            int day = 12;

            int hour = 14;
            int minute = 25;
            int second = 0;
            int nanosecond = 0;

            LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, second, nanosecond);

            assertThat(dateTime).isInstanceOf(LocalDateTime.class);
            assertThat(dateTime.toLocalDate()).isEqualTo(LocalDate.of(year, month, day));
            assertThat(dateTime.toLocalTime()).isEqualTo(LocalTime.of(hour, minute, second, nanosecond));
        }

        @DisplayName("It can be created from LocalTime and LocalDate")
        @Test
        void testCreatedByOtherObjects() {
            LocalTime time = LocalTime.now();
            LocalDate date = LocalDate.now();

            LocalDateTime dateTime = LocalDateTime.of(date, time);

            assertThat(dateTime)
                    .isInstanceOf(LocalDateTime.class)
                    .isAfter(LocalDateTime.of(2017, 1, 1, 1, 1));
        }
    }
}
