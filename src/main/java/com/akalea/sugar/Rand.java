package com.akalea.sugar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Rand {
    public static Random random = new Random();

    public static <T> T choose(List<T> values) {
        if (values == null || values.isEmpty())
            return null;
        return values.get(random.nextInt(values.size()));
    }

    public static <T> List<T> choose(List<T> values, int count) {
        if (values == null || values.isEmpty())
            return null;
        if (count >= values.size())
            return new ArrayList(values);
        Set<T> choice = new HashSet();
        while (choice.size() < count)
            choice.add(choose(values));
        return new ArrayList(choice);
    }

    public static String rand(int size) {
        StringBuilder s = new StringBuilder(size);
        for (int i = 0; i < size; i++)
            s.append(randChar());
        return s.toString();
    }

    public static String randA(int size) {
        StringBuilder s = new StringBuilder(size);
        for (int i = 0; i < size; i++)
            s.append(randA());
        return s.toString();
    }

    public static String randN(int size) {
        StringBuilder s = new StringBuilder(size);
        for (int i = 0; i < size; i++)
            s.append(randN());
        return s.toString();
    }

    public static char randChar() {
        int val = random.nextInt(10 + 2 * 26);
        if (val < 10) {
            return (char) (val + 48);
        } else if (val < 10 + 26) {
            return (char) (val - 10 + 65);
        } else {
            return (char) (val - (10 + 26) + 97);
        }
    }

    public static char randN() {
        int val = random.nextInt(10);
        return (char) (val + 48);
    }

    public static char randA() {
        int val = random.nextInt(2 * 26);
        if (val < 26) {
            return (char) (val + 65);
        } else {
            return (char) (val - 26 + 97);
        }
    }
}
