package com.akalea.sugar;

import static com.akalea.sugar.Dates.*;
import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;

public class DatesTest {

    // ==================== Current Date/Time Tests ====================

    @Test
    public void testNow() {
        LocalDateTime now = now();
        assertNotNull(now);
    }

    @Test
    public void testToday() {
        LocalDate today = today();
        assertNotNull(today);
        assertEquals(LocalDate.now(), today);
    }

    @Test
    public void testInstant() {
        assertNotNull(instant());
    }

    // ==================== Parsing Tests ====================

    @Test
    public void testParseDate() {
        LocalDate date = parseDate("2023-12-25", "yyyy-MM-dd", null);
        assertEquals(LocalDate.of(2023, 12, 25), date);
    }

    @Test
    public void testParseDateInvalid() {
        LocalDate defaultDate = LocalDate.of(2000, 1, 1);
        LocalDate date = parseDate("invalid", "yyyy-MM-dd", defaultDate);
        assertEquals(defaultDate, date);
    }

    @Test
    public void testParseDateNull() {
        LocalDate defaultDate = LocalDate.of(2000, 1, 1);
        LocalDate date = parseDate(null, "yyyy-MM-dd", defaultDate);
        assertEquals(defaultDate, date);
    }

    @Test
    public void testParseDateTime() {
        LocalDateTime dt = parseDateTime("2023-12-25 14:30:00", "yyyy-MM-dd HH:mm:ss", null);
        assertEquals(LocalDateTime.of(2023, 12, 25, 14, 30, 0), dt);
    }

    @Test
    public void testParseIsoDate() {
        LocalDate date = parseIsoDate("2023-12-25", null);
        assertEquals(LocalDate.of(2023, 12, 25), date);
    }

    // ==================== Formatting Tests ====================

    @Test
    public void testFormatDate() {
        LocalDate date = LocalDate.of(2023, 12, 25);
        assertEquals("25/12/2023", formatDate(date, "dd/MM/yyyy"));
    }

    @Test
    public void testFormatDateNull() {
        assertNull(formatDate(null, "yyyy-MM-dd"));
    }

