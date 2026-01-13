package com.akalea.sugar.internal;

import java.util.Objects;

/**
 * A tuple of four elements.
 */
public class Tuple4<T1, T2, T3, T4> {

    private T1 first;
    private T2 second;
    private T3 third;
    private T4 fourth;

    public Tuple4() {
    }

    public Tuple4(T1 first, T2 second, T3 third, T4 fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 first, T2 second, T3 third, T4 fourth) {
        return new Tuple4<>(first, second, third, fourth);
    }

    public T1 getFirst() {
        return first;
    }

    public Tuple4<T1, T2, T3, T4> setFirst(T1 first) {
        this.first = first;
        return this;
    }

    public T2 getSecond() {
        return second;
    }

    public Tuple4<T1, T2, T3, T4> setSecond(T2 second) {
        this.second = second;
        return this;
    }

    public T3 getThird() {
        return third;
    }

    public Tuple4<T1, T2, T3, T4> setThird(T3 third) {
        this.third = third;
        return this;
    }

    public T4 getFourth() {
        return fourth;
    }

    public Tuple4<T1, T2, T3, T4> setFourth(T4 fourth) {
        this.fourth = fourth;
        return this;
    }

    /**
     * Applies a function to all four elements.
     */
    public <R> R map(Function4<T1, T2, T3, T4, R> mapper) {
        return mapper.apply(first, second, third, fourth);
    }

    /**
     * Transforms the first element.
     */
    public <R> Tuple4<R, T2, T3, T4> mapFirst(java.util.function.Function<T1, R> mapper) {
        return new Tuple4<>(mapper.apply(first), second, third, fourth);
    }

    /**
     * Transforms the second element.
     */
    public <R> Tuple4<T1, R, T3, T4> mapSecond(java.util.function.Function<T2, R> mapper) {
        return new Tuple4<>(first, mapper.apply(second), third, fourth);
    }

    /**
     * Transforms the third element.
     */
    public <R> Tuple4<T1, T2, R, T4> mapThird(java.util.function.Function<T3, R> mapper) {
        return new Tuple4<>(first, second, mapper.apply(third), fourth);
    }

    /**
     * Transforms the fourth element.
     */
    public <R> Tuple4<T1, T2, T3, R> mapFourth(java.util.function.Function<T4, R> mapper) {
        return new Tuple4<>(first, second, third, mapper.apply(fourth));
    }

    /**
     * Converts to a Tuple3 dropping the fourth element.
     */
    public Tuple3<T1, T2, T3> toTuple3() {
        return new Tuple3<>(first, second, third);
    }

    /**
     * Converts to a Pair dropping the third and fourth elements.
     */
    public Pair<T1, T2> toPair() {
        return new Pair<T1, T2>().setFirst(first).setSecond(second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple4<?, ?, ?, ?> tuple4 = (Tuple4<?, ?, ?, ?>) o;
        return Objects.equals(first, tuple4.first) &&
               Objects.equals(second, tuple4.second) &&
               Objects.equals(third, tuple4.third) &&
               Objects.equals(fourth, tuple4.fourth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third, fourth);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ", " + fourth + ")";
    }

    /**
     * Functional interface for functions with 4 arguments.
     */
    @FunctionalInterface
    public interface Function4<T1, T2, T3, T4, R> {
        R apply(T1 t1, T2 t2, T3 t3, T4 t4);
    }
}
