package com.akalea.sugar;

import static com.akalea.sugar.Pojos.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

import com.akalea.sugar.internal.Pair;
import com.akalea.sugar.internal.Param;

public class PojosTest {

    // Test classes
    static class Person {
        public String name;
        public int age;
        public Address address;
    }

    static class Address {
        public String city;
    }

    static class Parent {
        public String parentField;
    }

    static class Child extends Parent {
        public String childField;
    }

    // ==================== getAllInheritedFields Tests ====================

    @Test
    public void testGetAllInheritedFieldsObject() {
        Person p = new Person();
        List<Field> fields = getAllInheritedFields(p);
        assertTrue(fields.size() >= 3);
    }

    @Test
    public void testGetAllInheritedFieldsClass() {
        List<Field> fields = getAllInheritedFields(Child.class);
        assertTrue(fields.stream().anyMatch(f -> f.getName().equals("childField")));
        assertTrue(fields.stream().anyMatch(f -> f.getName().equals("parentField")));
    }

    // ==================== toMap Tests ====================

    @Test
    public void testToMap() {
        Person p = new Person();
        p.name = "John";
        p.age = 30;
        Map<String, Object> map = toMap(p);
        assertEquals("John", map.get("name"));
        assertEquals(30, map.get("age"));
    }

    @Test
    public void testToMapNull() {
        assertNull(toMap(null));
    }

    @Test
    public void testToMapWithConverters() {
        Person p = new Person();
        p.name = "John";
        Map<Class, java.util.function.Function<Object, Object>> converters = new java.util.HashMap<>();
        converters.put(String.class, o -> ((String) o).toUpperCase());
        Map<String, Object> map = toMap(p, converters);
        assertEquals("JOHN", map.get("name"));
    }

    // ==================== ifPresent Tests ====================

    @Test
    public void testIfPresentNotNull() {
        StringBuilder sb = new StringBuilder();
        ifPresent("hello", s -> sb.append(s));
        assertEquals("hello", sb.toString());
    }

    @Test
    public void testIfPresentNull() {
        StringBuilder sb = new StringBuilder();
        ifPresent(null, s -> sb.append(s));
        assertEquals("", sb.toString());
    }

    // ==================== orElse Tests ====================

    @Test
    public void testOrElseValue() {
        assertEquals("hello", orElse("hello", "default"));
        assertEquals("default", orElse(null, "default"));
    }

    @Test
    public void testOrElseFunction() {
        assertEquals((Integer) 5, orElse("hello", (String s) -> s.length(), 0));
        assertEquals((Integer) 0, orElse(null, (String s) -> s.length(), 0));
    }

    @Test
    public void testOrElseSupplier() {
        assertEquals("hello", orElse("hello", () -> "default"));
        assertEquals("default", orElse(null, () -> "default"));
    }

    @Test
    public void testOrElseSupplierNull() {
        assertEquals("hello", orElse("hello", (java.util.function.Supplier<String>) null));
        assertNull(orElse(null, (java.util.function.Supplier<String>) null));
    }

    // ==================== apply Tests ====================

    @Test
    public void testApply() {
        assertEquals((Integer) 5, apply("hello", s -> s.length()));
        assertNull(apply(null, (String s) -> s.length()));
    }

    // ==================== p and o Tests ====================

    @Test
    public void testP() {
        Param<Integer> param = p("age", 30);
        assertEquals("age", param.getId());
        assertEquals((Integer) 30, param.getValue());
    }

    // ==================== coalesce Tests ====================

    @Test
    public void testCoalesce() {
        assertEquals("first", coalesce("first", "second", "third"));
        assertEquals("second", coalesce(null, "second", "third"));
        assertEquals("third", coalesce(null, null, "third"));
        assertNull(coalesce(null, null, null));
    }

    // ==================== when/unless Tests ====================

    @Test
    public void testWhenSupplierTrue() {
        Optional<String> result = when(true, () -> "value");
        assertTrue(result.isPresent());
        assertEquals("value", result.get());
    }

    @Test
    public void testWhenSupplierFalse() {
        Optional<String> result = when(false, () -> "value");
        assertFalse(result.isPresent());
    }

    @Test
    public void testUnlessSupplierTrue() {
        Optional<String> result = unless(true, () -> "value");
        assertFalse(result.isPresent());
    }

    @Test
    public void testUnlessSupplierFalse() {
        Optional<String> result = unless(false, () -> "value");
        assertTrue(result.isPresent());
    }

    @Test
    public void testWhenRunnableTrue() {
        final int[] counter = {0};
        when(true, () -> counter[0] = 42);
        assertEquals(42, counter[0]);
    }

    @Test
    public void testWhenRunnableFalse() {
        final int[] counter = {0};
        when(false, () -> counter[0] = 42);
        assertEquals(0, counter[0]);
    }

    @Test
    public void testUnlessRunnableTrue() {
        final int[] counter = {0};
        unless(true, () -> counter[0] = 42);
        assertEquals(0, counter[0]);
    }

    @Test
    public void testUnlessRunnableFalse() {
        final int[] counter = {0};
        unless(false, () -> counter[0] = 42);
        assertEquals(42, counter[0]);
    }

    // ==================== isNull/isNotNull Tests ====================

