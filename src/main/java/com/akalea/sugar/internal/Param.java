package com.akalea.sugar.internal;

public class Param<T> extends KeyValue<String, T> {

    public String getId() {
        return key;
    }

    public Param<T> setId(String id) {
        this.key = id;
        return this;
    }

    @Override
    public Param<T> setValue(T value) {
        return (Param<T>) super.setValue(value);
    }

}
