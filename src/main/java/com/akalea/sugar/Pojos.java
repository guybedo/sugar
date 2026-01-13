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

        if (converters != null && converters.containsKey(o.getClass()))
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

    // ==================== Null-Safety Enhancements ====================

    /**
     * Returns the first non-null value from the arguments.
     */
    @SafeVarargs
    public static <T> T coalesce(T... values) {
        for (T value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * Returns the value if condition is true, otherwise null.
     */
    public static <T> Optional<T> when(boolean condition, Supplier<T> supplier) {
        return condition ? Optional.ofNullable(supplier.get()) : Optional.empty();
    }

    /**
     * Returns the value if condition is false, otherwise null.
     */
    public static <T> Optional<T> unless(boolean condition, Supplier<T> supplier) {
        return when(!condition, supplier);
    }

    /**
     * Executes consumer if condition is true.
     */
    public static void when(boolean condition, Runnable runnable) {
        if (condition) {
            runnable.run();
        }
    }

    /**
     * Executes consumer if condition is false.
     */
    public static void unless(boolean condition, Runnable runnable) {
        when(!condition, runnable);
    }

    /**
     * Returns true if the object is null.
     */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /**
     * Returns true if the object is not null.
     */
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    /**
     * Returns the value if not null, otherwise throws the given exception.
     */
    public static <T, X extends Throwable> T requireNonNull(T obj, Supplier<X> exceptionSupplier) throws X {
        if (obj == null) {
            throw exceptionSupplier.get();
        }
        return obj;
    }

    /**
     * Returns the value if not null, otherwise throws IllegalArgumentException with message.
     */
    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
        return obj;
    }

    /**
     * Wraps a value in an Optional.
     */
    public static <T> Optional<T> opt(T obj) {
        return Optional.ofNullable(obj);
    }

    /**
     * Safely gets a nested property using a chain of functions.
     * Returns empty Optional if any step returns null.
     */
    public static <T, R> Optional<R> safeGet(T obj, Function<T, R> getter) {
        if (obj == null) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(getter.apply(obj));
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    /**
     * Safely gets a nested property with two levels of nesting.
     */
    public static <T, R1, R2> Optional<R2> safeGet(T obj, Function<T, R1> getter1, Function<R1, R2> getter2) {
        return safeGet(obj, getter1).flatMap(r -> safeGet(r, getter2));
    }

    /**
     * Safely gets a nested property with three levels of nesting.
     */
    public static <T, R1, R2, R3> Optional<R3> safeGet(
            T obj,
            Function<T, R1> getter1,
            Function<R1, R2> getter2,
            Function<R2, R3> getter3) {
        return safeGet(obj, getter1, getter2).flatMap(r -> safeGet(r, getter3));
    }

    /**
     * Chains multiple operations, short-circuiting on null.
     */
    public static <T, R> R chain(T obj, Function<T, R> func) {
        if (obj == null) {
            return null;
        }
        return func.apply(obj);
    }

    /**
     * Chains multiple operations with two steps.
     */
    public static <T, R1, R2> R2 chain(T obj, Function<T, R1> func1, Function<R1, R2> func2) {
        R1 r1 = chain(obj, func1);
        return chain(r1, func2);
    }

    /**
     * Chains multiple operations with three steps.
     */
    public static <T, R1, R2, R3> R3 chain(
            T obj,
            Function<T, R1> func1,
            Function<R1, R2> func2,
            Function<R2, R3> func3) {
        R2 r2 = chain(obj, func1, func2);
        return chain(r2, func3);
    }

    /**
     * Returns the result of the function if obj is not null, otherwise returns defaultValue.
     */
    public static <T, R> R mapOrDefault(T obj, Function<T, R> mapper, R defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        R result = mapper.apply(obj);
        return result != null ? result : defaultValue;
    }

    /**
     * Applies a side-effect function if the value is not null, then returns the value.
     */
    public static <T> T tap(T obj, Consumer<T> consumer) {
        if (obj != null) {
            consumer.accept(obj);
        }
        return obj;
    }

    /**
     * Transforms the value using the function and returns both original and transformed.
     */
    public static <T, R> com.akalea.sugar.internal.Pair<T, R> withTransform(T obj, Function<T, R> transformer) {
        return new com.akalea.sugar.internal.Pair<T, R>()
            .setFirst(obj)
            .setSecond(obj != null ? transformer.apply(obj) : null);
    }

    /**
     * Returns true if the object equals any of the provided values.
     */
    @SafeVarargs
    public static <T> boolean equalsAny(T obj, T... values) {
        if (obj == null) {
            for (T value : values) {
                if (value == null) return true;
            }
            return false;
        }
        for (T value : values) {
            if (obj.equals(value)) return true;
        }
        return false;
    }

    /**
     * Returns true if the object does not equal any of the provided values.
     */
    @SafeVarargs
    public static <T> boolean equalsNone(T obj, T... values) {
        return !equalsAny(obj, values);
    }

}
