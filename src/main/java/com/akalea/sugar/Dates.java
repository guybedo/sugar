package com.akalea.sugar;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * Date and time utilities for common operations.
 */
public interface Dates {

    // ==================== Formatting (existing) ====================

    static String isoDateTime(LocalDateTime dt) {
        if (dt == null) return null;
        return dt.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    static String isoDate(LocalDateTime dt) {
        if (dt == null) return null;
        return dt.format(DateTimeFormatter.ISO_DATE);
    }

    static String isoDate(LocalDate date) {
        if (date == null) return null;
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    // ==================== Current Date/Time ====================

    static LocalDateTime now() {
        return LocalDateTime.now();
    }

    static LocalDate today() {
        return LocalDate.now();
    }

    static Instant instant() {
        return Instant.now();
    }

    static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    // ==================== Parsing ====================

    static LocalDate parseDate(String str, String pattern, LocalDate defaultValue) {
        if (str == null || str.isEmpty()) return defaultValue;
        try {
            return LocalDate.parse(str, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            return defaultValue;
        }
    }

    static LocalDateTime parseDateTime(String str, String pattern, LocalDateTime defaultValue) {
        if (str == null || str.isEmpty()) return defaultValue;
        try {
            return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException e) {
            return defaultValue;
        }
    }

    static LocalDate parseIsoDate(String str, LocalDate defaultValue) {
        if (str == null || str.isEmpty()) return defaultValue;
        try {
            return LocalDate.parse(str, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            return defaultValue;
        }
    }

    static LocalDateTime parseIsoDateTime(String str, LocalDateTime defaultValue) {
        if (str == null || str.isEmpty()) return defaultValue;
        try {
            return LocalDateTime.parse(str, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException e) {
            return defaultValue;
        }
    }

    // ==================== Formatting ====================

    static String formatDate(LocalDate date, String pattern) {
        if (date == null) return null;
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    static String formatDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    // ==================== Duration Calculations ====================

    static long daysBetween(LocalDate a, LocalDate b) {
        if (a == null || b == null) return 0;
        return ChronoUnit.DAYS.between(a, b);
    }

    static long monthsBetween(LocalDate a, LocalDate b) {
        if (a == null || b == null) return 0;
        return ChronoUnit.MONTHS.between(a, b);
    }

    static long yearsBetween(LocalDate a, LocalDate b) {
        if (a == null || b == null) return 0;
        return ChronoUnit.YEARS.between(a, b);
    }

    static long hoursBetween(LocalDateTime a, LocalDateTime b) {
        if (a == null || b == null) return 0;
        return ChronoUnit.HOURS.between(a, b);
    }

    static long minutesBetween(LocalDateTime a, LocalDateTime b) {
        if (a == null || b == null) return 0;
        return ChronoUnit.MINUTES.between(a, b);
    }

    static long secondsBetween(LocalDateTime a, LocalDateTime b) {
        if (a == null || b == null) return 0;
        return ChronoUnit.SECONDS.between(a, b);
    }

    static long millisBetween(LocalDateTime a, LocalDateTime b) {
        if (a == null || b == null) return 0;
        return ChronoUnit.MILLIS.between(a, b);
    }

    // ==================== Date Arithmetic ====================

    static LocalDate addDays(LocalDate date, long days) {
        if (date == null) return null;
        return date.plusDays(days);
    }

    static LocalDate addWeeks(LocalDate date, long weeks) {
        if (date == null) return null;
        return date.plusWeeks(weeks);
    }

    static LocalDate addMonths(LocalDate date, long months) {
        if (date == null) return null;
        return date.plusMonths(months);
    }

    static LocalDate addYears(LocalDate date, long years) {
        if (date == null) return null;
        return date.plusYears(years);
    }

    static LocalDateTime addHours(LocalDateTime dateTime, long hours) {
        if (dateTime == null) return null;
        return dateTime.plusHours(hours);
    }

    static LocalDateTime addMinutes(LocalDateTime dateTime, long minutes) {
        if (dateTime == null) return null;
        return dateTime.plusMinutes(minutes);
    }

    static LocalDateTime addSeconds(LocalDateTime dateTime, long seconds) {
        if (dateTime == null) return null;
        return dateTime.plusSeconds(seconds);
    }

    // ==================== Truncation / Boundaries ====================

    static LocalDateTime startOfDay(LocalDate date) {
        if (date == null) return null;
        return date.atStartOfDay();
    }

    static LocalDateTime endOfDay(LocalDate date) {
        if (date == null) return null;
        return date.atTime(LocalTime.MAX);
    }

    static LocalDate startOfMonth(LocalDate date) {
        if (date == null) return null;
        return date.withDayOfMonth(1);
    }

    static LocalDate endOfMonth(LocalDate date) {
        if (date == null) return null;
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    static LocalDate startOfYear(LocalDate date) {
        if (date == null) return null;
        return date.withDayOfYear(1);
    }

    static LocalDate endOfYear(LocalDate date) {
        if (date == null) return null;
        return date.with(TemporalAdjusters.lastDayOfYear());
    }

    static LocalDate startOfWeek(LocalDate date) {
        if (date == null) return null;
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    static LocalDate endOfWeek(LocalDate date) {
        if (date == null) return null;
        return date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }

    // ==================== Predicates ====================

    static boolean isToday(LocalDate date) {
        if (date == null) return false;
        return date.equals(LocalDate.now());
    }

    static boolean isYesterday(LocalDate date) {
        if (date == null) return false;
        return date.equals(LocalDate.now().minusDays(1));
    }

    static boolean isTomorrow(LocalDate date) {
        if (date == null) return false;
        return date.equals(LocalDate.now().plusDays(1));
    }

    static boolean isWeekend(LocalDate date) {
        if (date == null) return false;
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    static boolean isWeekday(LocalDate date) {
        if (date == null) return false;
        return !isWeekend(date);
    }

    static boolean isFuture(LocalDate date) {
        if (date == null) return false;
        return date.isAfter(LocalDate.now());
    }

    static boolean isPast(LocalDate date) {
        if (date == null) return false;
        return date.isBefore(LocalDate.now());
    }

    static boolean isFuture(LocalDateTime dateTime) {
        if (dateTime == null) return false;
        return dateTime.isAfter(LocalDateTime.now());
    }

    static boolean isPast(LocalDateTime dateTime) {
        if (dateTime == null) return false;
        return dateTime.isBefore(LocalDateTime.now());
    }

    static boolean isSameDay(LocalDate a, LocalDate b) {
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    static boolean isBefore(LocalDate date, LocalDate reference) {
        if (date == null || reference == null) return false;
        return date.isBefore(reference);
    }

    static boolean isAfter(LocalDate date, LocalDate reference) {
        if (date == null || reference == null) return false;
        return date.isAfter(reference);
    }

    static boolean isBetween(LocalDate date, LocalDate start, LocalDate end) {
        if (date == null || start == null || end == null) return false;
        return !date.isBefore(start) && !date.isAfter(end);
    }

    static boolean isLeapYear(LocalDate date) {
        if (date == null) return false;
        return date.isLeapYear();
    }

    static boolean isLeapYear(int year) {
        return LocalDate.of(year, 1, 1).isLeapYear();
    }

    // ==================== Human-Readable Durations ====================

    static String humanize(Duration duration) {
        if (duration == null) return null;

        long seconds = Math.abs(duration.getSeconds());
        boolean negative = duration.isNegative();
        String prefix = negative ? "in " : "";
        String suffix = negative ? "" : " ago";

        if (seconds < 60) {
            return "just now";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return prefix + minutes + (minutes == 1 ? " minute" : " minutes") + suffix;
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return prefix + hours + (hours == 1 ? " hour" : " hours") + suffix;
        } else if (seconds < 2592000) { // ~30 days
            long days = seconds / 86400;
            return prefix + days + (days == 1 ? " day" : " days") + suffix;
        } else if (seconds < 31536000) { // ~365 days
            long months = seconds / 2592000;
            return prefix + months + (months == 1 ? " month" : " months") + suffix;
        } else {
            long years = seconds / 31536000;
            return prefix + years + (years == 1 ? " year" : " years") + suffix;
        }
    }

    static String humanize(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        Duration duration = Duration.between(dateTime, LocalDateTime.now());
        return humanize(duration);
    }

    static String humanize(LocalDate date) {
        if (date == null) return null;
        if (isToday(date)) return "today";
        if (isYesterday(date)) return "yesterday";
        if (isTomorrow(date)) return "tomorrow";
        return humanize(Duration.between(date.atStartOfDay(), LocalDateTime.now()));
    }

    static String humanize(long millis) {
        return humanize(Duration.ofMillis(millis));
    }

    // ==================== Conversions ====================

    static LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) return null;
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    static LocalDateTime toLocalDateTime(Instant instant, ZoneId zone) {
        if (instant == null) return null;
        return LocalDateTime.ofInstant(instant, zone != null ? zone : ZoneId.systemDefault());
    }

    static Instant toInstant(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    static Instant toInstant(LocalDateTime dateTime, ZoneId zone) {
        if (dateTime == null) return null;
        return dateTime.atZone(zone != null ? zone : ZoneId.systemDefault()).toInstant();
    }

    static long toEpochMillis(LocalDateTime dateTime) {
        if (dateTime == null) return 0;
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    static long toEpochMillis(LocalDate date) {
        if (date == null) return 0;
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    static LocalDateTime fromEpochMillis(long millis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }

    static LocalDate dateFromEpochMillis(long millis) {
        return fromEpochMillis(millis).toLocalDate();
    }

    // ==================== Utilities ====================

    static int daysInMonth(LocalDate date) {
        if (date == null) return 0;
        return date.lengthOfMonth();
    }

    static int daysInYear(LocalDate date) {
        if (date == null) return 0;
        return date.lengthOfYear();
    }

    static int dayOfYear(LocalDate date) {
        if (date == null) return 0;
        return date.getDayOfYear();
    }

    static int weekOfYear(LocalDate date) {
        if (date == null) return 0;
        return date.get(java.time.temporal.WeekFields.ISO.weekOfYear());
    }

    static DayOfWeek dayOfWeek(LocalDate date) {
        if (date == null) return null;
        return date.getDayOfWeek();
    }

    static int quarter(LocalDate date) {
        if (date == null) return 0;
        return (date.getMonthValue() - 1) / 3 + 1;
    }
}
