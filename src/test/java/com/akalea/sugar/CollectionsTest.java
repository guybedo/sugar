package com.akalea.sugar;

import org.junit.Assert;
import org.junit.Test;
import static com.akalea.sugar.Collections.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import com.akalea.sugar.internal.KeyValue;
import com.akalea.sugar.internal.Pair;
import com.akalea.sugar.internal.Tuple3;
import com.akalea.sugar.internal.Tuple4;

public class CollectionsTest {

    @Test
    public void test() {
        List<Integer> integers = list(1, 2, 3, 4);
        Assert.assertEquals(List.of(1, 2, 3, 4), integers);

        Assert.assertEquals((Integer) 4, max(integers));
        Assert.assertEquals((Integer) 1, max(integers, (a, b) -> b.compareTo(a)));
        Assert.assertEquals((Integer) 3, max(integers, i -> i % 4));
        Assert.assertEquals((Integer) 5, max((List<Integer>) map(integers, i -> i + 1)));

        Assert.assertEquals((Integer) 1, min(integers));
        Assert.assertEquals((Integer) 4, min(integers, (a, b) -> b.compareTo(a)));
        Assert.assertEquals((Integer) 4, min(integers, i -> i % 4));
        Assert.assertEquals((Integer) 2, min((List<Integer>) map(integers, i -> i + 1)));
    }

    // ==================== Tuple Creation Tests ====================

    @Test
    public void testPair() {
        Pair<String, Integer> p = pair("hello", 42);
        assertEquals("hello", p.getFirst());
        assertEquals((Integer) 42, p.getSecond());
    }

    @Test
    public void testTuple3() {
        Tuple3<String, Integer, Boolean> t = tuple("a", 1, true);
        assertEquals("a", t.getFirst());
        assertEquals((Integer) 1, t.getSecond());
        assertTrue(t.getThird());
    }

    @Test
    public void testTuple4() {
        Tuple4<String, Integer, Boolean, Double> t = tuple("a", 1, true, 3.14);
        assertEquals("a", t.getFirst());
        assertEquals((Integer) 1, t.getSecond());
        assertTrue(t.getThird());
        assertEquals(3.14, t.getFourth(), 0.001);
    }

    // ==================== iRange Tests ====================

    @Test
    public void testIRange() {
        assertEquals(list(0, 1, 2, 3, 4), iRange(0, 5));
    }

    @Test
    public void testIRangeWithStep() {
        assertEquals(list(0, 2, 4, 6, 8), iRange(0, 10, 2));
    }

    // ==================== orElse Tests ====================

