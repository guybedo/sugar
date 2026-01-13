package com.akalea.sugar;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.akalea.sugar.internal.Range;

public class RangeTest {

    // ==================== Factory Method Tests ====================

    @Test
    public void testClosed() {
        Range<Integer> r = Range.closed(1, 10);
        assertTrue(r.contains(1));
        assertTrue(r.contains(10));
        assertTrue(r.contains(5));
        assertFalse(r.contains(0));
        assertFalse(r.contains(11));
    }

    @Test
    public void testOpen() {
        Range<Integer> r = Range.open(1, 10);
        assertFalse(r.contains(1));
        assertFalse(r.contains(10));
        assertTrue(r.contains(5));
        assertTrue(r.contains(2));
        assertTrue(r.contains(9));
    }

    @Test
    public void testClosedOpen() {
        Range<Integer> r = Range.closedOpen(1, 10);
        assertTrue(r.contains(1));
        assertFalse(r.contains(10));
        assertTrue(r.contains(9));
    }

    @Test
    public void testOpenClosed() {
        Range<Integer> r = Range.openClosed(1, 10);
        assertFalse(r.contains(1));
        assertTrue(r.contains(10));
        assertTrue(r.contains(2));
    }

    @Test
    public void testAtLeast() {
        Range<Integer> r = Range.atLeast(5);
        assertFalse(r.contains(4));
        assertTrue(r.contains(5));
        assertTrue(r.contains(100));
        assertTrue(r.contains(Integer.MAX_VALUE));
    }

    @Test
    public void testAtMost() {
        Range<Integer> r = Range.atMost(5);
        assertTrue(r.contains(4));
        assertTrue(r.contains(5));
        assertFalse(r.contains(6));
        assertTrue(r.contains(Integer.MIN_VALUE));
    }

    @Test
    public void testGreaterThan() {
        Range<Integer> r = Range.greaterThan(5);
        assertFalse(r.contains(5));
        assertTrue(r.contains(6));
    }

    @Test
    public void testLessThan() {
        Range<Integer> r = Range.lessThan(5);
        assertFalse(r.contains(5));
        assertTrue(r.contains(4));
    }

    @Test
    public void testSingleton() {
        Range<Integer> r = Range.singleton(5);
        assertTrue(r.contains(5));
        assertFalse(r.contains(4));
        assertFalse(r.contains(6));
    }

    @Test
    public void testAll() {
        Range<Integer> r = Range.all();
        assertTrue(r.contains(0));
        assertTrue(r.contains(Integer.MIN_VALUE));
        assertTrue(r.contains(Integer.MAX_VALUE));
    }

    // ==================== Contains Tests ====================

    @Test
    public void testContainsNull() {
        Range<Integer> r = Range.closed(1, 10);
        assertFalse(r.contains(null));
    }

    @Test
    public void testContainsAll() {
        Range<Integer> r = Range.closed(1, 10);
        assertTrue(r.containsAll(java.util.Arrays.asList(1, 5, 10)));
        assertFalse(r.containsAll(java.util.Arrays.asList(1, 5, 11)));
    }

    @Test
    public void testContainsAllNull() {
        Range<Integer> r = Range.closed(1, 10);
        assertTrue(r.containsAll(null));
    }

    // ==================== Overlaps Tests ====================

    @Test
    public void testOverlaps() {
        Range<Integer> r1 = Range.closed(1, 10);
        Range<Integer> r2 = Range.closed(5, 15);
        assertTrue(r1.overlaps(r2));
        assertTrue(r2.overlaps(r1));
    }

    @Test
    public void testOverlapsTouching() {
        Range<Integer> r1 = Range.closed(1, 5);
        Range<Integer> r2 = Range.closed(5, 10);
        assertTrue(r1.overlaps(r2));  // Both include 5
    }

    @Test
    public void testOverlapsOpenTouching() {
        Range<Integer> r1 = Range.closedOpen(1, 5);
        Range<Integer> r2 = Range.closed(5, 10);
        assertFalse(r1.overlaps(r2));  // r1 excludes 5
    }

    @Test
    public void testOverlapsNoOverlap() {
        Range<Integer> r1 = Range.closed(1, 5);
        Range<Integer> r2 = Range.closed(10, 15);
        assertFalse(r1.overlaps(r2));
    }

    @Test
    public void testOverlapsNull() {
        Range<Integer> r = Range.closed(1, 10);
        assertFalse(r.overlaps(null));
    }

    // ==================== Encloses Tests ====================

    @Test
    public void testEncloses() {
        Range<Integer> r1 = Range.closed(1, 10);
        Range<Integer> r2 = Range.closed(3, 7);
        assertTrue(r1.encloses(r2));
        assertFalse(r2.encloses(r1));
    }

    @Test
    public void testEnclosesEqual() {
        Range<Integer> r1 = Range.closed(1, 10);
        Range<Integer> r2 = Range.closed(1, 10);
        assertTrue(r1.encloses(r2));
    }

