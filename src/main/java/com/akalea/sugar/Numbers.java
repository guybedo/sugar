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

    // ==================== New Number Operations ====================

    /**
     * Returns the greatest common divisor of two numbers.
     */
    public static long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    /**
     * Returns the greatest common divisor of two numbers.
     */
    public static int gcd(int a, int b) {
        return (int) gcd((long) a, (long) b);
    }

    /**
     * Returns the least common multiple of two numbers.
     */
    public static long lcm(long a, long b) {
        if (a == 0 || b == 0)
            return 0;
        return Math.abs(a / gcd(a, b) * b);
    }

    /**
     * Returns the least common multiple of two numbers.
     */
    public static int lcm(int a, int b) {
        return (int) lcm((long) a, (long) b);
    }

    /**
     * Returns true if the number is prime.
     */
    public static boolean isPrime(long n) {
        if (n <= 1)
            return false;
        if (n <= 3)
            return true;
        if (n % 2 == 0 || n % 3 == 0)
            return false;
        for (long i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0)
                return false;
        }
        return true;
    }

    /**
     * Returns true if the number is prime.
     */
    public static boolean isPrime(int n) {
        return isPrime((long) n);
    }

    /**
     * Returns true if two doubles are close within epsilon.
     */
    public static boolean closeTo(double a, double b, double epsilon) {
        return Math.abs(a - b) <= epsilon;
    }

    /**
     * Returns true if two floats are close within epsilon.
     */
    public static boolean closeTo(float a, float b, float epsilon) {
        return Math.abs(a - b) <= epsilon;
    }

    /**
     * Calculates percentage: (part / whole) * 100.
     */
    public static double percentage(double part, double whole) {
        if (whole == 0)
            return 0;
        return (part / whole) * 100;
    }

    /**
     * Applies percentage: (percent / 100) * value.
     */
    public static double percentOf(double percent, double value) {
        return (percent / 100) * value;
    }

    /**
     * Returns the factorial of n.
     */
    public static long factorial(int n) {
        if (n < 0)
            throw new IllegalArgumentException("Factorial not defined for negative numbers");
        if (n <= 1)
            return 1;
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    /**
     * Returns the nth Fibonacci number.
     */
    public static long fibonacci(int n) {
        if (n <= 0)
            return 0;
        if (n == 1)
            return 1;
        long a = 0, b = 1;
        for (int i = 2; i <= n; i++) {
            long temp = a + b;
            a = b;
            b = temp;
        }
        return b;
    }

    /**
     * Returns true if the value is positive.
     */
    public static boolean isPositive(double n) {
        return n > 0;
    }

    /**
     * Returns true if the value is negative.
     */
    public static boolean isNegative(double n) {
        return n < 0;
    }

    /**
     * Returns true if the value is zero.
     */
    public static boolean isZero(double n) {
        return n == 0;
    }

    /**
     * Returns the absolute value.
     */
    public static double abs(double n) {
        return Math.abs(n);
    }

    /**
     * Returns the absolute value.
     */
    public static int abs(int n) {
        return Math.abs(n);
    }

    /**
     * Returns the absolute value.
     */
    public static long abs(long n) {
        return Math.abs(n);
    }

    /**
     * Returns the power of a number.
     */
    public static double pow(double base, double exponent) {
        return Math.pow(base, exponent);
    }

    /**
     * Returns the square root.
     */
    public static double sqrt(double n) {
        return Math.sqrt(n);
    }

    /**
     * Returns the minimum of two values.
     */
    public static int min(int a, int b) {
        return Math.min(a, b);
    }

    /**
     * Returns the minimum of two values.
     */
    public static long min(long a, long b) {
        return Math.min(a, b);
    }

    /**
     * Returns the minimum of two values.
     */
    public static double min(double a, double b) {
        return Math.min(a, b);
    }

    /**
     * Returns the maximum of two values.
     */
    public static int max(int a, int b) {
        return Math.max(a, b);
    }

    /**
     * Returns the maximum of two values.
     */
    public static long max(long a, long b) {
        return Math.max(a, b);
    }

    /**
     * Returns the maximum of two values.
     */
    public static double max(double a, double b) {
        return Math.max(a, b);
    }

    /**
     * Constrains a value to be a multiple of step, rounding down.
     */
    public static int quantize(int value, int step) {
        if (step <= 0)
            return value;
        return (value / step) * step;
    }

    /**
     * Constrains a value to be a multiple of step, rounding down.
     */
    public static double quantize(double value, double step) {
        if (step <= 0)
            return value;
        return Math.floor(value / step) * step;
    }

    /**
     * Returns the sum of values from 1 to n.
     */
    public static long sumTo(int n) {
        if (n <= 0)
            return 0;
        return (long) n * (n + 1) / 2;
    }
}
