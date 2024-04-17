package com.akalea.sugar;

import static com.akalea.sugar.Collections.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.akalea.sugar.internal.Param;

public interface Pojos {

    public static Map<String, Object> toMap(Object o) {
        if (o == null)
            return null;
        Map<String, Object> dict = new HashMap<>();
        forEach(
            list(o.getClass().getDeclaredFields()),
            field -> {
                if (Modifier.isStatic(field.getModifiers()))
                    return;
                Object fieldValue = get(o, field);
                dict.put(field.getName(), nodeValue(fieldValue));
            });
        return dict;
    }

    private static Object nodeValue(Object o) {
        if (o == null)
            return null;
        if (isSimpleType(o.getClass())) {
            return o;
        } else if (isMapType(o.getClass())) {
            return Collections.toMap(
                ((Map) o).entrySet(),
                e -> String.valueOf(((Map.Entry) e).getKey()),
                e -> nodeValue(((Map.Entry) e).getValue()));
        } else if (isCollectionType(o.getClass())) {
            return map((Collection) o, v -> nodeValue(v));
        } else {
            return toMap(o);
        }
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
