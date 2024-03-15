package com.akalea.sugar;

import java.lang.reflect.Array;

public class Arrays {

    public static <T> T[] array(int size, T value) {
        try {
            T[] array = (T[]) Array.newInstance(value.getClass(), size);
            java.util.Arrays.fill(array, value);
            return array;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