    @Test
    public void testOrElseMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        assertEquals("value", orElse(map, "key", "default"));
        assertEquals("default", orElse(map, "missing", "default"));
    }

    // ==================== notNull Tests ====================

    @Test
    public void testNotNull() {
        List<String> list = list("a", null, "b", null, "c");
        assertEquals(list("a", "b", "c"), notNull(list));
    }

    // ==================== filter Tests ====================

    @Test
    public void testFilter() {
        List<Integer> result = filter(list(1, 2, 3, 4, 5), x -> x > 2);
        assertEquals(list(3, 4, 5), result);
    }

    @Test
    public void testFilterNull() {
        assertTrue(filter(null, x -> true).isEmpty());
    }

    // ==================== any/all/exists Tests ====================

    @Test
    public void testAny() {
        assertTrue(any(list(1, 2, 3), x -> x > 2));
        assertFalse(any(list(1, 2, 3), x -> x > 10));
    }

    @Test
    public void testAll() {
        assertTrue(all(list(2, 4, 6), x -> x % 2 == 0));
        assertFalse(all(list(1, 2, 3), x -> x % 2 == 0));
    }

    @Test
    public void testExists() {
        assertTrue(exists(list(1, 2, 3), x -> x == 2));
        assertFalse(exists(list(1, 2, 3), x -> x == 10));
    }

    // ==================== findFirst Tests ====================

    @Test
    public void testFindFirst() {
        assertEquals((Integer) 3, findFirst(list(1, 2, 3, 4), x -> x > 2));
        assertNull(findFirst(list(1, 2, 3), x -> x > 10));
        assertNull(findFirst(null, x -> true));
    }

    // ==================== forEach Tests ====================

    @Test
    public void testForEach() {
        StringBuilder sb = new StringBuilder();
        forEach(list("a", "b", "c"), s -> sb.append(s));
        assertEquals("abc", sb.toString());
    }

    // ==================== map Tests ====================

    @Test
    public void testMap() {
        assertEquals(list(2, 4, 6), map(list(1, 2, 3), x -> x * 2));
        assertTrue(map(null, x -> x).isEmpty());
    }

    // ==================== reduce Tests ====================

    @Test
    public void testReduce() {
        assertEquals((Integer) 6, reduce(list(1, 2, 3), (Integer a, Integer b) -> a + b));
        assertNull(reduce(null, (Integer a, Integer b) -> a + b));
    }

    // ==================== flatMap Tests ====================

    @Test
    public void testFlatMapListOfLists() {
        List<List<Integer>> nested = list(list(1, 2), list(3, 4));
        assertEquals(list(1, 2, 3, 4), flatMap(nested));
    }

    @Test
    public void testFlatMapWithFunction() {
        List<String> result = flatMap(list("ab", "cd"), s -> list(s.split("")));
        assertEquals(list("a", "b", "c", "d"), result);
    }

    @Test
    public void testFlatMapNull() {
        assertTrue(flatMap(null, x -> list()).isEmpty());
    }

    // ==================== shuffled/sorted/reversed Tests ====================

    @Test
    public void testShuffled() {
        List<Integer> original = list(1, 2, 3, 4, 5);
        List<Integer> shuffled = shuffled(original);
        assertEquals(5, shuffled.size());
        assertTrue(shuffled.containsAll(original));
    }

    @Test
    public void testShuffledNull() {
        assertTrue(shuffled(null).isEmpty());
    }

    @Test
    public void testSorted() {
        assertEquals(list(1, 2, 3), sorted(list(3, 1, 2)));
    }

    @Test
    public void testSortedNull() {
        assertTrue(sorted(null).isEmpty());
    }

    @Test
    public void testSortedWithComparator() {
        assertEquals(list(3, 2, 1), sorted(list(1, 2, 3), (a, b) -> b.compareTo(a)));
    }

    @Test
    public void testSortedWithComparatorNull() {
        assertTrue(sorted(null, (a, b) -> 0).isEmpty());
    }

    @Test
    public void testAsc() {
        assertEquals(list(1, 2, 3), asc(list(3, 1, 2)));
    }

    @Test
    public void testDesc() {
        assertEquals(list(3, 2, 1), desc(list(1, 2, 3)));
    }

    @Test
    public void testReversed() {
        assertEquals(list(3, 2, 1), reversed(list(1, 2, 3)));
    }

    // ==================== min/max Tests ====================

    @Test
    public void testMinVarargs() {
        assertEquals((Integer) 1, min(3, 1, 2));
    }

    @Test
    public void testMinWithFunction() {
        assertEquals("a", min(list("abc", "a", "ab"), s -> s.length()));
    }

    @Test
    public void testMinVal() {
        assertEquals((Integer) 1, minVal(list("abc", "a", "ab"), s -> s.length()));
    }

    @Test
    public void testMaxVarargs() {
        assertEquals((Integer) 3, max(1, 2, 3));
    }

    @Test
    public void testMaxVal() {
        assertEquals((Integer) 3, maxVal(list("abc", "a", "ab"), s -> s.length()));
    }

    // ==================== mean Tests ====================

    @Test
    public void testMean() {
        assertEquals(2.0f, mean(list(1.0f, 2.0f, 3.0f)), 0.001);
    }

    @Test
    public void testMeanVal() {
        assertEquals(2.0f, meanVal(list("a", "bb", "ccc"), s -> (float) s.length()), 0.001);
    }

    @Test
    public void testMeanWithFunction() {
        assertEquals(2.0f, mean(list("a", "bb", "ccc"), s -> (float) s.length()), 0.001);
    }

    // ==================== kv/map/hashmap/treemap Tests ====================

    @Test
    public void testKv() {
        KeyValue<String, Integer> kv = kv("key", 42);
        assertEquals("key", kv.getKey());
        assertEquals((Integer) 42, kv.getValue());
    }

    @Test
    public void testMapFromKeyValues() {
        Map<String, Integer> m = map(kv("a", 1), kv("b", 2));
        assertEquals(2, m.size());
        assertEquals((Integer) 1, m.get("a"));
    }

    @Test
    public void testHashmap() {
        Map<String, Integer> m = hashmap(kv("a", 1));
        assertTrue(m instanceof HashMap);
    }

    @Test
    public void testTreemap() {
        Map<String, Integer> m = treemap(kv("a", 1));
        assertTrue(m instanceof java.util.TreeMap);
    }

    @Test
    public void testMapCopy() {
        Map<String, Integer> original = hashmap(kv("a", 1));
        Map<String, Integer> copy = map(original);
        assertEquals(original, copy);
        assertNotSame(original, copy);
    }

    // ==================== toMap/index/groupBy Tests ====================

    @Test
    public void testToMapWithFunction() {
        Map<String, Integer> m = toMap(list("a", "bb"), s -> s.length());
        assertEquals((Integer) 1, m.get("a"));
        assertEquals((Integer) 2, m.get("bb"));
    }

    @Test
    public void testIndex() {
        Map<Integer, String> m = index(list("a", "bb"), s -> s.length());
        assertEquals("a", m.get(1));
        assertEquals("bb", m.get(2));
    }

    @Test
    public void testToMapWithKeyValue() {
        Map<Integer, String> m = toMap(list("a", "bb"), s -> s.length(), s -> s.toUpperCase());
        assertEquals("A", m.get(1));
        assertEquals("BB", m.get(2));
    }

    @Test
    public void testGroupBy() {
        Map<Integer, List<String>> grouped = groupBy(list("a", "bb", "c", "dd"), s -> s.length());
        assertEquals(list("a", "c"), grouped.get(1));
        assertEquals(list("bb", "dd"), grouped.get(2));
    }

    @Test
    public void testApply() {
        Map<String, List<Integer>> data = new HashMap<>();
        data.put("a", list(1, 2, 3));
        Map<String, List<Integer>> result = apply(data, l -> sorted(l));
        assertEquals(list(1, 2, 3), result.get("a"));
    }

    @Test
    public void testCounts() {
        Map<Integer, Integer> counts = counts(list("a", "bb", "c", "dd"), s -> s.length());
        assertEquals((Integer) 2, counts.get(1));
        assertEquals((Integer) 2, counts.get(2));
    }

    @Test
    public void testSums() {
        Map<String, Integer> sums = sums(list("a", "aa", "b", "bb"), s -> s.substring(0, 1), s -> s.length());
        assertEquals((Integer) 3, sums.get("a")); // 1 + 2
        assertEquals((Integer) 3, sums.get("b")); // 1 + 2
    }

    @Test
    public void testToMapWithReduce() {
        Map<Integer, String> m = toMap(list("a", "b", "cc"), s -> s.length(), s -> s, (s1, s2) -> s1 + s2);
        assertEquals("ab", m.get(1));
    }

    // ==================== list Tests ====================

    @Test
    public void testListFromCollection() {
        Set<Integer> set = set(1, 2, 3);
        List<Integer> result = list(set);
        assertEquals(3, result.size());
    }

    @Test
    public void testListFromIterable() {
        Iterable<Integer> iterable = () -> list(1, 2, 3).iterator();
        List<Integer> result = list(iterable);
        assertEquals(list(1, 2, 3), result);
    }

    @Test
    public void testListVarargs() {
        assertEquals(list(1, 2, 3), list(1, 2, 3));
    }

    @Test
    public void testToList() {
        List<Integer> result = toList(list(1, 2, 3).stream());
        assertEquals(list(1, 2, 3), result);
    }

    // ==================== set Tests ====================

    @Test
    public void testSetVarargs() {
        Set<Integer> s = set(1, 2, 2, 3);
        assertEquals(3, s.size());
    }

    @Test
    public void testSetFromCollections() {
        Set<Integer> s = set(list(1, 2), list(2, 3));
        assertEquals(3, s.size());
    }

    @Test
    public void testSetWithFunction() {
        Set<Integer> s = set(list("a", "bb", "ccc"), str -> str.length());
        assertEquals(set(1, 2, 3), s);
    }

    @Test
    public void testIntersect() {
        assertEquals(set(2, 3), intersect(set(1, 2, 3), set(2, 3, 4)));
    }

    @Test
    public void testDifference() {
        assertEquals(set(1), difference(set(1, 2, 3), set(2, 3, 4)));
    }

    // ==================== first/last Tests ====================

    @Test
    public void testFirst() {
        assertEquals((Integer) 1, first(list(1, 2, 3)));
        assertNull(first(list()));
        assertNull(first(null));
    }

    @Test
    public void testFirstWithPredicate() {
        assertEquals((Integer) 3, first(list(1, 2, 3, 4), x -> x > 2));
        assertNull(first(list(1, 2, 3), x -> x > 10));
        assertNull(first(null, x -> true));
    }

    @Test
    public void testLast() {
        assertEquals((Integer) 3, last(list(1, 2, 3)));
        assertNull(last(list()));
        assertNull(last(null));
    }

    @Test
    public void testLastWithPredicate() {
        assertEquals((Integer) 4, last(list(1, 2, 3, 4), x -> x > 2));
        assertNull(last(list(1, 2, 3), x -> x > 10));
        assertNull(last(null, x -> true));
    }

    @Test
    public void testLastN() {
        assertEquals(list(3, 4, 5), last(list(1, 2, 3, 4, 5), 3));
        assertEquals(list(1, 2), last(list(1, 2), 5));
        assertNull(last(null, 3));
    }

    // ==================== count/sum Tests ====================

    @Test
    public void testCount() {
        assertEquals(3, count(list("a", "b", "a", "c", "b"), s -> s));
    }

    @Test
    public void testSum() {
        assertEquals((Integer) 6, sum(list(1, 2, 3)));
        assertEquals((Double) 6.0, sum(list(1.0, 2.0, 3.0)));
        assertEquals((Long) 6L, sum(list(1L, 2L, 3L)));
        assertEquals((Float) 6.0f, sum(list(1.0f, 2.0f, 3.0f)));
    }

    @Test
    public void testFlatCount() {
        Map<String, Long> counts = flatCount(list(list("a", "b"), list("a", "c")), x -> x);
        assertEquals((Long) 2L, counts.get("a"));
        assertEquals((Long) 1L, counts.get("b"));
    }

    // ==================== enumerate Tests ====================

    @Test
    public void testEnumerateVarargs() {
        List<KeyValue<Integer, String>> result = enumerate("a", "b", "c");
        assertEquals(3, result.size());
        assertEquals((Integer) 0, result.get(0).getKey());
        assertEquals("a", result.get(0).getValue());
    }

    @Test
    public void testEnumerateCollection() {
        List<KeyValue<Integer, String>> result = enumerate(list("a", "b"));
        assertEquals(2, result.size());
    }

    @Test
    public void testEnumerateWithConsumer() {
        StringBuilder sb = new StringBuilder();
        enumerate(list("a", "b"), (i, s) -> sb.append(i).append(":").append(s).append(","));
        assertEquals("0:a,1:b,", sb.toString());
    }

    // ==================== partition Tests ====================

    @Test
    public void testPartition() {
        List<List<Integer>> result = partition(list(1, 2, 3, 4, 5), 2);
        assertEquals(3, result.size());
        assertEquals(list(1, 2), result.get(0));
        assertEquals(list(3, 4), result.get(1));
        assertEquals(list(5), result.get(2));
    }

    // ==================== zip Tests ====================

    @Test
    public void testZip() {
        List<Pair<String, Integer>> result = zip(list("a", "b"), list(1, 2));
        assertEquals(2, result.size());
        assertEquals("a", result.get(0).getFirst());
        assertEquals((Integer) 1, result.get(0).getSecond());
    }

    // ==================== concat/append/prepend Tests ====================

    @Test
    public void testConcat() {
        assertEquals(list(1, 2, 3, 4), concat(list(1, 2), list(3, 4)));
    }

    @Test
    public void testAppend() {
        assertEquals(list(1, 2, 3), append(list(1, 2), 3));
    }

    @Test
    public void testPrepend() {
        assertEquals(list(0, 1, 2), prepend(list(1, 2), 0));
    }

    // ==================== product/combinations Tests ====================

    @Test
    public void testProduct() {
        List<List<Integer>> result = product(list(1, 2), list(3, 4));
        assertEquals(4, result.size());
        assertTrue(result.contains(list(1, 3)));
        assertTrue(result.contains(list(1, 4)));
        assertTrue(result.contains(list(2, 3)));
        assertTrue(result.contains(list(2, 4)));
    }

    @Test
    public void testCombinations() {
        Map<String, List> params = new HashMap<>();
        params.put("a", list(1, 2));
        params.put("b", list("x", "y"));
        List<Map<String, Object>> result = combinations(params);
        assertEquals(4, result.size());
    }

    @Test
    public void testCombinationsEmpty() {
        assertTrue(combinations(new HashMap<>()).isEmpty());
    }

    // ==================== Map accessor Tests ====================

    @Test
    public void testBool() {
        Map<String, Object> m = new HashMap<>();
        m.put("flag", true);
        assertTrue(bool(m, "flag"));
    }

    @Test
    public void testAsBool() {
        Map<String, Object> m = new HashMap<>();
        m.put("bool", true);
        m.put("int0", 0);
        m.put("int1", 1);
        m.put("strTrue", "true");
        m.put("strFalse", "false");

        assertTrue(asBool(m, "bool"));
        assertFalse(asBool(m, "int0"));
        assertTrue(asBool(m, "int1"));
        assertTrue(asBool(m, "strTrue"));
        assertFalse(asBool(m, "strFalse"));
        assertNull(asBool(m, "missing"));
    }

    @Test(expected = RuntimeException.class)
    public void testAsBoolInvalid() {
        Map<String, Object> m = new HashMap<>();
        m.put("invalid", new Object());
        asBool(m, "invalid");
    }

    @Test
    public void testStr() {
        Map<String, Object> m = new HashMap<>();
        m.put("name", "John");
        assertEquals("John", str(m, "name"));
    }

    @Test
    public void testFlt() {
        Map<String, Object> m = new HashMap<>();
        m.put("value", 3.14f);
        assertEquals(3.14f, flt(m, "value"), 0.001);
    }

    @Test
    public void testIntg() {
        Map<String, Object> m = new HashMap<>();
        m.put("value", 42);
        assertEquals((Integer) 42, intg(m, "value"));
    }

    @Test
    public void testAsInt() {
        Map<String, Object> m = new HashMap<>();
        m.put("int", 42);
        m.put("float", 3.14f);
        m.put("double", 3.14);
        m.put("string", "42");

        assertEquals((Integer) 42, asInt(m, "int"));
        assertEquals((Integer) 3, asInt(m, "float"));
        assertEquals((Integer) 3, asInt(m, "double"));
        assertEquals((Integer) 42, asInt(m, "string"));
        assertNull(asInt(m, "missing"));
    }

    @Test(expected = RuntimeException.class)
    public void testAsIntInvalid() {
        Map<String, Object> m = new HashMap<>();
        m.put("invalid", new Object());
        asInt(m, "invalid");
    }

    @Test
    public void testMapAccessor() {
        Map<String, Object> outer = new HashMap<>();
        Map<String, Object> inner = new HashMap<>();
        inner.put("key", "value");
        outer.put("nested", inner);
        assertEquals(inner, map(outer, "nested"));
    }

    // ==================== distinctBy Tests ====================

    @Test
    public void testDistinctBy() {
        List<String> result = distinctBy(list("a", "bb", "c", "dd", "eee"), s -> s.length());
        assertEquals(3, result.size());
        assertEquals("a", result.get(0));
        assertEquals("bb", result.get(1));
        assertEquals("eee", result.get(2));
    }

    @Test
    public void testDistinctByNull() {
        assertTrue(distinctBy(null, x -> x).isEmpty());
    }

    // ==================== takeWhile/dropWhile Tests ====================

    @Test
    public void testTakeWhile() {
        assertEquals(list(1, 2, 3), takeWhile(list(1, 2, 3, 5, 4, 3), x -> x < 4));
    }

    @Test
    public void testTakeWhileNull() {
        assertTrue(takeWhile(null, x -> true).isEmpty());
    }

    @Test
    public void testDropWhile() {
        assertEquals(list(5, 4, 3), dropWhile(list(1, 2, 3, 5, 4, 3), x -> x < 4));
    }

    @Test
    public void testDropWhileNull() {
        assertTrue(dropWhile(null, x -> true).isEmpty());
    }

    // ==================== take/drop Tests ====================

    @Test
    public void testTake() {
        assertEquals(list(1, 2, 3), take(list(1, 2, 3, 4, 5), 3));
        assertEquals(list(1, 2), take(list(1, 2), 5));
    }

    @Test
    public void testTakeNull() {
        assertTrue(take(null, 3).isEmpty());
    }

    @Test
    public void testTakeZero() {
        assertTrue(take(list(1, 2, 3), 0).isEmpty());
    }

    @Test
    public void testDrop() {
        assertEquals(list(4, 5), drop(list(1, 2, 3, 4, 5), 3));
    }

    @Test
    public void testDropNull() {
        assertTrue(drop(null, 3).isEmpty());
    }

    @Test
    public void testDropAll() {
        assertTrue(drop(list(1, 2, 3), 5).isEmpty());
    }

    // ==================== sliding Tests ====================

    @Test
    public void testSliding() {
        List<List<Integer>> result = sliding(list(1, 2, 3, 4, 5), 3);
        assertEquals(3, result.size());
        assertEquals(list(1, 2, 3), result.get(0));
        assertEquals(list(2, 3, 4), result.get(1));
        assertEquals(list(3, 4, 5), result.get(2));
    }

    @Test
    public void testSlidingNull() {
        assertTrue(sliding(null, 3).isEmpty());
    }

    @Test
    public void testSlidingZeroSize() {
        assertTrue(sliding(list(1, 2, 3), 0).isEmpty());
    }

    @Test
    public void testSlidingWithStep() {
        List<List<Integer>> result = sliding(list(1, 2, 3, 4, 5), 2, 2);
        assertEquals(2, result.size());
        assertEquals(list(1, 2), result.get(0));
        assertEquals(list(3, 4), result.get(1));
    }

    @Test
    public void testSlidingWithStepNull() {
        assertTrue(sliding(null, 2, 2).isEmpty());
    }

    @Test
    public void testSlidingWithStepZero() {
        assertTrue(sliding(list(1, 2, 3), 2, 0).isEmpty());
    }

    // ==================== scan Tests ====================

    @Test
    public void testScan() {
        List<Integer> result = scan(list(1, 2, 3, 4), 0, (Integer acc, Integer x) -> acc + x);
        assertEquals(list(0, 1, 3, 6, 10), result);
    }

    @Test
    public void testScanNull() {
        assertEquals(list(0), scan(null, 0, (Integer acc, Integer x) -> acc + x));
    }

    // ==================== interleave Tests ====================

    @Test
    public void testInterleave() {
        assertEquals(list(1, "a", 2, "b", 3, "c"), interleave(list(1, 2, 3), list("a", "b", "c")));
    }

    @Test
    public void testInterleaveUnequal() {
        assertEquals(list(1, "a", 2, "b", 3), interleave(list(1, 2, 3), list("a", "b")));
    }

    @Test
    public void testInterleaveNull() {
        assertEquals(list(1, 2), interleave(list(1, 2), null));
        assertEquals(list(1, 2), interleave(null, list(1, 2)));
        assertTrue(interleave(null, null).isEmpty());
    }

    // ==================== transpose Tests ====================

    @Test
    public void testTranspose() {
        List<List<Integer>> matrix = list(list(1, 2, 3), list(4, 5, 6));
        List<List<Integer>> result = transpose(matrix);
        assertEquals(3, result.size());
        assertEquals(list(1, 4), result.get(0));
        assertEquals(list(2, 5), result.get(1));
        assertEquals(list(3, 6), result.get(2));
    }

    @Test
    public void testTransposeNull() {
        assertTrue(transpose(null).isEmpty());
    }

    @Test
    public void testTransposeEmpty() {
        assertTrue(transpose(list()).isEmpty());
    }

    // ==================== repeat Tests ====================

    @Test
    public void testRepeat() {
        assertEquals(list("x", "x", "x"), repeat("x", 3));
    }

    // ==================== generate Tests ====================

    @Test
    public void testGenerate() {
        final int[] counter = {0};
        List<Integer> result = generate(() -> counter[0]++, 5);
        assertEquals(list(0, 1, 2, 3, 4), result);
    }

    // ==================== iterate Tests ====================

    @Test
    public void testIterate() {
        List<Integer> result = iterate(1, x -> x * 2, 5);
        assertEquals(list(1, 2, 4, 8, 16), result);
    }

    // ==================== zip3/unzip Tests ====================

    @Test
    public void testZip3() {
        List<Tuple3<Integer, String, Boolean>> result = zip3(list(1, 2), list("a", "b"), list(true, false));
        assertEquals(2, result.size());
        assertEquals((Integer) 1, result.get(0).getFirst());
        assertEquals("a", result.get(0).getSecond());
        assertTrue(result.get(0).getThird());
    }

    @Test
    public void testUnzip() {
        List<Pair<String, Integer>> pairs = list(pair("a", 1), pair("b", 2));
        Pair<List<String>, List<Integer>> result = unzip(pairs);
        assertEquals(list("a", "b"), result.getFirst());
        assertEquals(list(1, 2), result.getSecond());
    }

    // ==================== Predicate Builders Tests ====================

    @Test
    public void testAnd() {
        Predicate<Integer> p = and(x -> x > 0, x -> x < 10, x -> x % 2 == 0);
        assertTrue(p.test(4));
        assertFalse(p.test(12));
        assertFalse(p.test(3));
    }

    @Test
    public void testOr() {
        Predicate<Integer> p = or(x -> x < 0, x -> x > 100);
        assertTrue(p.test(-5));
        assertTrue(p.test(200));
        assertFalse(p.test(50));
    }

    @Test
    public void testNot() {
        Predicate<Integer> p = not(x -> x > 0);
        assertTrue(p.test(-1));
        assertFalse(p.test(1));
    }

    // ==================== Comparator Builders Tests ====================

    @Test
    public void testComparing() {
        Comparator<String> c = comparing(s -> s.length());
        assertTrue(c.compare("a", "bb") < 0);
        assertTrue(c.compare("bb", "a") > 0);
        assertEquals(0, c.compare("a", "b"));
    }

    @Test
    public void testComparingDesc() {
        Comparator<String> c = comparingDesc(s -> s.length());
        assertTrue(c.compare("a", "bb") > 0);
    }

    @Test
    public void testThenBy() {
        Comparator<String> c = thenBy(comparing(s -> s.length()), comparing(s -> s));
        List<String> list = list("bb", "aa", "c");
        assertEquals(list("c", "aa", "bb"), sorted(list, c));
    }

    @Test
    public void testThenByWithFunction() {
        Comparator<String> c = thenBy(comparing(s -> s.length()), s -> s);
        List<String> list = list("bb", "aa", "c");
        assertEquals(list("c", "aa", "bb"), sorted(list, c));
    }

    // ==================== Map Enhancements Tests ====================

    @Test
    public void testMerge() {
        Map<String, Integer> m1 = hashmap(kv("a", 1), kv("b", 2));
        Map<String, Integer> m2 = hashmap(kv("b", 3), kv("c", 4));
        Map<String, Integer> result = merge(m1, m2, (v1, v2) -> v1 + v2);
        assertEquals((Integer) 1, result.get("a"));
        assertEquals((Integer) 5, result.get("b")); // 2 + 3
        assertEquals((Integer) 4, result.get("c"));
    }

    @Test
    public void testInvert() {
        Map<String, Integer> m = hashmap(kv("a", 1), kv("b", 2));
        Map<Integer, String> inverted = invert(m);
        assertEquals("a", inverted.get(1));
        assertEquals("b", inverted.get(2));
    }

    @Test
    public void testMapKeys() {
        Map<String, Integer> m = hashmap(kv("a", 1), kv("b", 2));
        Map<String, Integer> result = mapKeys(m, s -> s.toUpperCase());
        assertEquals((Integer) 1, result.get("A"));
        assertEquals((Integer) 2, result.get("B"));
    }

    @Test
    public void testMapValues() {
        Map<String, Integer> m = hashmap(kv("a", 1), kv("b", 2));
        Map<String, Integer> result = mapValues(m, v -> v * 10);
        assertEquals((Integer) 10, result.get("a"));
        assertEquals((Integer) 20, result.get("b"));
    }

    @Test
    public void testFilterKeys() {
        Map<String, Integer> m = hashmap(kv("a", 1), kv("b", 2), kv("c", 3));
        Map<String, Integer> result = filterKeys(m, k -> k.compareTo("b") <= 0);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("a"));
        assertTrue(result.containsKey("b"));
    }

    @Test
    public void testFilterValues() {
        Map<String, Integer> m = hashmap(kv("a", 1), kv("b", 2), kv("c", 3));
        Map<String, Integer> result = filterValues(m, v -> v > 1);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("b"));
        assertTrue(result.containsKey("c"));
    }

    @Test
    public void testGetOrCompute() {
        Map<String, Integer> m = new HashMap<>();
        m.put("a", 1);
        assertEquals((Integer) 1, getOrCompute(m, "a", () -> 10));
        assertEquals((Integer) 10, getOrCompute(m, "b", () -> 10));
        assertEquals((Integer) 10, m.get("b")); // Should be added to map
    }

    @Test
    public void testKeys() {
        Map<String, Integer> m = hashmap(kv("a", 1), kv("b", 2));
        List<String> keys = keys(m);
        assertEquals(2, keys.size());
        assertTrue(keys.contains("a"));
        assertTrue(keys.contains("b"));
    }

    @Test
    public void testValues() {
        Map<String, Integer> m = hashmap(kv("a", 1), kv("b", 2));
        List<Integer> vals = values(m);
        assertEquals(2, vals.size());
        assertTrue(vals.contains(1));
        assertTrue(vals.contains(2));
    }

    @Test
    public void testEntries() {
        Map<String, Integer> m = hashmap(kv("a", 1), kv("b", 2));
        List<KeyValue<String, Integer>> entries = entries(m);
        assertEquals(2, entries.size());
    }

    // ==================== add/removeKey Tests ====================

    @Test
    public void testAdd() {
        Map<String, Integer> m = new HashMap<>();
        add(m, kv("a", 1), kv("b", 2));
        assertEquals(2, m.size());
    }

    @Test
    public void testRemoveKey() {
        Map<String, Integer> m = hashmap(kv("a", 1), kv("b", 2));
        Map result = removeKey(m, "a");
        assertEquals(1, result.size());
        assertFalse(result.containsKey("a"));
    }
}
