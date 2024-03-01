package com.akalea.sugar.internal;

public class Pair<T1, T2> {

    private T1 first;
    private T2 second;

    public T1 getFirst() {
        return first;
    }

    public Pair<T1, T2> setFirst(T1 first) {
        this.first = first;
        return this;
    }

    public T2 getSecond() {
        return second;
    }

    public Pair<T1, T2> setSecond(T2 second) {
        this.second = second;
        return this;
    }

}
