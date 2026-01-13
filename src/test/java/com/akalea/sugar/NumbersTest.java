package com.akalea.sugar;

import static com.akalea.sugar.Numbers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class NumbersTest {

    // ==================== Clamp Tests ====================

    @Test
    public void testClampIntInRange() {
        assertEquals(5, clamp(5, 0, 10));
    }

    @Test
    public void testClampIntBelowMin() {
        assertEquals(0, clamp(-5, 0, 10));
    }

    @Test
    public void testClampIntAboveMax() {
        assertEquals(10, clamp(15, 0, 10));
    }

    @Test
    public void testClampLongInRange() {
        assertEquals(5L, clamp(5L, 0L, 10L));
    }

    @Test
    public void testClampLongBelowMin() {
        assertEquals(0L, clamp(-5L, 0L, 10L));
    }

    @Test
    public void testClampLongAboveMax() {
        assertEquals(10L, clamp(15L, 0L, 10L));
    }

    @Test
    public void testClampFloatInRange() {
        assertEquals(5.0f, clamp(5.0f, 0.0f, 10.0f), 0.001);
    }

    @Test
    public void testClampFloatBelowMin() {
        assertEquals(0.0f, clamp(-5.0f, 0.0f, 10.0f), 0.001);
    }

    @Test
    public void testClampFloatAboveMax() {
        assertEquals(10.0f, clamp(15.0f, 0.0f, 10.0f), 0.001);
    }

    @Test
    public void testClampDoubleInRange() {
        assertEquals(5.0, clamp(5.0, 0.0, 10.0), 0.001);
    }

    @Test
    public void testClampDoubleBelowMin() {
        assertEquals(0.0, clamp(-5.0, 0.0, 10.0), 0.001);
    }

    @Test
    public void testClampDoubleAboveMax() {
        assertEquals(10.0, clamp(15.0, 0.0, 10.0), 0.001);
    }

    @Test
    public void testClampComparableInRange() {
        assertEquals("b", clamp("b", "a", "c"));
    }

    @Test
    public void testClampComparableBelowMin() {
        assertEquals("a", clamp("0", "a", "c"));
    }

    @Test
    public void testClampComparableAboveMax() {
        assertEquals("c", clamp("z", "a", "c"));
    }

    // ==================== InRange Tests ====================

    @Test
    public void testInRangeIntTrue() {
        assertTrue(inRange(5, 0, 10));
    }

    @Test
    public void testInRangeIntFalse() {
        assertFalse(inRange(15, 0, 10));
    }

    @Test
    public void testInRangeIntBoundary() {
        assertTrue(inRange(0, 0, 10));
        assertTrue(inRange(10, 0, 10));
    }

    @Test
    public void testInRangeLongTrue() {
        assertTrue(inRange(5L, 0L, 10L));
    }

    @Test
    public void testInRangeLongFalse() {
        assertFalse(inRange(-1L, 0L, 10L));
    }

    @Test
    public void testInRangeFloatTrue() {
        assertTrue(inRange(5.0f, 0.0f, 10.0f));
    }

    @Test
    public void testInRangeFloatFalse() {
        assertFalse(inRange(15.0f, 0.0f, 10.0f));
    }

    @Test
    public void testInRangeDoubleTrue() {
        assertTrue(inRange(5.0, 0.0, 10.0));
    }

    @Test
    public void testInRangeDoubleFalse() {
        assertFalse(inRange(-1.0, 0.0, 10.0));
    }

    @Test
    public void testInRangeExclusiveIntTrue() {
        assertTrue(inRangeExclusive(5, 0, 10));
        assertTrue(inRangeExclusive(0, 0, 10));
    }

    @Test
    public void testInRangeExclusiveIntFalse() {
        assertFalse(inRangeExclusive(10, 0, 10)); // 10 is excluded
    }

    @Test
    public void testInRangeExclusiveLongTrue() {
        assertTrue(inRangeExclusive(5L, 0L, 10L));
    }

    @Test
    public void testInRangeExclusiveLongFalse() {
        assertFalse(inRangeExclusive(10L, 0L, 10L));
    }

    // ==================== Range Tests ====================

    @Test
    public void testRangeIntPositiveStep() {
        List<Integer> result = range(0, 5, 1);
        assertEquals(List.of(0, 1, 2, 3, 4), result);
    }

    @Test
    public void testRangeIntLargerStep() {
        List<Integer> result = range(0, 10, 2);
        assertEquals(List.of(0, 2, 4, 6, 8), result);
    }

    @Test
    public void testRangeIntNegativeStep() {
        List<Integer> result = range(10, 5, -1);
        assertEquals(List.of(10, 9, 8, 7, 6), result);
    }

    @Test
    public void testRangeIntZeroStep() {
        List<Integer> result = range(0, 5, 0);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testRangeLongPositiveStep() {
        List<Long> result = range(0L, 5L, 1L);
        assertEquals(List.of(0L, 1L, 2L, 3L, 4L), result);
    }

    @Test
    public void testRangeLongNegativeStep() {
        List<Long> result = range(10L, 5L, -2L);
        assertEquals(List.of(10L, 8L, 6L), result);
    }

    @Test
    public void testRangeLongZeroStep() {
        List<Long> result = range(0L, 5L, 0L);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testRangeDoublePositiveStep() {
        List<Double> result = range(0.0, 1.0, 0.25);
        assertEquals(4, result.size());
        assertEquals(0.0, result.get(0), 0.001);
        assertEquals(0.75, result.get(3), 0.001);
    }

    @Test
    public void testRangeDoubleNegativeStep() {
        List<Double> result = range(1.0, 0.0, -0.25);
        assertEquals(4, result.size());
    }

    @Test
    public void testRangeDoubleZeroStep() {
        List<Double> result = range(0.0, 5.0, 0.0);
        assertTrue(result.isEmpty());
    }

    // ==================== RoundTo Tests ====================

    @Test
    public void testRoundToDouble() {
        assertEquals(3.14, roundTo(3.14159, 2), 0.001);
        assertEquals(3.1, roundTo(3.14159, 1), 0.001);
        assertEquals(3.0, roundTo(3.14159, 0), 0.001);
    }

    @Test
    public void testRoundToFloat() {
        assertEquals(3.14f, roundTo(3.14159f, 2), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRoundToNegativeDecimalPlaces() {
        roundTo(3.14159, -1);
    }

    // ==================== Sign Tests ====================

    @Test
    public void testSignInt() {
        assertEquals(-1, sign(-5));
        assertEquals(0, sign(0));
        assertEquals(1, sign(5));
    }

    @Test
    public void testSignLong() {
        assertEquals(-1, sign(-5L));
        assertEquals(0, sign(0L));
        assertEquals(1, sign(5L));
    }

    @Test
    public void testSignDouble() {
        assertEquals(-1, sign(-5.0));
        assertEquals(0, sign(0.0));
        assertEquals(1, sign(5.0));
    }

    // ==================== Lerp Tests ====================

    @Test
    public void testLerpDouble() {
        assertEquals(0.0, lerp(0.0, 10.0, 0.0), 0.001);
        assertEquals(5.0, lerp(0.0, 10.0, 0.5), 0.001);
        assertEquals(10.0, lerp(0.0, 10.0, 1.0), 0.001);
    }

    @Test
    public void testLerpFloat() {
        assertEquals(0.0f, lerp(0.0f, 10.0f, 0.0f), 0.001);
        assertEquals(5.0f, lerp(0.0f, 10.0f, 0.5f), 0.001);
        assertEquals(10.0f, lerp(0.0f, 10.0f, 1.0f), 0.001);
    }

    // ==================== MapRange Tests ====================

    @Test
    public void testMapRange() {
        assertEquals(50.0, mapRange(5.0, 0.0, 10.0, 0.0, 100.0), 0.001);
        assertEquals(0.0, mapRange(0.0, 0.0, 10.0, 0.0, 100.0), 0.001);
        assertEquals(100.0, mapRange(10.0, 0.0, 10.0, 0.0, 100.0), 0.001);
    }

    // ==================== Even/Odd Tests ====================

    @Test
    public void testIsEvenInt() {
        assertTrue(isEven(0));
        assertTrue(isEven(2));
        assertTrue(isEven(-4));
        assertFalse(isEven(1));
        assertFalse(isEven(-3));
    }

    @Test
    public void testIsOddInt() {
        assertTrue(isOdd(1));
        assertTrue(isOdd(-3));
        assertFalse(isOdd(0));
        assertFalse(isOdd(2));
    }

    @Test
    public void testIsEvenLong() {
        assertTrue(isEven(0L));
        assertTrue(isEven(2L));
        assertFalse(isEven(1L));
    }

    @Test
    public void testIsOddLong() {
        assertTrue(isOdd(1L));
        assertFalse(isOdd(0L));
    }

    // ==================== Parse Tests ====================

    @Test
    public void testParseIntSuccess() {
        assertEquals(42, parseInt("42", 0));
    }

    @Test
    public void testParseIntFailure() {
        assertEquals(0, parseInt("not a number", 0));
    }

    @Test
    public void testParseLongSuccess() {
        assertEquals(42L, parseLong("42", 0L));
    }

    @Test
    public void testParseLongFailure() {
        assertEquals(0L, parseLong("not a number", 0L));
    }

    @Test
    public void testParseDoubleSuccess() {
        assertEquals(3.14, parseDouble("3.14", 0.0), 0.001);
    }

    @Test
    public void testParseDoubleFailure() {
        assertEquals(0.0, parseDouble("not a number", 0.0), 0.001);
    }

    @Test
    public void testParseFloatSuccess() {
        assertEquals(3.14f, parseFloat("3.14", 0.0f), 0.001);
    }

    @Test
    public void testParseFloatFailure() {
        assertEquals(0.0f, parseFloat("not a number", 0.0f), 0.001);
    }
}