    @Test
    public void testIsNull() {
        assertTrue(isNull(null));
        assertFalse(isNull("hello"));
    }

    @Test
    public void testIsNotNull() {
        assertFalse(isNotNull(null));
        assertTrue(isNotNull("hello"));
    }

    // ==================== requireNonNull Tests ====================

    @Test
    public void testRequireNonNullSuccess() {
        assertEquals("hello", requireNonNull("hello", "must not be null"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequireNonNullFailure() {
        requireNonNull(null, "must not be null");
    }

    @Test
    public void testRequireNonNullSupplierSuccess() throws Exception {
        assertEquals("hello", requireNonNull("hello", () -> new Exception("error")));
    }

    @Test(expected = Exception.class)
    public void testRequireNonNullSupplierFailure() throws Exception {
        requireNonNull(null, () -> new Exception("error"));
    }

    // ==================== opt Tests ====================

    @Test
    public void testOpt() {
        assertTrue(opt("hello").isPresent());
        assertFalse(opt(null).isPresent());
    }

    // ==================== safeGet Tests ====================

    @Test
    public void testSafeGetSingleLevel() {
        Person p = new Person();
        p.name = "John";
        Optional<String> name = safeGet(p, person -> person.name);
        assertTrue(name.isPresent());
        assertEquals("John", name.get());
    }

    @Test
    public void testSafeGetSingleLevelNull() {
        Optional<String> result = safeGet(null, (Person p) -> p.name);
        assertFalse(result.isPresent());
    }

    @Test
    public void testSafeGetTwoLevels() {
        Person p = new Person();
        p.address = new Address();
        p.address.city = "NYC";
        Optional<String> city = safeGet(p, person -> person.address, addr -> addr.city);
        assertTrue(city.isPresent());
        assertEquals("NYC", city.get());
    }

    @Test
    public void testSafeGetTwoLevelsNullMiddle() {
        Person p = new Person();
        p.address = null;
        Optional<String> city = safeGet(p, person -> person.address, addr -> addr.city);
        assertFalse(city.isPresent());
    }

    @Test
    public void testSafeGetThreeLevels() {
        // For three levels, we'd need a deeper structure
        Person p = new Person();
        p.name = "John";
        Optional<Integer> len = safeGet(p, person -> person.name, name -> name.length(), len2 -> len2);
        assertTrue(len.isPresent());
        assertEquals((Integer) 4, len.get());
    }

    // ==================== chain Tests ====================

    @Test
    public void testChainSingle() {
        assertEquals((Integer) 5, chain("hello", s -> s.length()));
        assertNull(chain(null, (String s) -> s.length()));
    }

    @Test
    public void testChainTwo() {
        assertEquals("5", chain("hello", s -> s.length(), len -> len.toString()));
        assertNull(chain(null, (String s) -> s.length(), len -> len.toString()));
    }

    @Test
    public void testChainThree() {
        assertEquals((Integer) 1, chain("hello", s -> s.length(), len -> len.toString(), str -> str.length()));
    }

    @Test
    public void testChainNullMiddle() {
        String result = chain("hello", s -> (String) null, s -> s.toUpperCase());
        assertNull(result);
    }

    // ==================== mapOrDefault Tests ====================

    @Test
    public void testMapOrDefault() {
        assertEquals((Integer) 5, mapOrDefault("hello", s -> s.length(), 0));
        assertEquals((Integer) 0, mapOrDefault(null, (String s) -> s.length(), 0));
    }

    @Test
    public void testMapOrDefaultNullResult() {
        assertEquals("default", mapOrDefault("hello", s -> null, "default"));
    }

    // ==================== tap Tests ====================

    @Test
    public void testTap() {
        StringBuilder sb = new StringBuilder();
        String result = tap("hello", s -> sb.append(s));
        assertEquals("hello", result);
        assertEquals("hello", sb.toString());
    }

    @Test
    public void testTapNull() {
        StringBuilder sb = new StringBuilder();
        String result = tap(null, s -> sb.append(s));
        assertNull(result);
        assertEquals("", sb.toString());
    }

    // ==================== withTransform Tests ====================

    @Test
    public void testWithTransform() {
        Pair<String, Integer> result = withTransform("hello", s -> s.length());
        assertEquals("hello", result.getFirst());
        assertEquals((Integer) 5, result.getSecond());
    }

    @Test
    public void testWithTransformNull() {
        Pair<String, Integer> result = withTransform(null, (String s) -> s.length());
        assertNull(result.getFirst());
        assertNull(result.getSecond());
    }

    // ==================== equalsAny/equalsNone Tests ====================

    @Test
    public void testEqualsAny() {
        assertTrue(equalsAny("a", "a", "b", "c"));
        assertTrue(equalsAny("b", "a", "b", "c"));
        assertFalse(equalsAny("x", "a", "b", "c"));
    }

    @Test
    public void testEqualsAnyNull() {
        assertTrue(equalsAny(null, "a", null, "c"));
        assertFalse(equalsAny(null, "a", "b", "c"));
    }

    @Test
    public void testEqualsNone() {
        assertTrue(equalsNone("x", "a", "b", "c"));
        assertFalse(equalsNone("a", "a", "b", "c"));
    }
}
