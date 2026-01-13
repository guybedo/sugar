package com.akalea.sugar;

import static org.junit.Assert.*;

import java.time.Duration;

import org.junit.Test;

import com.akalea.sugar.internal.Timed;

public class TimedTest {

    @Test
    public void testOf() {
        Timed<Integer> timed = Timed.of(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return 42;
        });
        assertEquals((Integer) 42, timed.getValue());
        assertTrue(timed.getMillis() >= 45);
        assertTrue(timed.getDuration().toMillis() >= 45);
    }

    @Test
    public void testOfNano() {
        Timed<Integer> timed = Timed.ofNano(() -> 42);
        assertEquals((Integer) 42, timed.getValue());
        assertTrue(timed.getNanos() >= 0);
    }

    @Test
    public void testOfRunnable() {
        final int[] counter = {0};
        Timed<Void> timed = Timed.ofRunnable(() -> counter[0] = 42);
        assertNull(timed.getValue());
        assertEquals(42, counter[0]);
        assertTrue(timed.getMillis() >= 0);
    }

    @Test
    public void testGetSeconds() {
        Timed<Integer> timed = Timed.of(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return 42;
        });
        assertTrue(timed.getSeconds() >= 0.09);
    }

    @Test
    public void testGetStartEndTime() {
        long beforeStart = System.currentTimeMillis();
        Timed<Integer> timed = Timed.of(() -> 42);
        long afterEnd = System.currentTimeMillis();

        assertTrue(timed.getStartTimeMs() >= beforeStart);
        assertTrue(timed.getEndTimeMs() <= afterEnd);
        assertTrue(timed.getEndTimeMs() >= timed.getStartTimeMs());
    }

    @Test
    public void testMap() {
        Timed<Integer> timed = Timed.of(() -> 21);
        Timed<Integer> mapped = timed.map(x -> x * 2);
        assertEquals((Integer) 42, mapped.getValue());
        assertEquals(timed.getDuration(), mapped.getDuration());
        assertEquals(timed.getStartTimeMs(), mapped.getStartTimeMs());
        assertEquals(timed.getEndTimeMs(), mapped.getEndTimeMs());
    }

    @Test
    public void testToString() {
        Timed<Integer> timed = Timed.of(() -> 42);
        String str = timed.toString();
        assertTrue(str.contains("42"));
        assertTrue(str.contains("ms"));
    }
}
