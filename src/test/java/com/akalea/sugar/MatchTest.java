package com.akalea.sugar;

import static org.junit.Assert.*;

import org.junit.Test;

public class MatchTest {

    // ==================== Basic Matching Tests ====================

    @Test
    public void testMatchExactValue() {
        String result = Match.of(200)
            .when(200, () -> "OK")
            .when(404, () -> "Not Found")
            .otherwise(() -> "Unknown");

        assertEquals("OK", result);
    }

    @Test
    public void testMatchSecondValue() {
        String result = Match.of(404)
            .when(200, () -> "OK")
            .when(404, () -> "Not Found")
            .otherwise(() -> "Unknown");

        assertEquals("Not Found", result);
    }

    @Test
    public void testMatchOtherwise() {
        String result = Match.of(500)
            .when(200, () -> "OK")
            .when(404, () -> "Not Found")
            .otherwise(() -> "Unknown");

        assertEquals("Unknown", result);
    }

    @Test
    public void testMatchOtherwiseConstant() {
        String result = Match.of(999)
            .when(200, () -> "OK")
            .otherwise("Default");

        assertEquals("Default", result);
    }

    // ==================== Predicate Matching Tests ====================

    @Test
    public void testMatchPredicate() {
        String result = Match.of(42)
            .when(x -> x > 100, () -> "Big")
            .when(x -> x > 10, () -> "Medium")
            .when(x -> x > 0, () -> "Small")
            .otherwise(() -> "Zero or negative");

        assertEquals("Medium", result);
    }

    @Test
    public void testMatchPredicateFirstWins() {
        String result = Match.of(150)
            .when(x -> x > 0, () -> "Positive")
            .when(x -> x > 100, () -> "Big")
            .otherwise(() -> "Other");

        assertEquals("Positive", result);  // First matching predicate wins
    }

    // ==================== Null Matching Tests ====================

    @Test
    public void testMatchNull() {
        String result = Match.of((Integer) null)
            .when(0, () -> "Zero")
            .whenNull(() -> "Null value")
            .otherwise(() -> "Other");

        assertEquals("Null value", result);
    }

    @Test
    public void testPredicateSkipsNull() {
        String result = Match.of((Integer) null)
            .when(x -> x > 0, () -> "Positive")  // Should not NPE
            .whenNull(() -> "Null")
            .otherwise(() -> "Other");

        assertEquals("Null", result);
    }

    // ==================== Type Matching Tests ====================

    @Test
    public void testMatchType() {
        Object value = "hello";
        String result = Match.of(value)
            .whenType(String.class, s -> "String: " + s.toUpperCase())
            .whenType(Integer.class, i -> "Integer: " + i)
            .otherwise(() -> "Unknown type");

        assertEquals("String: HELLO", result);
    }

    @Test
    public void testMatchTypeInteger() {
        Object value = 42;
        String result = Match.of(value)
            .whenType(String.class, s -> "String: " + s)
            .whenType(Integer.class, i -> "Integer: " + i)
            .otherwise(() -> "Unknown type");

        assertEquals("Integer: 42", result);
    }

    @Test
    public void testMatchTypeSupplier() {
        Object value = "test";
        String result = Match.of(value)
            .whenType(Integer.class, () -> "Number")
            .whenType(String.class, () -> "Text")
            .otherwise(() -> "Other");

        assertEquals("Text", result);
    }

    // ==================== Transform Tests ====================

    @Test
    public void testMatchWithTransformer() {
        String result = Match.of("hello")
            .when("hello", s -> s.toUpperCase())
            .otherwise(() -> "default");

        assertEquals("HELLO", result);
    }

    @Test
    public void testMatchPredicateWithTransformer() {
        Integer result = Match.of(5)
            .when(x -> x > 0, x -> x * 10)
            .otherwise(() -> 0);

        assertEquals(Integer.valueOf(50), result);
    }

