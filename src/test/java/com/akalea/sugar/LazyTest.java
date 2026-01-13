package com.akalea.sugar;

import static org.junit.Assert.*;

import org.junit.Test;

import com.akalea.sugar.internal.Lazy;

public class LazyTest {

    // ==================== Basic Creation Tests ====================

    @Test
    public void testLazyOf() {
        Lazy<String> lazy = Lazy.of(() -> "hello");
        assertNotNull(lazy);
        assertFalse(lazy.isEvaluated());
    }

    @Test
    public void testLazyEvaluated() {
        Lazy<String> lazy = Lazy.evaluated("hello");
        assertTrue(lazy.isEvaluated());
        assertEquals("hello", lazy.get());
    }

    // ==================== Get Tests ====================

    @Test
    public void testGet() {
        Lazy<Integer> lazy = Lazy.of(() -> 42);
        assertFalse(lazy.isEvaluated());
        assertEquals(Integer.valueOf(42), lazy.get());
        assertTrue(lazy.isEvaluated());
    }

    @Test
    public void testGetCachesValue() {
        int[] counter = {0};
        Lazy<Integer> lazy = Lazy.of(() -> {
            counter[0]++;
            return 42;
        });

        assertEquals(Integer.valueOf(42), lazy.get());
        assertEquals(Integer.valueOf(42), lazy.get());
        assertEquals(Integer.valueOf(42), lazy.get());
        assertEquals(1, counter[0]);  // Supplier called only once
    }

    @Test
    public void testGetOrElseNotEvaluated() {
        Lazy<String> lazy = Lazy.of(() -> "computed");
        String result = lazy.getOrElse("default");
        assertEquals("default", result);
        assertFalse(lazy.isEvaluated());  // Did not trigger evaluation
    }

    @Test
    public void testGetOrElseEvaluated() {
        Lazy<String> lazy = Lazy.of(() -> "computed");
        lazy.get();  // Force evaluation
        String result = lazy.getOrElse("default");
        assertEquals("computed", result);
    }

    @Test
    public void testGetOrDefault() {
        Lazy<String> lazy = Lazy.of(() -> "hello");
        assertEquals("hello", lazy.getOrDefault("default"));
    }

    @Test
    public void testGetOrDefaultWithException() {
        Lazy<String> lazy = Lazy.of(() -> {
            throw new RuntimeException("error");
        });
        assertEquals("default", lazy.getOrDefault("default"));
    }

    // ==================== Map Tests ====================

    @Test
    public void testMap() {
        Lazy<Integer> lazy = Lazy.of(() -> 5);
        Lazy<Integer> doubled = lazy.map(x -> x * 2);

        assertFalse(lazy.isEvaluated());
        assertFalse(doubled.isEvaluated());

        assertEquals(Integer.valueOf(10), doubled.get());
        assertTrue(lazy.isEvaluated());  // Original was evaluated to compute mapped value
        assertTrue(doubled.isEvaluated());
    }

    @Test
    public void testMapChaining() {
        Lazy<Integer> lazy = Lazy.of(() -> 2)
            .map(x -> x * 3)
            .map(x -> x + 1);

        assertEquals(Integer.valueOf(7), lazy.get());  // (2 * 3) + 1
    }

    // ==================== FlatMap Tests ====================

    @Test
    public void testFlatMap() {
        Lazy<Integer> lazy = Lazy.of(() -> 5);
        Lazy<Integer> result = lazy.flatMap(x -> Lazy.of(() -> x * 10));

        assertEquals(Integer.valueOf(50), result.get());
    }

    // ==================== Filter Tests ====================

    @Test
    public void testFilterMatches() {
        Lazy<Integer> lazy = Lazy.of(() -> 10);
        assertTrue(lazy.filter(x -> x > 5).isPresent());
    }

    @Test
    public void testFilterNotMatches() {
        Lazy<Integer> lazy = Lazy.of(() -> 3);
        assertFalse(lazy.filter(x -> x > 5).isPresent());
    }

    // ==================== IfEvaluated Tests ====================

    @Test
    public void testIfEvaluatedNotTriggered() {
        boolean[] called = {false};
        Lazy<String> lazy = Lazy.of(() -> "hello");
        lazy.ifEvaluated(s -> called[0] = true);
        assertFalse(called[0]);
    }

    @Test
    public void testIfEvaluatedTriggered() {
        boolean[] called = {false};
        Lazy<String> lazy = Lazy.of(() -> "hello");
        lazy.get();
        lazy.ifEvaluated(s -> called[0] = true);
        assertTrue(called[0]);
    }

    // ==================== Peek Tests ====================

    @Test
    public void testPeek() {
        StringBuilder sb = new StringBuilder();
        Lazy<String> lazy = Lazy.of(() -> "hello");
        lazy.peek(s -> sb.append(s));
        assertEquals("hello", sb.toString());
        assertTrue(lazy.isEvaluated());
    }

    // ==================== Conversion Tests ====================

    @Test
    public void testToOptional() {
        Lazy<String> lazy = Lazy.of(() -> "hello");
        assertTrue(lazy.toOptional().isPresent());
        assertEquals("hello", lazy.toOptional().get());
    }

    @Test
    public void testToOptionalNull() {
        Lazy<String> lazy = Lazy.of(() -> null);
        assertFalse(lazy.toOptional().isPresent());
    }

    @Test
    public void testToTry() {
        Lazy<String> lazy = Lazy.of(() -> "hello");
        assertTrue(lazy.toTry().isSuccess());
    }

    @Test
    public void testToTryFailure() {
        Lazy<String> lazy = Lazy.of(() -> {
            throw new RuntimeException("error");
        });
        assertTrue(lazy.toTry().isFailure());
    }

    // ==================== ToString Tests ====================

    @Test
    public void testToStringNotEvaluated() {
        Lazy<String> lazy = Lazy.of(() -> "hello");
        assertEquals("Lazy(<not evaluated>)", lazy.toString());
    }

    @Test
    public void testToStringEvaluated() {
        Lazy<String> lazy = Lazy.of(() -> "hello");
        lazy.get();
        assertEquals("Lazy(hello)", lazy.toString());
    }
}
