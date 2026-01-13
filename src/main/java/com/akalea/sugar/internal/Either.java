package com.akalea.sugar.internal;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A union type representing either a Left value or a Right value.
 * By convention, Left is typically used for errors and Right for success values.
 */
public abstract class Either<L, R> {

    /**
     * Creates a Left Either.
     */
    public static <L, R> Either<L, R> left(L value) {
        return new Left<>(value);
    }

    /**
     * Creates a Right Either.
     */
    public static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }

    /**
     * Returns true if this is a Left.
     */
    public abstract boolean isLeft();

    /**
     * Returns true if this is a Right.
     */
    public abstract boolean isRight();

    /**
     * Gets the Left value or throws if Right.
     */
    public abstract L getLeft();

    /**
     * Gets the Right value or throws if Left.
     */
    public abstract R getRight();

    /**
     * Gets the Right value or returns the default if Left.
     */
    public abstract R getOrElse(R defaultValue);

    /**
     * Gets the Right value or computes a value from the Left.
     */
    public abstract R getOrElse(Function<L, R> mapper);

    /**
     * Maps the Right value.
     */
    public abstract <T> Either<L, T> map(Function<R, T> mapper);

    /**
     * Maps the Left value.
     */
    public abstract <T> Either<T, R> mapLeft(Function<L, T> mapper);

    /**
     * FlatMaps the Right value.
     */
    public abstract <T> Either<L, T> flatMap(Function<R, Either<L, T>> mapper);

    /**
     * Folds both sides into a single value.
     */
    public abstract <T> T fold(Function<L, T> leftMapper, Function<R, T> rightMapper);

    /**
     * Swaps Left and Right.
     */
    public abstract Either<R, L> swap();

    /**
     * Executes consumer if Right.
     */
    public abstract Either<L, R> onRight(Consumer<R> consumer);

    /**
     * Executes consumer if Left.
     */
    public abstract Either<L, R> onLeft(Consumer<L> consumer);

    /**
     * Converts the Right side to an Optional.
     */
    public abstract Optional<R> toOptional();

    /**
     * Left case implementation.
     */
    private static class Left<L, R> extends Either<L, R> {
        private final L value;

        Left(L value) {
            this.value = value;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public L getLeft() {
            return value;
        }

        @Override
        public R getRight() {
            throw new NoSuchElementException("Left has no right value");
        }

        @Override
        public R getOrElse(R defaultValue) {
            return defaultValue;
        }

        @Override
        public R getOrElse(Function<L, R> mapper) {
            return mapper.apply(value);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> Either<L, T> map(Function<R, T> mapper) {
            return (Either<L, T>) this;
        }

        @Override
        public <T> Either<T, R> mapLeft(Function<L, T> mapper) {
            return left(mapper.apply(value));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> Either<L, T> flatMap(Function<R, Either<L, T>> mapper) {
            return (Either<L, T>) this;
        }

        @Override
        public <T> T fold(Function<L, T> leftMapper, Function<R, T> rightMapper) {
            return leftMapper.apply(value);
        }

        @Override
        public Either<R, L> swap() {
            return right(value);
        }

        @Override
        public Either<L, R> onRight(Consumer<R> consumer) {
            return this;
        }

        @Override
        public Either<L, R> onLeft(Consumer<L> consumer) {
            consumer.accept(value);
            return this;
        }

        @Override
        public Optional<R> toOptional() {
            return Optional.empty();
        }
    }

    /**
     * Right case implementation.
     */
    private static class Right<L, R> extends Either<L, R> {
        private final R value;

        Right(R value) {
            this.value = value;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public L getLeft() {
            throw new NoSuchElementException("Right has no left value");
        }

        @Override
        public R getRight() {
            return value;
        }

        @Override
        public R getOrElse(R defaultValue) {
            return value;
        }

        @Override
        public R getOrElse(Function<L, R> mapper) {
            return value;
        }

        @Override
        public <T> Either<L, T> map(Function<R, T> mapper) {
            return right(mapper.apply(value));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> Either<T, R> mapLeft(Function<L, T> mapper) {
            return (Either<T, R>) this;
        }

        @Override
        public <T> Either<L, T> flatMap(Function<R, Either<L, T>> mapper) {
            return mapper.apply(value);
        }

        @Override
        public <T> T fold(Function<L, T> leftMapper, Function<R, T> rightMapper) {
            return rightMapper.apply(value);
        }

        @Override
        public Either<R, L> swap() {
            return left(value);
        }

        @Override
        public Either<L, R> onRight(Consumer<R> consumer) {
            consumer.accept(value);
            return this;
        }

        @Override
        public Either<L, R> onLeft(Consumer<L> consumer) {
            return this;
        }

        @Override
        public Optional<R> toOptional() {
            return Optional.ofNullable(value);
        }
    }
}
