package com.akalea.sugar.internal;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A container for computations that may fail.
 * Represents either a successful value (Success) or a failure with an exception (Failure).
 */
public abstract class Try<T> {

    /**
     * Creates a Try from a supplier that may throw an exception.
     */
    public static <T> Try<T> of(Supplier<T> supplier) {
        try {
            return success(supplier.get());
        } catch (Exception e) {
            return failure(e);
        }
    }

    /**
     * Creates a successful Try with the given value.
     */
    public static <T> Try<T> success(T value) {
        return new Success<>(value);
    }

    /**
     * Creates a failed Try with the given exception.
     */
    public static <T> Try<T> failure(Exception exception) {
        return new Failure<>(exception);
    }

    /**
     * Returns true if this is a Success.
     */
    public abstract boolean isSuccess();

    /**
     * Returns true if this is a Failure.
     */
    public abstract boolean isFailure();

    /**
     * Gets the value if successful, throws if failure.
     */
    public abstract T get();

    /**
     * Gets the exception if failure, throws if success.
     */
    public abstract Exception getException();

    /**
     * Gets the value if successful, otherwise returns the default value.
     */
    public abstract T getOrElse(T defaultValue);

    /**
     * Gets the value if successful, otherwise computes a default value.
     */
    public abstract T getOrElse(Supplier<T> supplier);

    /**
     * Gets the value if successful, otherwise returns null.
     */
    public T getOrNull() {
        return getOrElse((T) null);
    }

    /**
     * Maps the value if successful.
     */
    public abstract <R> Try<R> map(Function<T, R> mapper);

    /**
     * FlatMaps the value if successful.
     */
    public abstract <R> Try<R> flatMap(Function<T, Try<R>> mapper);

    /**
     * Filters the value if successful.
     */
    public abstract Try<T> filter(Predicate<T> predicate);

    /**
     * Recovers from a failure with a default value.
     */
    public abstract Try<T> recover(Function<Exception, T> recovery);

    /**
     * Recovers from a failure with another Try.
     */
    public abstract Try<T> recoverWith(Function<Exception, Try<T>> recovery);

    /**
     * Executes the consumer if successful.
     */
    public abstract Try<T> onSuccess(Consumer<T> consumer);

    /**
     * Executes the consumer if failure.
     */
    public abstract Try<T> onFailure(Consumer<Exception> consumer);

    /**
     * Converts to an Optional.
     */
    public abstract Optional<T> toOptional();

    /**
     * Converts to an Either with the exception as Left.
     */
    public abstract Either<Exception, T> toEither();

    /**
     * Success case implementation.
     */
    private static class Success<T> extends Try<T> {
        private final T value;

        Success(T value) {
            this.value = value;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public Exception getException() {
            throw new NoSuchElementException("Success has no exception");
        }

        @Override
        public T getOrElse(T defaultValue) {
            return value;
        }

        @Override
        public T getOrElse(Supplier<T> supplier) {
            return value;
        }

        @Override
        public <R> Try<R> map(Function<T, R> mapper) {
            return Try.of(() -> mapper.apply(value));
        }

        @Override
        public <R> Try<R> flatMap(Function<T, Try<R>> mapper) {
            try {
                return mapper.apply(value);
            } catch (Exception e) {
                return failure(e);
            }
        }

        @Override
        public Try<T> filter(Predicate<T> predicate) {
            try {
                if (predicate.test(value)) {
                    return this;
                }
                return failure(new NoSuchElementException("Predicate does not match"));
            } catch (Exception e) {
                return failure(e);
            }
        }

        @Override
        public Try<T> recover(Function<Exception, T> recovery) {
            return this;
        }

        @Override
        public Try<T> recoverWith(Function<Exception, Try<T>> recovery) {
            return this;
        }

        @Override
        public Try<T> onSuccess(Consumer<T> consumer) {
            consumer.accept(value);
            return this;
        }

        @Override
        public Try<T> onFailure(Consumer<Exception> consumer) {
            return this;
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.ofNullable(value);
        }

        @Override
        public Either<Exception, T> toEither() {
            return Either.right(value);
        }
    }

    /**
     * Failure case implementation.
     */
    private static class Failure<T> extends Try<T> {
        private final Exception exception;

        Failure(Exception exception) {
            this.exception = exception;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public T get() {
            throw new RuntimeException(exception);
        }

        @Override
        public Exception getException() {
            return exception;
        }

        @Override
        public T getOrElse(T defaultValue) {
            return defaultValue;
        }

        @Override
        public T getOrElse(Supplier<T> supplier) {
            return supplier.get();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <R> Try<R> map(Function<T, R> mapper) {
            return (Try<R>) this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <R> Try<R> flatMap(Function<T, Try<R>> mapper) {
            return (Try<R>) this;
        }

        @Override
        public Try<T> filter(Predicate<T> predicate) {
            return this;
        }

        @Override
        public Try<T> recover(Function<Exception, T> recovery) {
            return Try.of(() -> recovery.apply(exception));
        }

        @Override
        public Try<T> recoverWith(Function<Exception, Try<T>> recovery) {
            try {
                return recovery.apply(exception);
            } catch (Exception e) {
                return failure(e);
            }
        }

        @Override
        public Try<T> onSuccess(Consumer<T> consumer) {
            return this;
        }

        @Override
        public Try<T> onFailure(Consumer<Exception> consumer) {
            consumer.accept(exception);
            return this;
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.empty();
        }

        @Override
        public Either<Exception, T> toEither() {
            return Either.left(exception);
        }
    }
}
