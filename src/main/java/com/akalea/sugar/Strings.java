package com.akalea.sugar;

import java.util.List;
import static com.akalea.sugar.Collections.*;

public interface Strings {

    public static String join(List<String> strings) {
        return join(strings, "");
    }

    public static String join(List<String> strings, String sep) {
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

    public static String format(String string, Object... params) {
        return String.format(string, params);
    }
}
