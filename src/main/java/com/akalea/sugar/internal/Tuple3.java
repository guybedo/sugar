package com.akalea.sugar.internal;

import java.util.Objects;

/**
 * A tuple of three elements.
 */
public class Tuple3<T1, T2, T3> {

    private T1 first;
    private T2 second;
    private T3 third;

    public Tuple3() {
    }

    public Tuple3(T1 first, T2 second, T3 third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 first, T2 second, T3 third) {
        return new Tuple3<>(first, second, third);
    }

    public T1 getFirst() {
        return first;
    }

    public Tuple3<T1, T2, T3> setFirst(T1 first) {
        this.first = first;
        return this;
    }

    public T2 getSecond() {
        return second;
    }

    public Tuple3<T1, T2, T3> setSecond(T2 second) {
        this.second = second;
        return this;
    }

    public T3 getThird() {
        return third;
    }

    public Tuple3<T1, T2, T3> setThird(T3 third) {
        this.third = third;
        return this;
    }

    /**
     * Applies a function to all three elements.
     */
    public <R> R map(Function3<T1, T2, T3, R> mapper) {
        return mapper.apply(first, second, third);
    }

    /**
     * Transforms the first element.
     */
    public <R> Tuple3<R, T2, T3> mapFirst(java.util.function.Function<T1, R> mapper) {
        return new Tuple3<>(mapper.apply(first), second, third);
    }

    /**
     * Transforms the second element.
     */
    public <R> Tuple3<T1, R, T3> mapSecond(java.util.function.Function<T2, R> mapper) {
        return new Tuple3<>(first, mapper.apply(second), third);
    }

    /**
     * Transforms the third element.
     */
    public <R> Tuple3<T1, T2, R> mapThird(java.util.function.Function<T3, R> mapper) {
        return new Tuple3<>(first, second, mapper.apply(third));
    }

    /**
     * Converts to a Pair dropping the third element.
     */
    public Pair<T1, T2> toPair() {
        return new Pair<T1, T2>().setFirst(first).setSecond(second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple3<?, ?, ?> tuple3 = (Tuple3<?, ?, ?>) o;
        return Objects.equals(first, tuple3.first) &&
               Objects.equals(second, tuple3.second) &&
               Objects.equals(third, tuple3.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ")";
    }

    /**
     * Functional interface for functions with 3 arguments.
     */
    @FunctionalInterface
    public interface Function3<T1, T2, T3, R> {
        R apply(T1 t1, T2 t2, T3 t3);
    }
}
