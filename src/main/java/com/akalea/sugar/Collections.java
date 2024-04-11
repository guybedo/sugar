package com.akalea.sugar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.akalea.sugar.internal.KeyValue;
import com.akalea.sugar.internal.Pair;

public interface Collections {

    public static <K, T> T orElse(Map<K, Object> map, K key, T other) {
        return map.containsKey(key) ? (T) map.get(key) : other;
    }

    public static <T> List<T> notNull(Collection<T> objs) {
        return filter(objs, o -> o != null);
    }

    public static <T> List<T> filter(Collection<T> objs, Predicate<T> func) {
        if (objs == null)
            return new ArrayList();
        return objs
            .stream()
            .filter(o -> func.test(o))
            .collect(Collectors.toList());
    }

    public static <T> boolean any(Collection<T> objs, Predicate<T> func) {
        return exists(objs, func);
    }

    public static <T> boolean all(Collection<T> objs, Predicate<T> func) {
        return filter(objs, func).size() == objs.size();
    }

    public static <T> boolean exists(Collection<T> objs, Predicate<T> func) {
        return findFirst(objs, func) != null;
    }

    public static <T> T findFirst(Collection<T> objs, Predicate<T> func) {
        if (objs == null)
            return null;
        return objs
            .stream()
            .filter(o -> func.test(o))
            .findFirst()
            .orElse(null);
    }

    public static <T> void forEach(Collection<T> objs, Consumer<T> func) {
        objs
            .stream()
            .forEach(o -> func.accept(o));
    }

    public static <T, R> List<R> map(Collection<T> objs, Function<T, R> func) {
        if (objs == null)
            return new ArrayList();
        return objs
            .stream()
            .map(o -> func.apply(o))
            .collect(Collectors.toList());
    }

