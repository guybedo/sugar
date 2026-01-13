package com.akalea.sugar;

import static com.akalea.sugar.Collections.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public interface Strings {

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
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

    // ==================== Additional String Utilities ====================

    public static List<String> split(String str, String delimiter) {
        if (str == null)
            return new ArrayList<>();
        return list(str.split(Pattern.quote(delimiter)));
    }

    public static List<String> splitRegex(String str, String regex) {
        if (str == null)
            return new ArrayList<>();
        return list(str.split(regex));
    }

    public static String truncate(String str, int maxLength) {
        return truncate(str, maxLength, "...");
    }

    public static String truncate(String str, int maxLength, String suffix) {
        if (str == null)
            return null;
        if (str.length() <= maxLength)
            return str;
        int truncateAt = maxLength - suffix.length();
        if (truncateAt <= 0)
            return suffix.substring(0, maxLength);
        return str.substring(0, truncateAt) + suffix;
    }

    public static String padLeft(String str, int length, char padChar) {
        if (str == null)
            str = "";
        if (str.length() >= length)
            return str;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length - str.length(); i++) {
            sb.append(padChar);
        }
        sb.append(str);
        return sb.toString();
    }

    public static String padRight(String str, int length, char padChar) {
        if (str == null)
            str = "";
        if (str.length() >= length)
            return str;
        StringBuilder sb = new StringBuilder(str);
        for (int i = str.length(); i < length; i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }

    public static String repeat(String str, int times) {
        if (str == null || times <= 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    public static String reverse(String str) {
        if (str == null)
            return null;
        return new StringBuilder(str).reverse().toString();
    }

    public static String capitalize(String str) {
        if (isEmpty(str))
            return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static String uncapitalize(String str) {
        if (isEmpty(str))
            return str;
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    public static String camelCase(String str) {
        if (isEmpty(str))
            return str;
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;
        for (char c : str.toCharArray()) {
            if (c == '_' || c == '-' || c == ' ') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
    }

    public static String snakeCase(String str) {
        if (isEmpty(str))
            return str;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0)
                    result.append('_');
                result.append(Character.toLowerCase(c));
            } else if (c == '-' || c == ' ') {
                result.append('_');
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String kebabCase(String str) {
        if (isEmpty(str))
            return str;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0)
                    result.append('-');
                result.append(Character.toLowerCase(c));
            } else if (c == '_' || c == ' ') {
                result.append('-');
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String template(String template, Map<String, Object> values) {
        if (template == null || values == null)
            return template;
        String result = template;
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            result = result.replace(placeholder, value);
        }
        return result;
    }

    @SafeVarargs
    public static String template(String template, com.akalea.sugar.internal.KeyValue<String, Object>... values) {
        if (template == null)
            return null;
        String result = template;
        for (com.akalea.sugar.internal.KeyValue<String, Object> kv : values) {
            String placeholder = "${" + kv.getKey() + "}";
            String value = kv.getValue() != null ? kv.getValue().toString() : "";
            result = result.replace(placeholder, value);
        }
        return result;
    }

    public static boolean contains(String str, String search) {
        if (str == null || search == null)
            return false;
        return str.contains(search);
    }

    public static boolean containsIgnoreCase(String str, String search) {
        if (str == null || search == null)
            return false;
        return str.toLowerCase().contains(search.toLowerCase());
    }

    public static boolean startsWith(String str, String prefix) {
        if (str == null || prefix == null)
            return false;
        return str.startsWith(prefix);
    }

    public static boolean endsWith(String str, String suffix) {
        if (str == null || suffix == null)
            return false;
        return str.endsWith(suffix);
    }

    public static String removePrefix(String str, String prefix) {
        if (str == null || prefix == null)
            return str;
        if (str.startsWith(prefix))
            return str.substring(prefix.length());
        return str;
    }

    public static String removeSuffix(String str, String suffix) {
        if (str == null || suffix == null)
            return str;
        if (str.endsWith(suffix))
            return str.substring(0, str.length() - suffix.length());
        return str;
    }

    public static String substringBefore(String str, String separator) {
        if (isEmpty(str) || separator == null)
            return str;
        int pos = str.indexOf(separator);
        if (pos < 0)
            return str;
        return str.substring(0, pos);
    }

    public static String substringAfter(String str, String separator) {
        if (isEmpty(str) || separator == null)
            return str;
        int pos = str.indexOf(separator);
        if (pos < 0)
            return "";
        return str.substring(pos + separator.length());
    }

    public static String substringBeforeLast(String str, String separator) {
        if (isEmpty(str) || separator == null)
            return str;
        int pos = str.lastIndexOf(separator);
        if (pos < 0)
            return str;
        return str.substring(0, pos);
    }

    public static String substringAfterLast(String str, String separator) {
        if (isEmpty(str) || separator == null)
            return str;
        int pos = str.lastIndexOf(separator);
        if (pos < 0)
            return "";
        return str.substring(pos + separator.length());
    }

    public static int countOccurrences(String str, String search) {
        if (isEmpty(str) || isEmpty(search))
            return 0;
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(search, idx)) != -1) {
            count++;
            idx += search.length();
        }
        return count;
    }

    public static List<Integer> findAllIndexes(String str, String search) {
        List<Integer> indexes = new ArrayList<>();
        if (isEmpty(str) || isEmpty(search))
            return indexes;
        int idx = 0;
        while ((idx = str.indexOf(search, idx)) != -1) {
            indexes.add(idx);
            idx += search.length();
        }
        return indexes;
    }

    public static String defaultIfEmpty(String str, String defaultValue) {
        return isEmpty(str) ? defaultValue : str;
    }

    public static String defaultIfBlank(String str, String defaultValue) {
        return isBlank(str) ? defaultValue : str;
    }

    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    public static String emptyToNull(String str) {
        return isEmpty(str) ? null : str;
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String strip(String str) {
        return str == null ? null : str.strip();
    }
}