    @Test
    public void testOtherwiseWithTransformer() {
        String result = Match.of("world")
            .when("hello", () -> "Greeting")
            .otherwise(s -> "Unknown: " + s);

        assertEquals("Unknown: world", result);
    }

    // ==================== WhenAny Tests ====================

    @Test
    public void testWhenAny() {
        String result = Match.of(200)
            .whenAny(() -> "Success", 200, 201, 204)
            .whenAny(() -> "Client Error", 400, 401, 404)
            .otherwise(() -> "Other");

        assertEquals("Success", result);
    }

    @Test
    public void testWhenAnySecondGroup() {
        String result = Match.of(401)
            .whenAny(() -> "Success", 200, 201, 204)
            .whenAny(() -> "Client Error", 400, 401, 404)
            .otherwise(() -> "Other");

        assertEquals("Client Error", result);
    }

    // ==================== WhenInRange Tests ====================

    @Test
    public void testWhenInRange() {
        String result = Match.of(50)
            .whenInRange(0, 25, () -> "Low")
            .whenInRange(26, 75, () -> "Medium")
            .whenInRange(76, 100, () -> "High")
            .otherwise(() -> "Out of range");

        assertEquals("Medium", result);
    }

    @Test
    public void testWhenInRangeWithTransformer() {
        String result = Match.of(50)
            .whenInRange(0, 100, x -> "Value: " + x)
            .otherwise(() -> "Out of range");

        assertEquals("Value: 50", result);
    }

    // ==================== OtherwiseThrow Tests ====================

    @Test(expected = IllegalArgumentException.class)
    public void testOtherwiseThrow() {
        Match.of(999)
            .when(200, () -> "OK")
            .otherwiseThrow(() -> new IllegalArgumentException("Invalid status"));
    }

    @Test
    public void testOtherwiseThrowNotTriggered() {
        String result = Match.of(200)
            .when(200, () -> "OK")
            .otherwiseThrow(() -> new IllegalArgumentException("Invalid status"));

        assertEquals("OK", result);
    }

    // ==================== State Tests ====================

    @Test
    public void testIsMatched() {
        Match.Matcher<Integer> matcher = Match.of(200)
            .when(200, () -> "OK");

        assertTrue(matcher.isMatched());
    }

    @Test
    public void testIsNotMatched() {
        Match.Matcher<Integer> matcher = Match.of(404)
            .when(200, () -> "OK");

        assertFalse(matcher.isMatched());
    }

    @Test
    public void testGet() {
        String result = Match.of(200)
            .when(200, () -> "OK")
            .get();

        assertEquals("OK", result);
    }

    @Test
    public void testGetNoMatch() {
        String result = Match.of(404)
            .when(200, () -> "OK")
            .get();

        assertNull(result);
    }

    @Test
    public void testToOptional() {
        assertTrue(Match.of(200).when(200, () -> "OK").toOptional().isPresent());
        assertFalse(Match.of(404).when(200, () -> "OK").toOptional().isPresent());
    }

    // ==================== Short Circuit Tests ====================

    @Test
    public void testShortCircuit() {
        int[] counter = {0};

        String result = Match.of(1)
            .when(1, () -> {
                counter[0]++;
                return "First";
            })
            .when(1, () -> {
                counter[0]++;
                return "Second";
            })
            .otherwise(() -> "Other");

        assertEquals("First", result);
        assertEquals(1, counter[0]);  // Second supplier never called
    }

    // ==================== String Matching Tests ====================

    @Test
    public void testMatchString() {
        String result = Match.of("hello")
            .when("hello", () -> "Greeting")
            .when("goodbye", () -> "Farewell")
            .otherwise(() -> "Unknown");

        assertEquals("Greeting", result);
    }

    @Test
    public void testMatchStringPredicate() {
        String result = Match.of("hello world")
            .when(s -> s.startsWith("hello"), () -> "Greeting")
            .when(s -> s.startsWith("goodbye"), () -> "Farewell")
            .otherwise(() -> "Unknown");

        assertEquals("Greeting", result);
    }
}