    public static <T, R> List<R> flatMap(Collection<T> objs, Function<T, Collection<R>> func) {
        if (objs == null)
            return new ArrayList();
        return objs
            .stream()
            .map(o -> func.apply(o))
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    public static <T> List<T> sorted(Collection<T> objs, Comparator<T> comp) {
        if (objs == null)
            return new ArrayList();
        return objs
            .stream()
            .sorted(comp)
            .collect(Collectors.toList());
    }

    public static <T extends Comparable<T>> List<T> asc(Collection<T> objs) {
        return sorted(objs, true);
    }

    public static <T extends Comparable<T>> List<T> desc(Collection<T> objs) {
        return sorted(objs, false);
    }

    public static <T extends Comparable<T>> List<T> sorted(Collection<T> objs, boolean asc) {
        return objs
            .stream()
            .sorted((a, b) -> asc ? a.compareTo(b) : b.compareTo(a))
            .collect(Collectors.toList());
    }

    public static <T> List<T> reversed(List<T> objs) {
        List<T> reversed = new ArrayList<>();
        for (int i = objs.size() - 1; i >= 0; i--)
            reversed.add(objs.get(i));
        return reversed;
    }

    public static <T extends Comparable<T>> T min(T... objs) {
        return min(list(objs));
    }

    public static <T extends Comparable<T>> T min(Collection<T> objs) {
        return objs
            .stream()
            .sorted((a, b) -> a.compareTo(b))
            .findFirst()
            .orElse(null);
    }
    
    public static <T, R extends Comparable<R>> T min(Collection<T> objs, Function<T, R> supplier) {
        return objs
            .stream()
            .sorted((a, b) -> supplier.apply(a).compareTo(supplier.apply(b)))
            .findFirst()
            .orElse(null);
    }

    public static <T, R extends Comparable<R>> R minVal(
        Collection<T> objs,
        Function<T, R> supplier) {
        return min(
            (List<R>) objs
                .stream()
                .map(o -> (R) supplier.apply(o))
                .collect(Collectors.toList()));
    }

    public static <T> T min(Collection<T> objs, Comparator<T> comp) {
        return objs
            .stream()
            .sorted(comp)
            .findFirst()
            .orElse(null);
    }

    public static <T extends Comparable<T>> T max(Collection<T> objs) {
        return objs
            .stream()
            .sorted((a, b) -> b.compareTo(a))
            .findFirst()
            .orElse(null);
    }

    public static <T extends Comparable<T>> T max(T... objs) {
        return max(list(objs));
    }

    public static <T> T max(Collection<T> objs, Comparator<T> comp) {
        return first(
            reversed(
                objs
                    .stream()
                    .sorted(comp)
                    .collect(Collectors.toList())));
    }

    public static <T, R extends Comparable<R>> R maxVal(
        Collection<T> objs,
        Function<T, R> supplier) {
        return max(
            (List<R>) objs
                .stream()
                .map(o -> (R) supplier.apply(o))
                .collect(Collectors.toList()));
    }

    public static <T, R extends Comparable<R>> T max(Collection<T> objs, Function<T, R> supplier) {
        return objs
            .stream()
            .sorted((a, b) -> supplier.apply(b).compareTo(supplier.apply(a)))
            .findFirst()
            .orElse(null);
    }

    public static Float mean(List<Float> vals) {
        return (float) vals
            .stream()
            .mapToDouble(v -> v)
            .average()
            .orElse(0);
    }

    public static <K, V> KeyValue<K, V> kv(K key, V value) {
        return new KeyValue<K, V>().setKey(key).setValue(value);
    }

    public static <T, R> Map<T, R> map(KeyValue<T, R>... keyValues) {
        return hashmap(keyValues);
    }

    public static <T, R> Map<T, R> hashmap(KeyValue<T, R>... keyValues) {
        return add(new HashMap<>(), keyValues);
    }

    public static <T, R> Map<T, R> treemap(KeyValue<T, R>... keyValues) {
        return add(new TreeMap<>(), keyValues);
    }

    public static <T, R, M extends Map> Map<T, R> map(M map) {
        try {
            Map newMap = map.getClass().getDeclaredConstructor().newInstance();
            newMap.putAll(map);
            return newMap;
        } catch (Exception e) {
            throw new RuntimeException("Error creating new map", e);
        }
    }

    public static <T, R, M extends Map> Map<T, R> add(M map, KeyValue<T, R>... keyValues) {
        Stream.of(keyValues).forEach(kv -> map.put(kv.getKey(), kv.getValue()));
        return map;
    }

    public static <T, R> Map<T, R> toMap(Collection<T> objs, Function<T, R> func) {
        return objs
            .stream()
            .collect(Collectors.toMap(o -> o, o -> func.apply(o)));
    }

    public static <T> void apply(Collection<T> elements, Consumer<T> func) {
        elements.stream().forEach(e -> func.accept(e));
    }

    public static <T> List<T> list(Collection<T> collection) {
        List<T> arrayList = new ArrayList<>();
        arrayList.addAll(collection);
        return arrayList;
    }

    public static <T> List<T> list(T... elements) {
        List<T> arrayList = new ArrayList<>();
        Stream.of(elements).forEach(e -> arrayList.add(e));
        return arrayList;
    }

    public static <T> List<T> toList(BaseStream<T, ?> elements) {
        List<T> arrayList = new ArrayList<>();
        elements.iterator().forEachRemaining(e -> arrayList.add((T) e));
        return arrayList;
    }

    public static <T> List<List<T>> product(Collection<T>... collections) {
        return product(list(collections));
    }

    public static <T> List<List<T>> product(List<Collection<T>> collections) {
        if (collections.size() == 1)
            return collections.get(0)
                .stream()
                .map(e -> list(e))
                .collect(Collectors.toList());
        List<List<T>> result = new ArrayList<>();
        for (T elem : collections.get(0))
            for (List<T> others : product(collections.subList(1, collections.size())))
                result.add(prepend(others, elem));

        return result;
    }

    public static <T> Set<T> set(T... elements) {
        Set<T> set = new HashSet<>();
        Stream.of(elements).forEach(e -> set.add(e));
        return set;
    }

    public static <T> Set<T> set(Collection<T>... collections) {
        Set<T> set = new HashSet<>();
        Stream.of(collections).forEach(e -> set.addAll(e));
        return set;
    }

    public static <T, R> Set<R> set(Collection<T> collection, Function<T, R> func) {
        return collection
            .stream()
            .map(e -> func.apply(e))
            .collect(Collectors.toSet());
    }

    public static <T> Set<T> intersect(Set<T> a, Set<T> b) {
        Set<T> intersection = set(a);
        intersection.retainAll(b);
        return intersection;
    }

    public static <T> Set<T> difference(Set<T> a, Set<T> b) {
        Set<T> intersection = set(a);
        intersection.removeAll(b);
        return intersection;
    }

    public static <T> T first(List<T> elements) {
        if (elements == null || elements.isEmpty())
            return null;
        return elements.get(0);
    }

    public static <T> T first(List<T> elements, Predicate<T> predicate) {
        if (elements == null || elements.isEmpty())
            return null;
        for (T t : elements) {
            if (predicate.test(t))
                return t;
        }
        return null;
    }

    public static <T> T last(List<T> elements, Predicate<T> predicate) {
        if (elements == null || elements.isEmpty())
            return null;
        for (int i = elements.size() - 1; i >= 0; i--) {
            T t = elements.get(i);
            if (predicate.test(t))
                return t;
        }
        return null;
    }

    public static <T> T last(List<T> elements) {
        if (elements == null || elements.isEmpty())
            return null;
        return elements.get(elements.size() - 1);
    }

    public static <T, R> long count(Collection<T> collection, Function<T, R> func) {
        return collection
            .stream()
            .map(e -> func.apply(e))
            .collect(Collectors.toSet())
            .size();
    }

    public static <T extends Number> T sum(Collection<T> collection) {
        return (T) collection
            .stream()
            .reduce((a, b) -> add(a, b))
            .orElse((T) (Number) 0);
    }

    private static <T extends Number> T add(Number a, Number b) {
        if (a instanceof Float || b instanceof Float) {
            return (T) (Float) (a.floatValue() + b.floatValue());
        }
        if (a instanceof Double || b instanceof Double) {
            return (T) (Double) (a.doubleValue() + b.doubleValue());
        }
        if (a instanceof Long || b instanceof Long) {
            return (T) (Long) (a.longValue() + b.longValue());
        }
        return (T) (Integer) (a.intValue() + b.intValue());
    }

    public static <T, R> Map<R, Long> flatCount(
        Collection<T> collection,
        Function<T, Collection<R>> func) {
        List<R> values = flatMap(collection, func);
        return values
            .stream()
            .collect(
                Collectors.toMap(
                    v -> v,
                    v -> 1l,
                    (c1, c2) -> c1 + c2));
    }

    public static <T> List<KeyValue<Integer, T>> enumerate(T... elements) {
        return enumerate(list(elements));
    }

    public static <T> List<KeyValue<Integer, T>> enumerate(Collection<T> elements) {
        List<KeyValue<Integer, T>> enumeration = new ArrayList();
        int i = 0;
        for (T elem : elements) {
            enumeration.add(new KeyValue<Integer, T>().setKey(i++).setValue(elem));
        }
        return enumeration;
    }

    public static <T> void enumerate(Collection<T> elements, BiConsumer<Integer, T> func) {
        enumerate(elements)
            .stream()
            .forEach(e -> func.accept(e.getKey(), e.getValue()));
    }

    public static <T> List<List<T>> partition(List<T> elements, int size) {
        List<List<T>> partitions = new ArrayList();
        for (int i = 0; i < elements.size(); i += size) {
            int toIdx = min(elements.size(), i + size);
            partitions.add(elements.subList(i, toIdx));
        }
        return partitions;
    }

    public static <T1, T2> List<Pair<T1, T2>> zip(List<T1> l1, List<T2> l2) {
        List<Pair<T1, T2>> zipped = new ArrayList<>();
        for (int i = 0; i < l1.size(); i++) {
            zipped.add(new Pair<T1, T2>().setFirst(l1.get(i)).setSecond(l2.get(i)));
        }
        return zipped;
    }

    public static <T> List<T> concat(List<T>... l) {
        return list(l)
            .stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    public static <T> List<T> append(List<T> l1, T e) {
        List<T> list = new ArrayList<>(l1);
        list.add(e);
        return list;
    }

    public static <T> List<T> prepend(List<T> l1, T e) {
        List<T> list = new ArrayList<>();
        list.add(e);
        list.addAll(l1);
        return list;
    }
}
