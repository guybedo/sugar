package com.akalea.sugar;

import java.util.ArrayList;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.akalea.sugar.internal.KeyValue;
import com.akalea.sugar.internal.Pair;

public interface Collections {

    public static <T> List<T> filter(List<T> objs, Function<T, Boolean> func) {
        if (objs == null)
            return new ArrayList();
        return objs
            .stream()
            .filter(o -> func.apply(o))
            .collect(Collectors.toList());
    }

    public static <T> boolean any(List<T> objs, Function<T, Boolean> func) {
        return exists(objs, func);
    }

    public static <T> boolean all(List<T> objs, Function<T, Boolean> func) {
        return filter(objs, func).size() == objs.size();
    }

    public static <T> boolean exists(List<T> objs, Function<T, Boolean> func) {
        return findFirst(objs, func) != null;
    }

    public static <T> T findFirst(List<T> objs, Function<T, Boolean> func) {
        if (objs == null)
            return null;
        return objs
            .stream()
            .filter(o -> func.apply(o))
            .findFirst()
            .orElse(null);
    }

    public static <T, R> List<R> map(List<T> objs, Function<T, R> func) {
        if (objs == null)
            return new ArrayList();
        return objs
            .stream()
            .map(o -> func.apply(o))
            .collect(Collectors.toList());
    }

    public static <T> List<T> sorted(List<T> objs, Comparator<T> comp) {
        if (objs == null)
            return new ArrayList();
        return objs
            .stream()
            .sorted(comp)
            .collect(Collectors.toList());
    }

    public static <T extends Comparable<T>> List<T> asc(List<T> objs) {
        return sorted(objs, true);
    }

    public static <T extends Comparable<T>> List<T> desc(List<T> objs) {
        return sorted(objs, false);
    }

    public static <T extends Comparable<T>> List<T> sorted(List<T> objs, boolean asc) {
        return objs
            .stream()
            .sorted((a, b) -> (asc ? 1 : -1) * a.compareTo(b))
            .collect(Collectors.toList());
    }

    public static <T extends Comparable<T>> List<T> reversed(List<T> objs) {
        List reversed = new ArrayList();
        reversed.addAll(objs);
        java.util.Collections.reverse(reversed);
        return reversed;
    }

    public static <T extends Comparable<T>> T min(List<T> objs) {
        return objs
            .stream()
            .sorted((a, b) -> a.compareTo(b))
            .findFirst()
            .orElse(null);
    }

    public static <T extends Comparable<T>> T min(List objs, Function<Object, T> supplier) {
        return min(
            (List<T>) objs
                .stream()
                .map(o -> (T) supplier.apply(o))
                .collect(Collectors.toList()));
    }

    public static <T extends Comparable<T>> T max(List<T> objs) {
        return objs
            .stream()
            .sorted((a, b) -> -a.compareTo(b))
            .findFirst()
            .orElse(null);
    }

    public static <T extends Comparable<T>> T max(List objs, Function<Object, T> supplier) {
        return max(
            (List<T>) objs
                .stream()
                .map(o -> (T) supplier.apply(o))
                .collect(Collectors.toList()));
    }

    public static <K, V> KeyValue<K, V> kv(K key, V value) {
        return new KeyValue<K, V>().setKey(key).setValue(value);
    }

    public static <T, R> Map<T, R> map(KeyValue<T, R>... keyValues) {
        return hashmap(keyValues);
    }

    public static <T, R> Map<T, R> hashmap(KeyValue<T, R>... keyValues) {
        return map(new HashMap<>(), keyValues);
    }

    public static <T, R> Map<T, R> treemap(KeyValue<T, R>... keyValues) {
        return map(new TreeMap<>(), keyValues);
    }

    public static <T, R, M extends Map> Map<T, R> map(M map, KeyValue<T, R>... keyValues) {
        Stream.of(keyValues).forEach(kv -> map.put(kv.getKey(), kv.getValue()));
        return map;
    }

    public static <T> void apply(List<T> elements, Consumer<T> func) {
        elements.stream().forEach(e -> func.accept(e));
    }

    public static <T> List<T> list(T... elements) {
        List<T> arrayList = new ArrayList<>();
        Stream.of(elements).forEach(e -> arrayList.add(e));
        return arrayList;
    }

    public static <T> Set<T> set(T... elements) {
        Set<T> set = new HashSet<>();
        Stream.of(elements).forEach(e -> set.add(e));
        return set;
    }

    public static <T> T first(List<T> elements) {
        return elements.get(0);
    }

    public static <T> T last(List<T> elements) {
        return elements.get(elements.size() - 1);
    }

    public static <T> List<KeyValue<Integer, T>> enumerate(T... elements) {
        return enumerate(list(elements));
    }

    public static <T> List<KeyValue<Integer, T>> enumerate(List<T> elements) {
        List<KeyValue<Integer, T>> enumeration = new ArrayList();
        for (int i = 0; i < elements.size(); i++) {
            T t = elements.get(i);
            enumeration.add(new KeyValue<Integer, T>().setKey(i).setValue(t));
        }
        return enumeration;
    }

    public static <T> void enumerate(List<T> elements, BiConsumer<Integer, T> func) {
        enumerate(elements).stream().forEach(e -> func.accept(e.getKey(), e.getValue()));
    }

    public static <T1, T2> List<Pair<T1, T2>> zip(List<T1> l1, List<T2> l2) {
        List<Pair<T1, T2>> zipped = new ArrayList<>();
        for (int i = 0; i < l1.size(); i++) {
            zipped.add(new Pair<T1, T2>().setFirst(l1.get(i)).setSecond(l2.get(i)));
        }
        return zipped;
    }

    public static <T> List<T> concat(List<T> l1, List<T> l2) {
        return list(l1, l2)
            .stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    public static <T> List<T> append(List<T> l1, T e) {
        List<T> list = new ArrayList<>(l1);
        list.add(e);
        return list;
    }
}