    @Test
    public void testFormatDateTime() {
        LocalDateTime dt = LocalDateTime.of(2023, 12, 25, 14, 30, 0);
        assertEquals("2023-12-25 14:30:00", formatDateTime(dt, "yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void testIsoDateTime() {
        LocalDateTime dt = LocalDateTime.of(2023, 12, 25, 14, 30, 0);
        assertTrue(isoDateTime(dt).contains("2023-12-25"));
    }

    @Test
    public void testIsoDate() {
        LocalDate date = LocalDate.of(2023, 12, 25);
        assertEquals("2023-12-25", isoDate(date));
    }

    // ==================== Duration Calculation Tests ====================

    @Test
    public void testDaysBetween() {
        LocalDate a = LocalDate.of(2023, 1, 1);
        LocalDate b = LocalDate.of(2023, 1, 11);
        assertEquals(10, daysBetween(a, b));
    }

    @Test
    public void testDaysBetweenNull() {
        assertEquals(0, daysBetween(null, LocalDate.now()));
        assertEquals(0, daysBetween(LocalDate.now(), null));
    }

    @Test
    public void testMonthsBetween() {
        LocalDate a = LocalDate.of(2023, 1, 1);
        LocalDate b = LocalDate.of(2023, 4, 1);
        assertEquals(3, monthsBetween(a, b));
    }

    @Test
    public void testYearsBetween() {
        LocalDate a = LocalDate.of(2020, 1, 1);
        LocalDate b = LocalDate.of(2023, 1, 1);
        assertEquals(3, yearsBetween(a, b));
    }

    @Test
    public void testHoursBetween() {
        LocalDateTime a = LocalDateTime.of(2023, 1, 1, 10, 0);
        LocalDateTime b = LocalDateTime.of(2023, 1, 1, 15, 0);
        assertEquals(5, hoursBetween(a, b));
    }

    // ==================== Date Arithmetic Tests ====================

    @Test
    public void testAddDays() {
        LocalDate date = LocalDate.of(2023, 1, 1);
        assertEquals(LocalDate.of(2023, 1, 11), addDays(date, 10));
    }

    @Test
    public void testAddDaysNull() {
        assertNull(addDays(null, 10));
    }

    @Test
    public void testAddMonths() {
        LocalDate date = LocalDate.of(2023, 1, 15);
        assertEquals(LocalDate.of(2023, 4, 15), addMonths(date, 3));
    }

    @Test
    public void testAddYears() {
        LocalDate date = LocalDate.of(2023, 6, 15);
        assertEquals(LocalDate.of(2025, 6, 15), addYears(date, 2));
    }

    @Test
    public void testAddHours() {
        LocalDateTime dt = LocalDateTime.of(2023, 1, 1, 10, 0);
        assertEquals(LocalDateTime.of(2023, 1, 1, 15, 0), addHours(dt, 5));
    }

    // ==================== Boundary Tests ====================

    @Test
    public void testStartOfDay() {
        LocalDate date = LocalDate.of(2023, 6, 15);
        LocalDateTime start = startOfDay(date);
        assertEquals(LocalDateTime.of(2023, 6, 15, 0, 0, 0), start);
    }

    @Test
    public void testStartOfDayNull() {
        assertNull(startOfDay(null));
    }

    @Test
    public void testEndOfDay() {
        LocalDate date = LocalDate.of(2023, 6, 15);
        LocalDateTime end = endOfDay(date);
        assertEquals(23, end.getHour());
        assertEquals(59, end.getMinute());
    }

    @Test
    public void testStartOfMonth() {
        LocalDate date = LocalDate.of(2023, 6, 15);
        assertEquals(LocalDate.of(2023, 6, 1), startOfMonth(date));
    }

    @Test
    public void testEndOfMonth() {
        LocalDate date = LocalDate.of(2023, 6, 15);
        assertEquals(LocalDate.of(2023, 6, 30), endOfMonth(date));
    }

    @Test
    public void testEndOfMonthFebruary() {
        LocalDate date = LocalDate.of(2023, 2, 15);
        assertEquals(LocalDate.of(2023, 2, 28), endOfMonth(date));
    }

    @Test
    public void testStartOfYear() {
        LocalDate date = LocalDate.of(2023, 6, 15);
        assertEquals(LocalDate.of(2023, 1, 1), startOfYear(date));
    }

    @Test
    public void testEndOfYear() {
        LocalDate date = LocalDate.of(2023, 6, 15);
        assertEquals(LocalDate.of(2023, 12, 31), endOfYear(date));
    }

    @Test
    public void testStartOfWeek() {
        LocalDate wednesday = LocalDate.of(2023, 6, 14);  // Wednesday
        LocalDate monday = startOfWeek(wednesday);
        assertEquals(DayOfWeek.MONDAY, monday.getDayOfWeek());
    }

    @Test
    public void testEndOfWeek() {
        LocalDate wednesday = LocalDate.of(2023, 6, 14);  // Wednesday
        LocalDate sunday = endOfWeek(wednesday);
        assertEquals(DayOfWeek.SUNDAY, sunday.getDayOfWeek());
    }

    // ==================== Predicate Tests ====================

    @Test
    public void testIsToday() {
        assertTrue(isToday(LocalDate.now()));
        assertFalse(isToday(LocalDate.now().minusDays(1)));
    }

    @Test
    public void testIsTodayNull() {
        assertFalse(isToday(null));
    }

    @Test
    public void testIsYesterday() {
        assertTrue(isYesterday(LocalDate.now().minusDays(1)));
        assertFalse(isYesterday(LocalDate.now()));
    }

    @Test
    public void testIsTomorrow() {
        assertTrue(isTomorrow(LocalDate.now().plusDays(1)));
        assertFalse(isTomorrow(LocalDate.now()));
    }

    @Test
    public void testIsWeekend() {
        LocalDate saturday = LocalDate.of(2023, 6, 17);  // Saturday
        LocalDate sunday = LocalDate.of(2023, 6, 18);    // Sunday
        LocalDate monday = LocalDate.of(2023, 6, 19);    // Monday

        assertTrue(isWeekend(saturday));
        assertTrue(isWeekend(sunday));
        assertFalse(isWeekend(monday));
    }

    @Test
    public void testIsWeekday() {
        LocalDate monday = LocalDate.of(2023, 6, 19);    // Monday
        LocalDate saturday = LocalDate.of(2023, 6, 17);  // Saturday

        assertTrue(isWeekday(monday));
        assertFalse(isWeekday(saturday));
    }

    @Test
    public void testIsFuture() {
        assertTrue(isFuture(LocalDate.now().plusDays(1)));
        assertFalse(isFuture(LocalDate.now()));
        assertFalse(isFuture(LocalDate.now().minusDays(1)));
    }

    @Test
    public void testIsPast() {
        assertTrue(isPast(LocalDate.now().minusDays(1)));
        assertFalse(isPast(LocalDate.now()));
        assertFalse(isPast(LocalDate.now().plusDays(1)));
    }

    @Test
    public void testIsSameDay() {
        LocalDate a = LocalDate.of(2023, 6, 15);
        LocalDate b = LocalDate.of(2023, 6, 15);
        LocalDate c = LocalDate.of(2023, 6, 16);

        assertTrue(isSameDay(a, b));
        assertFalse(isSameDay(a, c));
    }

    @Test
    public void testIsBetween() {
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2023, 12, 31);
        LocalDate middle = LocalDate.of(2023, 6, 15);

        assertTrue(isBetween(middle, start, end));
        assertTrue(isBetween(start, start, end));  // Inclusive
        assertTrue(isBetween(end, start, end));    // Inclusive
        assertFalse(isBetween(LocalDate.of(2024, 1, 1), start, end));
    }

    @Test
    public void testIsLeapYear() {
        assertTrue(isLeapYear(LocalDate.of(2024, 1, 1)));
        assertFalse(isLeapYear(LocalDate.of(2023, 1, 1)));
        assertTrue(isLeapYear(2024));
        assertFalse(isLeapYear(2023));
    }

    // ==================== Humanize Tests ====================

    @Test
    public void testHumanizeDurationSeconds() {
        assertEquals("just now", humanize(Duration.ofSeconds(30)));
    }

    @Test
    public void testHumanizeDurationMinutes() {
        assertTrue(humanize(Duration.ofMinutes(5)).contains("5 minutes"));
    }

    @Test
    public void testHumanizeDurationHours() {
        assertTrue(humanize(Duration.ofHours(3)).contains("3 hours"));
    }

    @Test
    public void testHumanizeDurationDays() {
        assertTrue(humanize(Duration.ofDays(7)).contains("7 days"));
    }

    @Test
    public void testHumanizeLocalDate() {
        assertEquals("today", humanize(LocalDate.now()));
        assertEquals("yesterday", humanize(LocalDate.now().minusDays(1)));
        assertEquals("tomorrow", humanize(LocalDate.now().plusDays(1)));
    }

    // ==================== Conversion Tests ====================

    @Test
    public void testToEpochMillis() {
        LocalDateTime dt = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        long millis = toEpochMillis(dt);
        assertTrue(millis > 0);
    }

    @Test
    public void testFromEpochMillis() {
        long now = System.currentTimeMillis();
        LocalDateTime dt = fromEpochMillis(now);
        assertNotNull(dt);
    }

    // ==================== Utility Tests ====================

    @Test
    public void testDaysInMonth() {
        assertEquals(31, daysInMonth(LocalDate.of(2023, 1, 15)));
        assertEquals(28, daysInMonth(LocalDate.of(2023, 2, 15)));
        assertEquals(29, daysInMonth(LocalDate.of(2024, 2, 15)));  // Leap year
        assertEquals(30, daysInMonth(LocalDate.of(2023, 4, 15)));
    }

    @Test
    public void testDaysInYear() {
        assertEquals(365, daysInYear(LocalDate.of(2023, 1, 1)));
        assertEquals(366, daysInYear(LocalDate.of(2024, 1, 1)));  // Leap year
    }

    @Test
    public void testDayOfWeek() {
        LocalDate monday = LocalDate.of(2023, 6, 19);
        assertEquals(DayOfWeek.MONDAY, dayOfWeek(monday));
    }

    @Test
    public void testQuarter() {
        assertEquals(1, quarter(LocalDate.of(2023, 1, 15)));
        assertEquals(1, quarter(LocalDate.of(2023, 3, 31)));
        assertEquals(2, quarter(LocalDate.of(2023, 4, 1)));
        assertEquals(3, quarter(LocalDate.of(2023, 7, 15)));
        assertEquals(4, quarter(LocalDate.of(2023, 12, 25)));
    }
}
