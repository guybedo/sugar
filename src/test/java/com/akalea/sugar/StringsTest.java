package com.akalea.sugar;

import static com.akalea.sugar.Strings.*;
import static com.akalea.sugar.Collections.kv;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class StringsTest {

    // ==================== isEmpty/isBlank Tests ====================

    @Test
    public void testIsEmpty() {
        assertTrue(isEmpty(null));
        assertTrue(isEmpty(""));
        assertFalse(isEmpty(" "));
        assertFalse(isEmpty("hello"));
    }

    @Test
    public void testIsBlank() {
        assertTrue(isBlank(null));
        assertTrue(isBlank(""));
        assertTrue(isBlank("   "));
        assertTrue(isBlank("\t\n"));
        assertFalse(isBlank("hello"));
    }

    @Test
    public void testIsNotEmpty() {
        assertFalse(isNotEmpty(null));
        assertFalse(isNotEmpty(""));
        assertTrue(isNotEmpty(" "));
        assertTrue(isNotEmpty("hello"));
    }

    @Test
    public void testIsNotBlank() {
        assertFalse(isNotBlank(null));
        assertFalse(isNotBlank(""));
        assertFalse(isNotBlank("   "));
        assertTrue(isNotBlank("hello"));
    }

    // ==================== format Tests ====================

    @Test
    public void testFormat() {
        assertEquals("Hello World", format("Hello %s", "World"));
        assertEquals("42", format("%d", 42));
    }

    // ==================== join Tests ====================

    @Test
    public void testJoin() {
        assertEquals("abc", join(Arrays.asList("a", "b", "c")));
    }

    @Test
    public void testJoinWithSeparator() {
        assertEquals("a,b,c", join(Arrays.asList("a", "b", "c"), ","));
    }

    // ==================== firstNonAlphanum/firstAlphanum Tests ====================

    @Test
    public void testFirstNonAlphanum() {
        assertEquals(5, firstNonAlphanum("hello world"));
        assertEquals(-1, firstNonAlphanum("helloworld"));
        assertEquals(0, firstNonAlphanum(" hello"));
    }

    @Test
    public void testFirstAlphanum() {
        assertEquals(0, firstAlphanum("hello"));
        assertEquals(3, firstAlphanum("   hello"));
        assertEquals(-1, firstAlphanum("   "));
    }

    // ==================== split Tests ====================

    @Test
    public void testSplit() {
        assertEquals(Arrays.asList("a", "b", "c"), split("a,b,c", ","));
        assertEquals(Arrays.asList("hello"), split("hello", ","));
    }

    @Test
    public void testSplitNull() {
        assertTrue(split(null, ",").isEmpty());
    }

    @Test
    public void testSplitRegex() {
        assertEquals(Arrays.asList("a", "b", "c"), splitRegex("a1b2c", "\\d"));
    }

    @Test
    public void testSplitRegexNull() {
        assertTrue(splitRegex(null, "\\d").isEmpty());
    }

    // ==================== truncate Tests ====================

    @Test
    public void testTruncate() {
        assertEquals("hello...", truncate("hello world", 8));
        assertEquals("hello", truncate("hello", 10));
    }

    @Test
    public void testTruncateCustomSuffix() {
        assertEquals("he--", truncate("hello world", 4, "--"));
    }

    @Test
    public void testTruncateNull() {
        assertNull(truncate(null, 10));
    }

    @Test
    public void testTruncateShortSuffix() {
        assertEquals(".", truncate("hello", 1, "..."));
    }

    // ==================== pad Tests ====================

    @Test
    public void testPadLeft() {
        assertEquals("00042", padLeft("42", 5, '0'));
        assertEquals("hello", padLeft("hello", 3, '0'));
    }

    @Test
    public void testPadLeftNull() {
        assertEquals("000", padLeft(null, 3, '0'));
    }

    @Test
    public void testPadRight() {
        assertEquals("42000", padRight("42", 5, '0'));
        assertEquals("hello", padRight("hello", 3, '0'));
    }

    @Test
    public void testPadRightNull() {
        assertEquals("000", padRight(null, 3, '0'));
    }

    // ==================== repeat Tests ====================

    @Test
    public void testRepeat() {
        assertEquals("aaa", repeat("a", 3));
        assertEquals("", repeat("a", 0));
        assertEquals("", repeat("a", -1));
    }

    @Test
    public void testRepeatNull() {
        assertEquals("", repeat(null, 3));
    }

    // ==================== reverse Tests ====================

    @Test
    public void testReverse() {
        assertEquals("olleh", reverse("hello"));
        assertNull(reverse(null));
    }

    // ==================== capitalize/uncapitalize Tests ====================

    @Test
    public void testCapitalize() {
        assertEquals("Hello", capitalize("hello"));
        assertEquals("", capitalize(""));
        assertNull(capitalize(null));
    }

    @Test
    public void testUncapitalize() {
        assertEquals("hello", uncapitalize("Hello"));
        assertEquals("", uncapitalize(""));
        assertNull(uncapitalize(null));
    }

    // ==================== Case Conversion Tests ====================

    @Test
    public void testCamelCase() {
        assertEquals("helloWorld", camelCase("hello_world"));
        assertEquals("helloWorld", camelCase("hello-world"));
        assertEquals("helloWorld", camelCase("hello world"));
        assertEquals("", camelCase(""));
        assertNull(camelCase(null));
    }

    @Test
    public void testSnakeCase() {
        assertEquals("hello_world", snakeCase("helloWorld"));
        assertEquals("hello_world", snakeCase("hello-world"));
        assertEquals("hello_world", snakeCase("hello world"));
        assertEquals("", snakeCase(""));
        assertNull(snakeCase(null));
    }

    @Test
    public void testKebabCase() {
        assertEquals("hello-world", kebabCase("helloWorld"));
        assertEquals("hello-world", kebabCase("hello_world"));
        assertEquals("hello-world", kebabCase("hello world"));
        assertEquals("", kebabCase(""));
        assertNull(kebabCase(null));
    }

    // ==================== template Tests ====================

    @Test
    public void testTemplateMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "World");
        values.put("count", 42);
        assertEquals("Hello World, count: 42", template("Hello ${name}, count: ${count}", values));
    }

    @Test
    public void testTemplateMapNull() {
        assertNull(template(null, new HashMap<>()));
        assertEquals("hello", template("hello", (Map<String, Object>) null));
    }

    @Test
    public void testTemplateMapNullValue() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", null);
        assertEquals("Hello ", template("Hello ${name}", values));
    }

    @Test
    public void testTemplateKeyValues() {
        assertEquals("Hello World", template("Hello ${name}", kv("name", "World")));
    }

    @Test
    public void testTemplateKeyValuesNull() {
        assertNull(template(null, kv("a", "b")));
    }

    @Test
    public void testTemplateKeyValuesNullValue() {
        assertEquals("Hello ", template("Hello ${name}", kv("name", null)));
    }

    // ==================== contains Tests ====================

    @Test
    public void testContains() {
        assertTrue(contains("hello world", "world"));
        assertFalse(contains("hello world", "foo"));
        assertFalse(contains(null, "foo"));
        assertFalse(contains("hello", null));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(containsIgnoreCase("Hello World", "WORLD"));
        assertFalse(containsIgnoreCase("hello", "foo"));
        assertFalse(containsIgnoreCase(null, "foo"));
        assertFalse(containsIgnoreCase("hello", null));
    }

    // ==================== startsWith/endsWith Tests ====================

    @Test
    public void testStartsWith() {
        assertTrue(startsWith("hello world", "hello"));
        assertFalse(startsWith("hello", "world"));
        assertFalse(startsWith(null, "hello"));
        assertFalse(startsWith("hello", null));
    }

    @Test
    public void testEndsWith() {
        assertTrue(endsWith("hello world", "world"));
        assertFalse(endsWith("hello", "world"));
        assertFalse(endsWith(null, "world"));
        assertFalse(endsWith("hello", null));
    }

    // ==================== removePrefix/removeSuffix Tests ====================

    @Test
    public void testRemovePrefix() {
        assertEquals("world", removePrefix("hello world", "hello "));
        assertEquals("hello", removePrefix("hello", "foo"));
        assertNull(removePrefix(null, "foo"));
        assertEquals("hello", removePrefix("hello", null));
    }

    @Test
    public void testRemoveSuffix() {
        assertEquals("hello", removeSuffix("hello world", " world"));
        assertEquals("hello", removeSuffix("hello", "foo"));
        assertNull(removeSuffix(null, "foo"));
        assertEquals("hello", removeSuffix("hello", null));
    }

    // ==================== substring Tests ====================

    @Test
    public void testSubstringBefore() {
        assertEquals("hello", substringBefore("hello world", " "));
        assertEquals("hello", substringBefore("hello", " "));
        assertNull(substringBefore(null, " "));
        assertEquals("hello", substringBefore("hello", null));
    }

    @Test
    public void testSubstringAfter() {
        assertEquals("world", substringAfter("hello world", " "));
        assertEquals("", substringAfter("hello", " "));
        assertNull(substringAfter(null, " "));
        assertEquals("hello", substringAfter("hello", null));
    }

    @Test
    public void testSubstringBeforeLast() {
        assertEquals("hello world", substringBeforeLast("hello world end", " "));
        assertEquals("hello", substringBeforeLast("hello", " "));
        assertNull(substringBeforeLast(null, " "));
        assertEquals("hello", substringBeforeLast("hello", null));
    }

    @Test
    public void testSubstringAfterLast() {
        assertEquals("end", substringAfterLast("hello world end", " "));
        assertEquals("", substringAfterLast("hello", " "));
        assertNull(substringAfterLast(null, " "));
        assertEquals("hello", substringAfterLast("hello", null));
    }

    // ==================== count Tests ====================

    @Test
    public void testCountOccurrences() {
        assertEquals(3, countOccurrences("ababa", "a"));
        assertEquals(0, countOccurrences("hello", "x"));
        assertEquals(0, countOccurrences(null, "a"));
        assertEquals(0, countOccurrences("hello", null));
        assertEquals(0, countOccurrences("", "a"));
        assertEquals(0, countOccurrences("hello", ""));
    }

    @Test
    public void testFindAllIndexes() {
        assertEquals(Arrays.asList(0, 2, 4), findAllIndexes("ababa", "a"));
        assertTrue(findAllIndexes("hello", "x").isEmpty());
        assertTrue(findAllIndexes(null, "a").isEmpty());
        assertTrue(findAllIndexes("hello", null).isEmpty());
    }

    // ==================== default Tests ====================

    @Test
    public void testDefaultIfEmpty() {
        assertEquals("default", defaultIfEmpty(null, "default"));
        assertEquals("default", defaultIfEmpty("", "default"));
        assertEquals(" ", defaultIfEmpty(" ", "default"));
        assertEquals("hello", defaultIfEmpty("hello", "default"));
    }

    @Test
    public void testDefaultIfBlank() {
        assertEquals("default", defaultIfBlank(null, "default"));
        assertEquals("default", defaultIfBlank("", "default"));
        assertEquals("default", defaultIfBlank("   ", "default"));
        assertEquals("hello", defaultIfBlank("hello", "default"));
    }

    // ==================== null/empty conversion Tests ====================

    @Test
    public void testNullToEmpty() {
        assertEquals("", nullToEmpty(null));
        assertEquals("hello", nullToEmpty("hello"));
    }

    @Test
    public void testEmptyToNull() {
        assertNull(emptyToNull(null));
        assertNull(emptyToNull(""));
        assertEquals("hello", emptyToNull("hello"));
    }

    // ==================== trim/strip Tests ====================

    @Test
    public void testTrim() {
        assertEquals("hello", trim("  hello  "));
        assertNull(trim(null));
    }

    @Test
    public void testStrip() {
        assertEquals("hello", strip("  hello  "));
        assertNull(strip(null));
    }
}
