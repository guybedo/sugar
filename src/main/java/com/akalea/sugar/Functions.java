package com.akalea.sugar;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Function composition and manipulation utilities.
 */
public interface Functions {

    /**
     * Composes two functions: g(f(x)).
     */
    public static <A, B, C> Function<A, C> compose(Function<A, B> f, Function<B, C> g) {
        return a -> g.apply(f.apply(a));
    }

    /**
     * Pipes two functions: f then g (same as compose but reads left-to-right).
     */
    public static <A, B, C> Function<A, C> pipe(Function<A, B> f, Function<B, C> g) {
        return compose(f, g);
    }

    /**
     * Composes three functions.
     */
    public static <A, B, C, D> Function<A, D> compose(
            Function<A, B> f,
            Function<B, C> g,
            Function<C, D> h) {
        return a -> h.apply(g.apply(f.apply(a)));
    }

    /**
     * Composes four functions.
     */
    public static <A, B, C, D, E> Function<A, E> compose(
            Function<A, B> f,
            Function<B, C> g,
            Function<C, D> h,
            Function<D, E> i) {
        return a -> i.apply(h.apply(g.apply(f.apply(a))));
    }

    /**
     * Partially applies the first argument of a BiFunction.
     */
    public static <A, B, R> Function<B, R> partial(BiFunction<A, B, R> f, A a) {
        return b -> f.apply(a, b);
    }

    /**
     * Partially applies the second argument of a BiFunction.
     */
    public static <A, B, R> Function<A, R> partial2(BiFunction<A, B, R> f, B b) {
        return a -> f.apply(a, b);
    }

    /**
     * Curries a BiFunction into nested Functions.
     */
    public static <A, B, R> Function<A, Function<B, R>> curry(BiFunction<A, B, R> f) {
        return a -> b -> f.apply(a, b);
    }

    /**
     * Uncurries nested Functions back into a BiFunction.
     */
    public static <A, B, R> BiFunction<A, B, R> uncurry(Function<A, Function<B, R>> f) {
        return (a, b) -> f.apply(a).apply(b);
    }

    /**
     * Flips the arguments of a BiFunction.
     */
    public static <A, B, R> BiFunction<B, A, R> flip(BiFunction<A, B, R> f) {
        return (b, a) -> f.apply(a, b);
    }

    /**
     * Creates a memoized version of a function (caches results).
     */
    public static <A, R> Function<A, R> memoize(Function<A, R> f) {
        Map<A, R> cache = new ConcurrentHashMap<>();
        return a -> cache.computeIfAbsent(a, f);
    }

    /**
     * Creates a memoized supplier (lazy singleton).
     */
    public static <T> Supplier<T> memoize(Supplier<T> supplier) {
        return new Supplier<T>() {
            private volatile T value;
            private volatile boolean computed = false;

            @Override
            public synchronized T get() {
                if (!computed) {
                    value = supplier.get();
                    computed = true;
                }
                return value;
            }
        };
    }

    /**
     * Creates a function that always returns the same value.
     */
    public static <A, R> Function<A, R> constant(R value) {
        return a -> value;
    }

    /**
     * The identity function.
     */
    public static <T> Function<T, T> identity() {
        return t -> t;
    }

    /**
     * Creates a function that runs the first function, ignores its result,
     * then returns the result of the second function.
     */
    public static <A, B, C> Function<A, C> andThen(Function<A, B> f, Function<A, C> g) {
        return a -> {
            f.apply(a);
            return g.apply(a);
        };
    }

    /**
     * Converts a function that throws checked exceptions to one that wraps them in RuntimeException.
     */
    public static <A, R> Function<A, R> unchecked(ThrowingFunction<A, R> f) {
        return a -> {
            try {
                return f.apply(a);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Converts a supplier that throws checked exceptions to one that wraps them in RuntimeException.
     */
    public static <T> Supplier<T> unchecked(ThrowingSupplier<T> supplier) {
        return () -> {
            try {
                return supplier.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * Converts a runnable that throws checked exceptions to one that wraps them in RuntimeException.
     */
    public static Runnable unchecked(ThrowingRunnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * A function that can throw checked exceptions.
     */
    @FunctionalInterface
    interface ThrowingFunction<A, R> {
        R apply(A a) throws Exception;
    }

    /**
     * A supplier that can throw checked exceptions.
     */
    @FunctionalInterface
    interface ThrowingSupplier<T> {
        T get() throws Exception;
    }

    /**
     * A runnable that can throw checked exceptions.
     */
    @FunctionalInterface
    interface ThrowingRunnable {
        void run() throws Exception;
    }
}