    @Test
    public void testEnclosesOpenVsClosed() {
        Range<Integer> r1 = Range.closed(1, 10);
        Range<Integer> r2 = Range.open(1, 10);
        assertTrue(r1.encloses(r2));  // Closed encloses open
        assertFalse(r2.encloses(r1)); // Open doesn't enclose closed
    }

    // ==================== isEmpty Tests ====================

    @Test
    public void testIsEmptyFalse() {
        assertFalse(Range.closed(1, 10).isEmpty());
        assertFalse(Range.singleton(5).isEmpty());
    }

    @Test
    public void testIsEmptyOpen() {
        assertTrue(Range.open(5, 5).isEmpty());  // (5, 5) has no values
    }

    @Test
    public void testIsEmptyClosedOpen() {
        assertTrue(Range.closedOpen(5, 5).isEmpty());  // [5, 5) has no values
    }

    // ==================== Intersection Tests ====================

    @Test
    public void testIntersection() {
        Range<Integer> r1 = Range.closed(1, 10);
        Range<Integer> r2 = Range.closed(5, 15);
        Range<Integer> intersection = r1.intersection(r2);

        assertNotNull(intersection);
        assertTrue(intersection.contains(5));
        assertTrue(intersection.contains(10));
        assertFalse(intersection.contains(4));
        assertFalse(intersection.contains(11));
    }

    @Test
    public void testIntersectionNoOverlap() {
        Range<Integer> r1 = Range.closed(1, 5);
        Range<Integer> r2 = Range.closed(10, 15);
        assertNull(r1.intersection(r2));
    }

    // ==================== Span Tests ====================

    @Test
    public void testSpan() {
        Range<Integer> r1 = Range.closed(1, 5);
        Range<Integer> r2 = Range.closed(10, 15);
        Range<Integer> span = r1.span(r2);

        assertTrue(span.contains(1));
        assertTrue(span.contains(7));
        assertTrue(span.contains(15));
    }

    @Test
    public void testSpanNull() {
        Range<Integer> r = Range.closed(1, 10);
        assertEquals(r, r.span(null));
    }

    // ==================== toList Tests ====================

    @Test
    public void testToListClosed() {
        List<Integer> list = Range.toList(Range.closed(1, 5));
        assertEquals(java.util.Arrays.asList(1, 2, 3, 4, 5), list);
    }

    @Test
    public void testToListOpen() {
        List<Integer> list = Range.toList(Range.open(1, 5));
        assertEquals(java.util.Arrays.asList(2, 3, 4), list);
    }

    @Test
    public void testToListClosedOpen() {
        List<Integer> list = Range.toList(Range.closedOpen(1, 5));
        assertEquals(java.util.Arrays.asList(1, 2, 3, 4), list);
    }

    @Test
    public void testToListWithStep() {
        List<Integer> list = Range.toList(Range.closed(0, 10), 2);
        assertEquals(java.util.Arrays.asList(0, 2, 4, 6, 8, 10), list);
    }

    @Test
    public void testToListLong() {
        List<Long> list = Range.toLongList(Range.closed(1L, 5L));
        assertEquals(java.util.Arrays.asList(1L, 2L, 3L, 4L, 5L), list);
    }

    // ==================== Accessor Tests ====================

    @Test
    public void testGetters() {
        Range<Integer> r = Range.closed(1, 10);
        assertEquals(Integer.valueOf(1), r.getLower());
        assertEquals(Integer.valueOf(10), r.getUpper());
        assertTrue(r.isLowerInclusive());
        assertTrue(r.isUpperInclusive());
        assertTrue(r.hasLowerBound());
        assertTrue(r.hasUpperBound());
    }

    @Test
    public void testGettersUnbounded() {
        Range<Integer> r = Range.atLeast(5);
        assertEquals(Integer.valueOf(5), r.getLower());
        assertNull(r.getUpper());
        assertTrue(r.hasLowerBound());
        assertFalse(r.hasUpperBound());
    }

    // ==================== Equals and HashCode Tests ====================

    @Test
    public void testEquals() {
        Range<Integer> r1 = Range.closed(1, 10);
        Range<Integer> r2 = Range.closed(1, 10);
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    public void testNotEquals() {
        Range<Integer> r1 = Range.closed(1, 10);
        Range<Integer> r2 = Range.closedOpen(1, 10);
        assertNotEquals(r1, r2);
    }

    // ==================== ToString Tests ====================

    @Test
    public void testToString() {
        assertEquals("[1, 10]", Range.closed(1, 10).toString());
        assertEquals("(1, 10)", Range.open(1, 10).toString());
        assertEquals("[1, 10)", Range.closedOpen(1, 10).toString());
        assertEquals("(1, 10]", Range.openClosed(1, 10).toString());
    }

    @Test
    public void testToStringUnbounded() {
        assertTrue(Range.atLeast(5).toString().contains("+∞"));
        assertTrue(Range.atMost(5).toString().contains("-∞"));
    }
}
