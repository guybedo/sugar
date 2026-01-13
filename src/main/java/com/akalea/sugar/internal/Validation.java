package com.akalea.sugar.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A validation type that accumulates errors instead of fail-fast.
 * Unlike Either, Validation can collect multiple errors before failing.
 */
public abstract class Validation<E, T> {

    /**
     * Creates a valid Validation.
     */
    public static <E, T> Validation<E, T> valid(T value) {
        return new Valid<>(value);
    }

    /**
     * Creates an invalid Validation with one error.
     */
    public static <E, T> Validation<E, T> invalid(E error) {
        List<E> errors = new ArrayList<>();
        errors.add(error);
        return new Invalid<>(errors);
    }

    /**
     * Creates an invalid Validation with multiple errors.
     */
    public static <E, T> Validation<E, T> invalid(List<E> errors) {
        return new Invalid<>(new ArrayList<>(errors));
    }

    /**
     * Creates a Validation from a predicate test.
     */
    public static <E, T> Validation<E, T> fromPredicate(T value, java.util.function.Predicate<T> predicate, E error) {
        if (predicate.test(value)) {
            return valid(value);
        }
        return invalid(error);
    }

    /**
     * Returns true if this is valid.
     */
    public abstract boolean isValid();

    /**
     * Returns true if this is invalid.
     */
    public abstract boolean isInvalid();

    /**
     * Gets the value if valid, throws if invalid.
     */
    public abstract T getValue();

    /**
     * Gets the errors if invalid, empty list if valid.
     */
    public abstract List<E> getErrors();

    /**
     * Gets the value or returns a default.
     */
    public abstract T getOrElse(T defaultValue);

    /**
     * Maps the value if valid.
     */
    public abstract <R> Validation<E, R> map(Function<T, R> mapper);

    /**
     * Maps the errors if invalid.
     */
    public abstract <F> Validation<F, T> mapErrors(Function<E, F> mapper);

    /**
     * Converts to an Either.
     */
    public abstract Either<List<E>, T> toEither();

    /**
     * Combines two validations, accumulating errors.
     */
    public static <E, T1, T2, R> Validation<E, R> combine(
            Validation<E, T1> v1,
            Validation<E, T2> v2,
            BiFunction<T1, T2, R> combiner) {
        if (v1.isValid() && v2.isValid()) {
            return valid(combiner.apply(v1.getValue(), v2.getValue()));
        }
        List<E> errors = new ArrayList<>();
        errors.addAll(v1.getErrors());
        errors.addAll(v2.getErrors());
        return invalid(errors);
    }

    /**
     * Combines three validations, accumulating errors.
     */
    public static <E, T1, T2, T3, R> Validation<E, R> combine(
            Validation<E, T1> v1,
            Validation<E, T2> v2,
            Validation<E, T3> v3,
            Function3<T1, T2, T3, R> combiner) {
        if (v1.isValid() && v2.isValid() && v3.isValid()) {
            return valid(combiner.apply(v1.getValue(), v2.getValue(), v3.getValue()));
        }
        List<E> errors = new ArrayList<>();
        errors.addAll(v1.getErrors());
        errors.addAll(v2.getErrors());
        errors.addAll(v3.getErrors());
        return invalid(errors);
    }

    /**
     * Combines four validations, accumulating errors.
     */
    public static <E, T1, T2, T3, T4, R> Validation<E, R> combine(
            Validation<E, T1> v1,
            Validation<E, T2> v2,
            Validation<E, T3> v3,
            Validation<E, T4> v4,
            Function4<T1, T2, T3, T4, R> combiner) {
        if (v1.isValid() && v2.isValid() && v3.isValid() && v4.isValid()) {
            return valid(combiner.apply(v1.getValue(), v2.getValue(), v3.getValue(), v4.getValue()));
        }
        List<E> errors = new ArrayList<>();
        errors.addAll(v1.getErrors());
        errors.addAll(v2.getErrors());
        errors.addAll(v3.getErrors());
        errors.addAll(v4.getErrors());
        return invalid(errors);
    }

    /**
     * Collects validations from a list, accumulating all errors.
     */
    public static <E, T> Validation<E, List<T>> sequence(List<Validation<E, T>> validations) {
        List<E> errors = new ArrayList<>();
        List<T> values = new ArrayList<>();
        for (Validation<E, T> v : validations) {
            if (v.isValid()) {
                values.add(v.getValue());
            } else {
                errors.addAll(v.getErrors());
            }
        }
        if (errors.isEmpty()) {
            return valid(values);
        }
        return invalid(errors);
    }

    /**
     * Valid case implementation.
     */
    private static class Valid<E, T> extends Validation<E, T> {
        private final T value;

        Valid(T value) {
            this.value = value;
        }

        @Override
        public boolean isValid() {
            return true;
        }

        @Override
        public boolean isInvalid() {
            return false;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public List<E> getErrors() {
            return new ArrayList<>();
        }

        @Override
        public T getOrElse(T defaultValue) {
            return value;
        }

        @Override
        public <R> Validation<E, R> map(Function<T, R> mapper) {
            return valid(mapper.apply(value));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <F> Validation<F, T> mapErrors(Function<E, F> mapper) {
            return (Validation<F, T>) this;
        }

        @Override
        public Either<List<E>, T> toEither() {
            return Either.right(value);
        }
    }

    /**
     * Invalid case implementation.
     */
    private static class Invalid<E, T> extends Validation<E, T> {
        private final List<E> errors;

        Invalid(List<E> errors) {
            this.errors = errors;
        }

        @Override
        public boolean isValid() {
            return false;
        }

        @Override
        public boolean isInvalid() {
            return true;
        }

        @Override
        public T getValue() {
            throw new IllegalStateException("Cannot get value from invalid validation: " + errors);
        }

        @Override
        public List<E> getErrors() {
            return new ArrayList<>(errors);
        }

        @Override
        public T getOrElse(T defaultValue) {
            return defaultValue;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <R> Validation<E, R> map(Function<T, R> mapper) {
            return (Validation<E, R>) this;
        }

        @Override
        public <F> Validation<F, T> mapErrors(Function<E, F> mapper) {
            List<F> mapped = new ArrayList<>();
            for (E e : errors) {
                mapped.add(mapper.apply(e));
            }
            return invalid(mapped);
        }

        @Override
        public Either<List<E>, T> toEither() {
            return Either.left(errors);
        }
    }

    /**
     * Functional interface for functions with 3 arguments.
     */
    @FunctionalInterface
    public interface Function3<T1, T2, T3, R> {
        R apply(T1 t1, T2 t2, T3 t3);
    }

    /**
     * Functional interface for functions with 4 arguments.
     */
    @FunctionalInterface
    public interface Function4<T1, T2, T3, T4, R> {
        R apply(T1 t1, T2 t2, T3 t3, T4 t4);
    }
}
