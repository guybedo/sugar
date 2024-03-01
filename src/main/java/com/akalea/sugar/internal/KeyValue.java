package com.akalea.sugar.internal;

public class KeyValue<K, V> {
    protected K key;
    protected V value;

    public K getKey() {
        return key;
    }

    public KeyValue<K, V> setKey(K id) {
        this.key = id;
        return this;
    }

    public V getValue() {
        return value;
    }

    public KeyValue<K, V> setValue(V value) {
        this.value = value;
        return this;
    }

}
