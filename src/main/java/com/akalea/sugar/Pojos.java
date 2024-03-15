package com.akalea.sugar;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.akalea.sugar.internal.Param;

public interface Pojos {

    public static <T, R> void ifNotNull(T obj, Consumer<T> func) {
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
