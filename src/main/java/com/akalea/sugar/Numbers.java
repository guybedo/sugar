package com.akalea.sugar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Numeric utilities.
 */
public interface Numbers {

    /**
     * Clamps a value between min and max (inclusive).
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Clamps a value between min and max (inclusive).
     */
    public static long clamp(long value, long min, long max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Clamps a value between min and max (inclusive).
     */
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Clamps a value between min and max (inclusive).
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Clamps a comparable value between min and max (inclusive).
     */
    public static <T extends Comparable<T>> T clamp(T value, T min, T max) {
        if (value.compareTo(min) < 0) return min;
        if (value.compareTo(max) > 0) return max;
        return value;
    }

    /**
     * Returns true if value is in range [min, max] inclusive.
     */
    public static boolean inRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * Returns true if value is in range [min, max] inclusive.
     */
    public static boolean inRange(long value, long min, long max) {
        return value >= min && value <= max;
    }

    /**
     * Returns true if value is in range [min, max] inclusive.
     */
    public static boolean inRange(float value, float min, float max) {
        return value >= min && value <= max;
    }

    /**
     * Returns true if value is in range [min, max] inclusive.
     */
    public static boolean inRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    /**
     * Returns true if value is in range [min, max) exclusive on max.
     */
    public static boolean inRangeExclusive(int value, int min, int max) {
        return value >= min && value < max;
    }

    /**
     * Returns true if value is in range [min, max) exclusive on max.
     */
    public static boolean inRangeExclusive(long value, long min, long max) {
        return value >= min && value < max;
    }

    /**
     * Generates a range of integers from start (inclusive) to end (exclusive) with step.
     */
    public static List<Integer> range(int start, int end, int step) {
        List<Integer> result = new ArrayList<>();
        if (step > 0) {
            for (int i = start; i < end; i += step) {
                result.add(i);
            }
        } else if (step < 0) {
            for (int i = start; i > end; i += step) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * Generates a range of longs from start (inclusive) to end (exclusive) with step.
     */
    public static List<Long> range(long start, long end, long step) {
        List<Long> result = new ArrayList<>();
        if (step > 0) {
            for (long i = start; i < end; i += step) {
                result.add(i);
            }
        } else if (step < 0) {
            for (long i = start; i > end; i += step) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * Generates a range of doubles from start (inclusive) to end (exclusive) with step.
     */
    public static List<Double> range(double start, double end, double step) {
        List<Double> result = new ArrayList<>();
        if (step > 0) {
            for (double i = start; i < end; i += step) {
                result.add(i);
            }
        } else if (step < 0) {
            for (double i = start; i > end; i += step) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * Rounds a double to the specified number of decimal places.
     */
    public static double roundTo(double value, int decimalPlaces) {
        if (decimalPlaces < 0) {
            throw new IllegalArgumentException("Decimal places must be non-negative");
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Rounds a float to the specified number of decimal places.
     */
    public static float roundTo(float value, int decimalPlaces) {
        return (float) roundTo((double) value, decimalPlaces);
    }

    /**
     * Returns the sign of an integer: -1, 0, or 1.
     */
    public static int sign(int value) {
        return Integer.compare(value, 0);
    }

    /**
     * Returns the sign of a long: -1, 0, or 1.
     */
    public static int sign(long value) {
        return Long.compare(value, 0);
    }

    /**
     * Returns the sign of a double: -1, 0, or 1.
     */
    public static int sign(double value) {
        return Double.compare(value, 0.0);
    }

    /**
     * Linear interpolation between two values.
     */
    public static double lerp(double start, double end, double t) {
        return start + t * (end - start);
    }

    /**
     * Linear interpolation between two values.
     */
    public static float lerp(float start, float end, float t) {
        return start + t * (end - start);
    }

    /**
     * Maps a value from one range to another.
     */
    public static double mapRange(double value, double fromMin, double fromMax, double toMin, double toMax) {
        return toMin + (value - fromMin) * (toMax - toMin) / (fromMax - fromMin);
    }

    /**
     * Returns true if the value is even.
     */
    public static boolean isEven(int value) {
        return value % 2 == 0;
    }

    /**
     * Returns true if the value is odd.
     */
    public static boolean isOdd(int value) {
        return value % 2 != 0;
    }

    /**
     * Returns true if the value is even.
     */
    public static boolean isEven(long value) {
        return value % 2 == 0;
    }

    /**
     * Returns true if the value is odd.
     */
    public static boolean isOdd(long value) {
        return value % 2 != 0;
    }

    /**
     * Parses an integer safely, returning a default value on failure.
     */
    public static int parseInt(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Parses a long safely, returning a default value on failure.
     */
    public static long parseLong(String str, long defaultValue) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Parses a double safely, returning a default value on failure.
     */
    public static double parseDouble(String str, double defaultValue) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Parses a float safely, returning a default value on failure.
     */
    public static float parseFloat(String str, float defaultValue) {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
