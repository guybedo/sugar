package com.akalea.sugar;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import com.akalea.sugar.internal.Param;

public interface Pojos {

    public static <T,R> R ifNotNull(T obj, Function<T,R> func) {
        return Optional.ofNullable(obj).map(o->func.apply(o)).orElse(null);
    }

    public static <T> T orElse(T obj, T other) {
        return Optional.ofNullable(obj).orElse(other);
    }

    public static <T> T orElse(T obj, Supplier<T> supplier) {
        return Optional.ofNullable(obj).orElseGet(supplier);
    }

    public static <T> Param<T> param(String id, T value) {
        return new Param<T>().setId(id).setValue(value);
    }

}
