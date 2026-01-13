package com.akalea.sugar;

import static org.junit.Assert.*;

import org.junit.Test;

import com.akalea.sugar.internal.Pair;
import com.akalea.sugar.internal.Tuple3;
import com.akalea.sugar.internal.Tuple4;

public class TuplesTest {

    // ==================== Pair Tests ====================

    @Test
    public void testPairCreation() {
        Pair<String, Integer> p = new Pair<String, Integer>().setFirst("hello").setSecond(42);
        assertEquals("hello", p.getFirst());
        assertEquals((Integer) 42, p.getSecond());
    }

    @Test
    public void testPairFluentSetters() {
        Pair<String, Integer> p = new Pair<>();
        Pair<String, Integer> result = p.setFirst("a").setSecond(1);
        assertSame(p, result);
    }

    // ==================== Tuple3 Tests ====================

    @Test
    public void testTuple3DefaultConstructor() {
        Tuple3<String, Integer, Boolean> t = new Tuple3<>();
        assertNull(t.getFirst());
        assertNull(t.getSecond());
        assertNull(t.getThird());
    }

    @Test
    public void testTuple3Constructor() {
        Tuple3<String, Integer, Boolean> t = new Tuple3<>("a", 1, true);
        assertEquals("a", t.getFirst());
        assertEquals((Integer) 1, t.getSecond());
        assertEquals(true, t.getThird());
    }

    @Test
    public void testTuple3Of() {
        Tuple3<String, Integer, Boolean> t = Tuple3.of("a", 1, true);
        assertEquals("a", t.getFirst());
        assertEquals((Integer) 1, t.getSecond());
        assertEquals(true, t.getThird());
    }

    @Test
    public void testTuple3FluentSetters() {
        Tuple3<String, Integer, Boolean> t = new Tuple3<>();
        Tuple3<String, Integer, Boolean> result = t.setFirst("a").setSecond(1).setThird(true);
        assertSame(t, result);
        assertEquals("a", t.getFirst());
        assertEquals((Integer) 1, t.getSecond());
        assertEquals(true, t.getThird());
    }

    @Test
    public void testTuple3Map() {
        Tuple3<Integer, Integer, Integer> t = Tuple3.of(1, 2, 3);
        Integer sum = t.map((a, b, c) -> a + b + c);
        assertEquals((Integer) 6, sum);
    }

    @Test
    public void testTuple3MapFirst() {
        Tuple3<Integer, String, Boolean> t = Tuple3.of(1, "hello", true);
        Tuple3<String, String, Boolean> mapped = t.mapFirst(x -> "num:" + x);
        assertEquals("num:1", mapped.getFirst());
        assertEquals("hello", mapped.getSecond());
        assertEquals(true, mapped.getThird());
    }

    @Test
    public void testTuple3MapSecond() {
        Tuple3<Integer, String, Boolean> t = Tuple3.of(1, "hello", true);
        Tuple3<Integer, Integer, Boolean> mapped = t.mapSecond(s -> s.length());
        assertEquals((Integer) 1, mapped.getFirst());
        assertEquals((Integer) 5, mapped.getSecond());
        assertEquals(true, mapped.getThird());
    }

    @Test
    public void testTuple3MapThird() {
        Tuple3<Integer, String, Boolean> t = Tuple3.of(1, "hello", true);
        Tuple3<Integer, String, String> mapped = t.mapThird(b -> b ? "yes" : "no");
        assertEquals((Integer) 1, mapped.getFirst());
        assertEquals("hello", mapped.getSecond());
        assertEquals("yes", mapped.getThird());
    }

    @Test
    public void testTuple3ToPair() {
        Tuple3<String, Integer, Boolean> t = Tuple3.of("a", 1, true);
        Pair<String, Integer> p = t.toPair();
        assertEquals("a", p.getFirst());
        assertEquals((Integer) 1, p.getSecond());
    }

    @Test
    public void testTuple3Equals() {
        Tuple3<String, Integer, Boolean> t1 = Tuple3.of("a", 1, true);
        Tuple3<String, Integer, Boolean> t2 = Tuple3.of("a", 1, true);
        Tuple3<String, Integer, Boolean> t3 = Tuple3.of("b", 1, true);
        assertEquals(t1, t2);
        assertNotEquals(t1, t3);
    }

    @Test
    public void testTuple3EqualsNull() {
        Tuple3<String, Integer, Boolean> t = Tuple3.of("a", 1, true);
        assertNotEquals(t, null);
    }

    @Test
    public void testTuple3EqualsDifferentType() {
        Tuple3<String, Integer, Boolean> t = Tuple3.of("a", 1, true);
        assertNotEquals(t, "string");
    }

    @Test
    public void testTuple3EqualsSame() {
        Tuple3<String, Integer, Boolean> t = Tuple3.of("a", 1, true);
        assertEquals(t, t);
    }

