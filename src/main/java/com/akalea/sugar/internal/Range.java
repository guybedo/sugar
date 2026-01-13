package com.akalea.sugar.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a range of comparable values with configurable bounds.
 * Supports open, closed, and half-open ranges.
 */
public class Range<T extends Comparable<T>> {

    private final T lower;
    private final T upper;
    private final boolean lowerInclusive;
    private final boolean upperInclusive;

    private Range(T lower, T upper, boolean lowerInclusive, boolean upperInclusive) {
        this.lower = lower;
        this.upper = upper;
        this.lowerInclusive = lowerInclusive;
        this.upperInclusive = upperInclusive;
    }

    // ==================== Factory Methods ====================

    /**
     * Creates a closed range [lower, upper] that includes both endpoints.
     */
    public static <T extends Comparable<T>> Range<T> closed(T lower, T upper) {
        return new Range<>(lower, upper, true, true);
    }

    /**
     * Creates an open range (lower, upper) that excludes both endpoints.
     */
    public static <T extends Comparable<T>> Range<T> open(T lower, T upper) {
        return new Range<>(lower, upper, false, false);
    }

    /**
     * Creates a half-open range [lower, upper) that includes lower but excludes upper.
     */
    public static <T extends Comparable<T>> Range<T> closedOpen(T lower, T upper) {
        return new Range<>(lower, upper, true, false);
    }

    /**
     * Creates a half-open range (lower, upper] that excludes lower but includes upper.
     */
    public static <T extends Comparable<T>> Range<T> openClosed(T lower, T upper) {
        return new Range<>(lower, upper, false, true);
    }

    /**
     * Creates a range [lower, +infinity) that has no upper bound.
     */
    public static <T extends Comparable<T>> Range<T> atLeast(T lower) {
        return new Range<>(lower, null, true, false);
    }

    /**
     * Creates a range (lower, +infinity) that has no upper bound and excludes lower.
     */
    public static <T extends Comparable<T>> Range<T> greaterThan(T lower) {
        return new Range<>(lower, null, false, false);
    }

    /**
     * Creates a range (-infinity, upper] that has no lower bound.
     */
    public static <T extends Comparable<T>> Range<T> atMost(T upper) {
        return new Range<>(null, upper, false, true);
    }

    /**
     * Creates a range (-infinity, upper) that has no lower bound and excludes upper.
     */
    public static <T extends Comparable<T>> Range<T> lessThan(T upper) {
        return new Range<>(null, upper, false, false);
    }

    /**
     * Creates a range containing all values.
     */
    public static <T extends Comparable<T>> Range<T> all() {
        return new Range<>(null, null, false, false);
    }

    /**
     * Creates a range containing a single value [value, value].
     */
    public static <T extends Comparable<T>> Range<T> singleton(T value) {
        return closed(value, value);
    }

    // ==================== Accessors ====================

    public T getLower() {
        return lower;
    }

    public T getUpper() {
        return upper;
    }

    public boolean isLowerInclusive() {
        return lowerInclusive;
    }

    public boolean isUpperInclusive() {
        return upperInclusive;
    }

    public boolean hasLowerBound() {
        return lower != null;
    }

    public boolean hasUpperBound() {
        return upper != null;
    }

    // ==================== Operations ====================

    /**
     * Returns true if this range contains the specified value.
     */
    public boolean contains(T value) {
        if (value == null) {
            return false;
        }

        boolean aboveLower = true;
        boolean belowUpper = true;

        if (lower != null) {
            int cmp = value.compareTo(lower);
            aboveLower = lowerInclusive ? cmp >= 0 : cmp > 0;
        }

        if (upper != null) {
            int cmp = value.compareTo(upper);
            belowUpper = upperInclusive ? cmp <= 0 : cmp < 0;
        }

        return aboveLower && belowUpper;
    }

