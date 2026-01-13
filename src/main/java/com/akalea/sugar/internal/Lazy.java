package com.akalea.sugar.internal;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A lazy evaluation wrapper that computes its value on first access.
 * Thread-safe: the supplier is guaranteed to be called at most once.
 */
public class Lazy<T> {

    private volatile T value;
    private volatile boolean evaluated = false;
    private final Supplier<T> supplier;

    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Creates a lazy value from a supplier.
     */
    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }

    /**
     * Creates an already-evaluated lazy value.
     */
    public static <T> Lazy<T> evaluated(T value) {
        Lazy<T> lazy = new Lazy<>(() -> value);
        lazy.value = value;
        lazy.evaluated = true;
        return lazy;
    }

    /**
     * Gets the value, computing it if necessary.
     * Thread-safe: uses double-checked locking.
     */
    public T get() {
        if (!evaluated) {
            synchronized (this) {
                if (!evaluated) {
                    value = supplier.get();
                    evaluated = true;
                }
            }
        }
        return value;
    }

    /**
     * Returns true if the value has been computed.
     */
    public boolean isEvaluated() {
        return evaluated;
    }

    /**
     * Gets the value if evaluated, otherwise returns the default value.
     * Does NOT trigger evaluation.
     */
    public T getOrElse(T defaultValue) {
        return evaluated ? value : defaultValue;
    }

    /**
     * Gets the value if evaluated, otherwise computes a default value.
     * Does NOT trigger evaluation.
     */
    public T getOrElse(Supplier<T> defaultSupplier) {
        return evaluated ? value : defaultSupplier.get();
    }

    /**
     * Gets the value, computing it if necessary.
     * If evaluation fails with an exception, returns the default value.
     */
    public T getOrDefault(T defaultValue) {
        try {
            return get();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Maps the lazy value using the given function.
     * Returns a new Lazy that will apply the function when evaluated.
     */
    public <R> Lazy<R> map(Function<T, R> mapper) {
        return Lazy.of(() -> mapper.apply(get()));
    }

    /**
     * FlatMaps the lazy value using the given function.
     * Returns a new Lazy that will apply the function and unwrap when evaluated.
     */
    public <R> Lazy<R> flatMap(Function<T, Lazy<R>> mapper) {
        return Lazy.of(() -> mapper.apply(get()).get());
    }

    /**
     * Filters the lazy value using the given predicate.
     * Returns an Optional containing the value if it passes the predicate, empty otherwise.
     * Triggers evaluation.
     */
    public Optional<T> filter(java.util.function.Predicate<T> predicate) {
        T val = get();
        return predicate.test(val) ? Optional.ofNullable(val) : Optional.empty();
    }

    /**
     * Executes the consumer if the value has been evaluated.
     * Does NOT trigger evaluation.
     */
    public Lazy<T> ifEvaluated(Consumer<T> consumer) {
        if (evaluated) {
            consumer.accept(value);
        }
        return this;
    }

    /**
     * Executes the consumer with the value, triggering evaluation if necessary.
     */
    public Lazy<T> peek(Consumer<T> consumer) {
        consumer.accept(get());
        return this;
    }

    /**
     * Converts to an Optional.
     * Triggers evaluation.
     */
    public Optional<T> toOptional() {
        return Optional.ofNullable(get());
    }

    /**
     * Converts to a Try, capturing any exception during evaluation.
     */
    public Try<T> toTry() {
        return Try.of(this::get);
    }

    @Override
    public String toString() {
        if (evaluated) {
            return "Lazy(" + value + ")";
        } else {
            return "Lazy(<not evaluated>)";
        }
    }
}
