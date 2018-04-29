package com.jepster;

import static java.time.format.DateTimeFormatter.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.time.*;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void stuff() {
        assertEquals("2016-08-24T18:38:05.507+02:00", ps("2016-08-24T18:38:05.507+0200"));
    }

    @Test
    public void shouldBeAbleToParseFullDateTime() {
        assertEquals("2007-12-03T10:15:30+05:00", ps("2007-12-03T10:15:30+05:00"));
    }

    @Test
    public void shouldBeAbleToParseFullDateTimeWithoutColon() {
        assertEquals("2007-12-03T10:15:30+05:00", ps("2007-12-03T10:15:30+0500"));
    }

    @Test
    public void shouldBeAbleToParseFullDateTimeInJune() {
        assertEquals("2007-06-03T10:15:30+01:00", ps("2007-06-03T10:15:30+01:00"));
    }

    @Test
    public void shouldBeAbleToParseWithoutTimeZoneAndDefaultToSystemDefault() {
        assertEquals("2007-06-03T10:15:30+02:00", ps("2007-06-03T10:15:30"));
    }

    @Test
    public void shouldBeAbleToParseWithoutMillis() {
        assertEquals("2007-12-03T10:15:30+05:00", ps("2007-12-03T10:15:30+05:00"));
    }

    @Test
    public void shouldBeAbleToParseSixMillis() {
        assertEquals("2007-12-03T10:15:30.123456+05:00", ps("2007-12-03T10:15:30.123456+0500"));
    }

    @Test
    public void shouldBeAbleToParseWithoutSeconds() {
        assertEquals("2007-06-03T10:15+02:00", ps("2007-06-03T10:15"));
    }

    @Test
    public void shouldBeAbleToParseDate() {
        assertEquals("2007-06-03T00:00+02:00", ps("2007-06-03"));
    }

    @Test
    public void shouldBeAbleToParseWithoutT() {
        assertEquals("2007-12-03T10:15:30.123456+05:00", ps("2007-12-03 10:15:30.123456+0500"));
    }

    @Test
    public void shouldBeAbleToParseZuluTime() {
        assertEquals("2007-12-03T10:15:30.123456Z", ps("2007-12-03 10:15:30.123456Z"));
    }

    private static OffsetDateTime parse(String dateAsString) {
        try {
            return OffsetDateTime.parse(dateAsString,formatterByString());
        } catch (DateTimeParseException exception) {
            return LocalDateTime.parse(dateAsString, formatterByString()).atZone(ZoneId.systemDefault()).toOffsetDateTime();
        }
    }

    private static String ps(String dateAsString) {
        return parse(dateAsString).toString();
    }

    private static DateTimeFormatter formatterByString() {
        return new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd['T'][' '][HH:mm[:ss][.SSSSSS][.SSS]][VV][x][xx][xxx]")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .parseDefaulting(ChronoField.NANO_OF_SECOND, 0).toFormatter();
    }


    private static DateTimeFormatter isoZonedDateTime() {
        return new DateTimeFormatterBuilder()
                .append(isoOffsetDateTime())
                .optionalStart()
                .appendLiteral('[')
                .parseCaseSensitive()
                .appendZoneRegionId()
                .appendLiteral(']')
                .toFormatter();
    }

    private static DateTimeFormatter isoOffsetDateTime() {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(isoLocalDateTime())
                .appendPattern("[VV][x][xx][xxx]")
                .parseStrict()
                .toFormatter();
    }

    private static DateTimeFormatter isoLocalDateTime() {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(ISO_LOCAL_DATE)
                .optionalStart()
                .appendPattern("['T'][' ']")
                .append(ISO_LOCAL_TIME)
                .optionalEnd()
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
                .toFormatter();
    }

}
