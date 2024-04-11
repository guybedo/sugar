package com.akalea.sugar;

import static com.akalea.sugar.Collections.*;

import java.util.Collection;
import java.util.stream.IntStream;

public interface Strings {

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static String format(String str, Object... args) {
        return String.format(str, args);
    }

    public static String join(Collection<String> strings) {
        return join(strings, "");
    }

    public static String join(Collection<String> strings, String sep) {
        StringBuffer buffer = new StringBuffer();
        int size = strings.size();
        enumerate(
            strings,
            (i, s) -> {
                buffer.append(s);
                if (i < size - 1)
                    buffer.append(sep);
            });
        return buffer.toString();
    }

    public static int firstNonAlphanum(String str) {
        return IntStream
            .range(0, str.length())
            .filter(i -> !Character.isLetterOrDigit(str.charAt(i)))
            .findFirst()
            .orElse(-1);
    }

    public static int firstAlphanum(String str) {
        return IntStream
            .range(0, str.length())
            .filter(i -> Character.isLetterOrDigit(str.charAt(i)))
            .findFirst()
            .orElse(-1);
    }
}