    /**
     * Returns true if this range contains all of the specified values.
     */
    public boolean containsAll(Iterable<T> values) {
        if (values == null) {
            return true;
        }
        for (T value : values) {
            if (!contains(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if there exists a value that is contained by both ranges.
     */
    public boolean overlaps(Range<T> other) {
        if (other == null) {
            return false;
        }

        // Check if ranges don't overlap
        if (hasUpperBound() && other.hasLowerBound()) {
            int cmp = upper.compareTo(other.lower);
            if (cmp < 0 || (cmp == 0 && !(upperInclusive && other.lowerInclusive))) {
                return false;
            }
        }

        if (hasLowerBound() && other.hasUpperBound()) {
            int cmp = lower.compareTo(other.upper);
            if (cmp > 0 || (cmp == 0 && !(lowerInclusive && other.upperInclusive))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if this range completely contains the other range.
     */
    public boolean encloses(Range<T> other) {
        if (other == null) {
            return false;
        }

        // Check lower bound
        if (other.hasLowerBound()) {
            if (!hasLowerBound()) {
                // We have no lower bound, so we enclose other's lower bound
            } else {
                int cmp = lower.compareTo(other.lower);
                if (cmp > 0) {
                    return false;
                }
                if (cmp == 0 && !lowerInclusive && other.lowerInclusive) {
                    return false;
                }
            }
        } else if (hasLowerBound()) {
            return false;
        }

        // Check upper bound
        if (other.hasUpperBound()) {
            if (!hasUpperBound()) {
                // We have no upper bound, so we enclose other's upper bound
            } else {
                int cmp = upper.compareTo(other.upper);
                if (cmp < 0) {
                    return false;
                }
                if (cmp == 0 && !upperInclusive && other.upperInclusive) {
                    return false;
                }
            }
        } else if (hasUpperBound()) {
            return false;
        }

        return true;
    }

    /**
     * Returns true if this range contains no values.
     */
    public boolean isEmpty() {
        if (!hasLowerBound() || !hasUpperBound()) {
            return false;
        }
        int cmp = lower.compareTo(upper);
        if (cmp > 0) {
            return true;
        }
        if (cmp == 0) {
            return !lowerInclusive || !upperInclusive;
        }
        return false;
    }

    /**
     * Returns the intersection of this range with another, or null if they don't overlap.
     */
    public Range<T> intersection(Range<T> other) {
        if (!overlaps(other)) {
            return null;
        }

        T newLower;
        boolean newLowerInclusive;

        if (!hasLowerBound()) {
            newLower = other.lower;
            newLowerInclusive = other.lowerInclusive;
        } else if (!other.hasLowerBound()) {
            newLower = lower;
            newLowerInclusive = lowerInclusive;
        } else {
            int cmp = lower.compareTo(other.lower);
            if (cmp > 0) {
                newLower = lower;
                newLowerInclusive = lowerInclusive;
            } else if (cmp < 0) {
                newLower = other.lower;
                newLowerInclusive = other.lowerInclusive;
            } else {
                newLower = lower;
                newLowerInclusive = lowerInclusive && other.lowerInclusive;
            }
        }

        T newUpper;
        boolean newUpperInclusive;

        if (!hasUpperBound()) {
            newUpper = other.upper;
            newUpperInclusive = other.upperInclusive;
        } else if (!other.hasUpperBound()) {
            newUpper = upper;
            newUpperInclusive = upperInclusive;
        } else {
            int cmp = upper.compareTo(other.upper);
            if (cmp < 0) {
                newUpper = upper;
                newUpperInclusive = upperInclusive;
            } else if (cmp > 0) {
                newUpper = other.upper;
                newUpperInclusive = other.upperInclusive;
            } else {
                newUpper = upper;
                newUpperInclusive = upperInclusive && other.upperInclusive;
            }
        }

        return new Range<>(newLower, newUpper, newLowerInclusive, newUpperInclusive);
    }

    /**
     * Returns the smallest range that encloses both this range and the other.
     */
    public Range<T> span(Range<T> other) {
        if (other == null) {
            return this;
        }

        T newLower;
        boolean newLowerInclusive;

        if (!hasLowerBound() || !other.hasLowerBound()) {
            newLower = null;
            newLowerInclusive = false;
        } else {
            int cmp = lower.compareTo(other.lower);
            if (cmp < 0) {
                newLower = lower;
                newLowerInclusive = lowerInclusive;
            } else if (cmp > 0) {
                newLower = other.lower;
                newLowerInclusive = other.lowerInclusive;
            } else {
                newLower = lower;
                newLowerInclusive = lowerInclusive || other.lowerInclusive;
            }
        }

        T newUpper;
        boolean newUpperInclusive;

        if (!hasUpperBound() || !other.hasUpperBound()) {
            newUpper = null;
            newUpperInclusive = false;
        } else {
            int cmp = upper.compareTo(other.upper);
            if (cmp > 0) {
                newUpper = upper;
                newUpperInclusive = upperInclusive;
            } else if (cmp < 0) {
                newUpper = other.upper;
                newUpperInclusive = other.upperInclusive;
            } else {
                newUpper = upper;
                newUpperInclusive = upperInclusive || other.upperInclusive;
            }
        }

        return new Range<>(newLower, newUpper, newLowerInclusive, newUpperInclusive);
    }

    // ==================== Integer/Long Range to List ====================

    /**
     * Converts an integer range to a list of integers.
     * Only works for bounded ranges.
     */
    public static List<Integer> toList(Range<Integer> range) {
        return toList(range, 1);
    }

    /**
     * Converts an integer range to a list of integers with a step.
     */
    public static List<Integer> toList(Range<Integer> range, int step) {
        if (range == null || !range.hasLowerBound() || !range.hasUpperBound() || step <= 0) {
            return new ArrayList<>();
        }

        List<Integer> result = new ArrayList<>();
        int start = range.lowerInclusive ? range.lower : range.lower + 1;
        int end = range.upperInclusive ? range.upper : range.upper - 1;

        for (int i = start; i <= end; i += step) {
            result.add(i);
        }
        return result;
    }

    /**
     * Converts a long range to a list of longs.
     */
    public static List<Long> toLongList(Range<Long> range) {
        return toLongList(range, 1L);
    }

    /**
     * Converts a long range to a list of longs with a step.
     */
    public static List<Long> toLongList(Range<Long> range, long step) {
        if (range == null || !range.hasLowerBound() || !range.hasUpperBound() || step <= 0) {
            return new ArrayList<>();
        }

        List<Long> result = new ArrayList<>();
        long start = range.lowerInclusive ? range.lower : range.lower + 1;
        long end = range.upperInclusive ? range.upper : range.upper - 1;

        for (long i = start; i <= end; i += step) {
            result.add(i);
        }
        return result;
    }

    // ==================== Object Methods ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range<?> range = (Range<?>) o;
        return lowerInclusive == range.lowerInclusive &&
               upperInclusive == range.upperInclusive &&
               Objects.equals(lower, range.lower) &&
               Objects.equals(upper, range.upper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lower, upper, lowerInclusive, upperInclusive);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(lowerInclusive ? "[" : "(");
        sb.append(hasLowerBound() ? lower : "-∞");
        sb.append(", ");
        sb.append(hasUpperBound() ? upper : "+∞");
        sb.append(upperInclusive ? "]" : ")");
        return sb.toString();
    }
}
