package com.akalea.sugar;

import static com.akalea.sugar.Collections.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.akalea.sugar.internal.Param;

public interface Pojos {

    public static List<Field> getAllInheritedFields(Object o) {
        return getAllInheritedFields(o.getClass());
    }

    public static List<Field> getAllInheritedFields(Class clazz) {
        List<Field> fields = list(clazz.getDeclaredFields());
        ifPresent(clazz.getSuperclass(), c -> fields.addAll(getAllInheritedFields(c)));
        return fields;
    }

    public static Map<String, Object> toMap(Object o) {
        return toMap(o, null);
    }

    public static Map<String, Object> toMap(
        Object o,
        Map<Class, Function<Object, Object>> converters) {
        if (o == null)
            return null;
        Map<String, Object> dict = new HashMap<>();
        forEach(
            getAllInheritedFields(o),
            field -> {
                if (Modifier.isStatic(field.getModifiers()))
                    return;
                Object fieldValue = get(o, field);
                dict.put(field.getName(), nodeValue(fieldValue, converters));
            });
        return dict;
    }

    private static Object nodeValue(
        Object o,
        Map<Class, Function<Object, Object>> converters) {
        if (o == null)
            return null;

        if (converters.containsKey(o.getClass()))
            return converters.get(o.getClass()).apply(o);

        if (isSimpleType(o.getClass())) {
            return o;
        }

        if (isMapType(o.getClass())) {
            return Collections.toMap(
                ((Map) o).entrySet(),
                e -> String.valueOf(((Map.Entry) e).getKey()),
                e -> nodeValue(((Map.Entry) e).getValue(), converters));
        }
        if (isCollectionType(o.getClass())) {
            return map((Collection) o, v -> nodeValue(v, converters));
        }
        return toMap(o);
    }

    private static boolean isMapType(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    private static boolean isCollectionType(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    private static boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive()
            || clazz.isEnum()
            || isWrapperType(clazz);
    }

    private static boolean isWrapperType(Class<?> clazz) {
        return clazz == Boolean.class
            || clazz == Integer.class
            || clazz == Character.class
            || clazz == Byte.class
            || clazz == Short.class
            || clazz == Double.class
            || clazz == Long.class
            || clazz == Float.class
            || clazz == String.class;
    }

    public static Object get(Object o, Field field) {
        try {
            field.setAccessible(true);
            return field.get(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T, R> void ifPresent(T obj, Consumer<T> func) {
        Optional.ofNullable(obj).ifPresent(o -> func.accept(o));
    }

    public static <T> T orElse(T obj, T other) {
        return Optional.ofNullable(obj).orElse(other);
    }

    public static <T, R> R orElse(T obj, Function<T, R> map, R other) {
        return Optional.ofNullable(obj).map(o -> map.apply(o)).orElse(other);
    }

    public static <T> T orElse(T obj, Supplier<T> supplier) {
        if (supplier == null)
            return orElse(obj, (T) null);
        return Optional.ofNullable(obj).orElseGet(supplier);
    }
    
    public static <T, R> R apply(T obj, Function<T, R> map) {
        return Optional.ofNullable(obj).map(o -> map.apply(o)).orElse(null);
    }

    public static <T> Param<T> p(String id, T value) {
        return new Param<T>().setId(id).setValue(value);
    }

    public static <T> T o(T obj, Param... params) {
        Stream.of(params).forEach(p -> {
            try {
                Field field = obj.getClass().getField(p.getId());
                field.setAccessible(true);
                field.set(obj, p.getValue());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return obj;
    }

}