    @Test
    public void testTuple3HashCode() {
        Tuple3<String, Integer, Boolean> t1 = Tuple3.of("a", 1, true);
        Tuple3<String, Integer, Boolean> t2 = Tuple3.of("a", 1, true);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    public void testTuple3ToString() {
        Tuple3<String, Integer, Boolean> t = Tuple3.of("a", 1, true);
        assertEquals("(a, 1, true)", t.toString());
    }

    // ==================== Tuple4 Tests ====================

    @Test
    public void testTuple4DefaultConstructor() {
        Tuple4<String, Integer, Boolean, Double> t = new Tuple4<>();
        assertNull(t.getFirst());
        assertNull(t.getSecond());
        assertNull(t.getThird());
        assertNull(t.getFourth());
    }

    @Test
    public void testTuple4Constructor() {
        Tuple4<String, Integer, Boolean, Double> t = new Tuple4<>("a", 1, true, 3.14);
        assertEquals("a", t.getFirst());
        assertEquals((Integer) 1, t.getSecond());
        assertEquals(true, t.getThird());
        assertEquals(3.14, t.getFourth(), 0.001);
    }

    @Test
    public void testTuple4Of() {
        Tuple4<String, Integer, Boolean, Double> t = Tuple4.of("a", 1, true, 3.14);
        assertEquals("a", t.getFirst());
        assertEquals((Integer) 1, t.getSecond());
        assertEquals(true, t.getThird());
        assertEquals(3.14, t.getFourth(), 0.001);
    }

    @Test
    public void testTuple4FluentSetters() {
        Tuple4<String, Integer, Boolean, Double> t = new Tuple4<>();
        Tuple4<String, Integer, Boolean, Double> result = t.setFirst("a").setSecond(1).setThird(true).setFourth(3.14);
        assertSame(t, result);
    }

    @Test
    public void testTuple4Map() {
        Tuple4<Integer, Integer, Integer, Integer> t = Tuple4.of(1, 2, 3, 4);
        Integer sum = t.map((a, b, c, d) -> a + b + c + d);
        assertEquals((Integer) 10, sum);
    }

    @Test
    public void testTuple4MapFirst() {
        Tuple4<Integer, String, Boolean, Double> t = Tuple4.of(1, "hello", true, 3.14);
        Tuple4<String, String, Boolean, Double> mapped = t.mapFirst(x -> "num:" + x);
        assertEquals("num:1", mapped.getFirst());
    }

    @Test
    public void testTuple4MapSecond() {
        Tuple4<Integer, String, Boolean, Double> t = Tuple4.of(1, "hello", true, 3.14);
        Tuple4<Integer, Integer, Boolean, Double> mapped = t.mapSecond(s -> s.length());
        assertEquals((Integer) 5, mapped.getSecond());
    }

    @Test
    public void testTuple4MapThird() {
        Tuple4<Integer, String, Boolean, Double> t = Tuple4.of(1, "hello", true, 3.14);
        Tuple4<Integer, String, String, Double> mapped = t.mapThird(b -> b ? "yes" : "no");
        assertEquals("yes", mapped.getThird());
    }

    @Test
    public void testTuple4MapFourth() {
        Tuple4<Integer, String, Boolean, Double> t = Tuple4.of(1, "hello", true, 3.14);
        Tuple4<Integer, String, Boolean, Integer> mapped = t.mapFourth(d -> (int) Math.floor(d));
        assertEquals((Integer) 3, mapped.getFourth());
    }

    @Test
    public void testTuple4ToTuple3() {
        Tuple4<String, Integer, Boolean, Double> t = Tuple4.of("a", 1, true, 3.14);
        Tuple3<String, Integer, Boolean> t3 = t.toTuple3();
        assertEquals("a", t3.getFirst());
        assertEquals((Integer) 1, t3.getSecond());
        assertEquals(true, t3.getThird());
    }

    @Test
    public void testTuple4ToPair() {
        Tuple4<String, Integer, Boolean, Double> t = Tuple4.of("a", 1, true, 3.14);
        Pair<String, Integer> p = t.toPair();
        assertEquals("a", p.getFirst());
        assertEquals((Integer) 1, p.getSecond());
    }

    @Test
    public void testTuple4Equals() {
        Tuple4<String, Integer, Boolean, Double> t1 = Tuple4.of("a", 1, true, 3.14);
        Tuple4<String, Integer, Boolean, Double> t2 = Tuple4.of("a", 1, true, 3.14);
        Tuple4<String, Integer, Boolean, Double> t3 = Tuple4.of("b", 1, true, 3.14);
        assertEquals(t1, t2);
        assertNotEquals(t1, t3);
    }

    @Test
    public void testTuple4EqualsNull() {
        Tuple4<String, Integer, Boolean, Double> t = Tuple4.of("a", 1, true, 3.14);
        assertNotEquals(t, null);
    }

    @Test
    public void testTuple4EqualsDifferentType() {
        Tuple4<String, Integer, Boolean, Double> t = Tuple4.of("a", 1, true, 3.14);
        assertNotEquals(t, "string");
    }

    @Test
    public void testTuple4EqualsSame() {
        Tuple4<String, Integer, Boolean, Double> t = Tuple4.of("a", 1, true, 3.14);
        assertEquals(t, t);
    }

    @Test
    public void testTuple4HashCode() {
        Tuple4<String, Integer, Boolean, Double> t1 = Tuple4.of("a", 1, true, 3.14);
        Tuple4<String, Integer, Boolean, Double> t2 = Tuple4.of("a", 1, true, 3.14);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    public void testTuple4ToString() {
        Tuple4<String, Integer, Boolean, Double> t = Tuple4.of("a", 1, true, 3.14);
        assertEquals("(a, 1, true, 3.14)", t.toString());
    }
}
