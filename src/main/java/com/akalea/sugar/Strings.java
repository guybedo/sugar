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

    // ==================== New String Operations ====================

    /**
     * Splits a string into lines.
     */
    public static List<String> lines(String str) {
        if (str == null)
            return new ArrayList<>();
        return list(str.split("\\R"));
    }

    /**
     * Splits a string into words (by whitespace).
     */
    public static List<String> words(String str) {
        if (str == null || str.trim().isEmpty())
            return new ArrayList<>();
        return list(str.trim().split("\\s+"));
    }

    /**
     * Indents each line by the specified number of spaces.
     */
    public static String indent(String str, int spaces) {
        if (str == null)
            return null;
        String prefix = repeat(" ", spaces);
        return join(map(lines(str), line -> prefix + line), "\n");
    }

    /**
     * Removes common leading whitespace from all lines.
     */
    public static String dedent(String str) {
        if (str == null)
            return null;
        List<String> linesList = lines(str);
        if (linesList.isEmpty())
            return str;

        int minIndent = Integer.MAX_VALUE;
        for (String line : linesList) {
            if (line.trim().isEmpty())
                continue;
            int indent = 0;
            for (char c : line.toCharArray()) {
                if (c == ' ')
                    indent++;
                else if (c == '\t')
                    indent += 4;
                else
                    break;
            }
            minIndent = Math.min(minIndent, indent);
        }

        if (minIndent == Integer.MAX_VALUE || minIndent == 0)
            return str;

        final int removeIndent = minIndent;
        return join(map(linesList, line -> {
            if (line.length() <= removeIndent)
                return "";
            int charsToRemove = 0;
            int spacesRemoved = 0;
            for (char c : line.toCharArray()) {
                if (spacesRemoved >= removeIndent)
                    break;
                if (c == ' ') {
                    spacesRemoved++;
                    charsToRemove++;
                } else if (c == '\t') {
                    spacesRemoved += 4;
                    charsToRemove++;
                } else {
                    break;
                }
            }
            return line.substring(charsToRemove);
        }), "\n");
    }

    /**
     * Word wraps text at the specified width.
     */
    public static String wrap(String str, int width) {
        if (str == null || width <= 0)
            return str;
        StringBuilder result = new StringBuilder();
        int lineLength = 0;
        for (String word : words(str)) {
            if (lineLength + word.length() > width && lineLength > 0) {
                result.append("\n");
                lineLength = 0;
            }
            if (lineLength > 0) {
                result.append(" ");
                lineLength++;
            }
            result.append(word);
            lineLength += word.length();
        }
        return result.toString();
    }

    /**
     * Creates a URL-friendly slug from a string.
     */
    public static String slugify(String str) {
        if (str == null)
            return null;
        return str.toLowerCase()
            .replaceAll("[^a-z0-9\\s-]", "")
            .replaceAll("\\s+", "-")
            .replaceAll("-+", "-")
            .replaceAll("^-|-$", "");
    }

    /**
     * Masks part of a string, keeping visible characters at start and end.
     */
    public static String mask(String str, int visibleStart, int visibleEnd) {
        return mask(str, visibleStart, visibleEnd, '*');
    }

    /**
     * Masks part of a string with a specified mask character.
     */
    public static String mask(String str, int visibleStart, int visibleEnd, char maskChar) {
        if (str == null)
            return null;
        int len = str.length();
        if (visibleStart + visibleEnd >= len)
            return str;

        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, visibleStart));
        for (int i = visibleStart; i < len - visibleEnd; i++) {
            sb.append(maskChar);
        }
        sb.append(str.substring(len - visibleEnd));
        return sb.toString();
    }

    /**
     * Returns true if the string contains only digits.
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str))
            return false;
        return str.chars().allMatch(Character::isDigit);
    }

    /**
     * Returns true if the string contains only letters.
     */
    public static boolean isAlpha(String str) {
        if (isEmpty(str))
            return false;
        return str.chars().allMatch(Character::isLetter);
    }

    /**
     * Returns true if the string contains only letters and digits.
     */
    public static boolean isAlphanumeric(String str) {
        if (isEmpty(str))
            return false;
        return str.chars().allMatch(Character::isLetterOrDigit);
    }

    /**
     * Returns true if all letters in the string are lowercase.
     */
    public static boolean isLowerCase(String str) {
        if (isEmpty(str))
            return false;
        return str.chars().filter(Character::isLetter).allMatch(Character::isLowerCase);
    }

    /**
     * Returns true if all letters in the string are uppercase.
     */
    public static boolean isUpperCase(String str) {
        if (isEmpty(str))
            return false;
        return str.chars().filter(Character::isLetter).allMatch(Character::isUpperCase);
    }

    /**
     * Centers a string within the specified width.
     */
    public static String center(String str, int width, char padChar) {
        if (str == null)
            str = "";
        if (str.length() >= width)
            return str;
        int totalPad = width - str.length();
        int leftPad = totalPad / 2;
        int rightPad = totalPad - leftPad;
        return repeat(String.valueOf(padChar), leftPad) + str + repeat(String.valueOf(padChar), rightPad);
    }

    /**
     * Replaces the first occurrence of a search string.
     */
    public static String replaceFirst(String str, String search, String replacement) {
        if (str == null || search == null)
            return str;
        return str.replaceFirst(Pattern.quote(search), Matcher.quoteReplacement(replacement == null ? "" : replacement));
    }

    /**
     * Replaces the last occurrence of a search string.
     */
    public static String replaceLast(String str, String search, String replacement) {
        if (str == null || search == null)
            return str;
        int lastIndex = str.lastIndexOf(search);
        if (lastIndex < 0)
            return str;
        return str.substring(0, lastIndex) + (replacement == null ? "" : replacement) + str.substring(lastIndex + search.length());
    }

    /**
     * Converts a string to title case.
     */
    public static String titleCase(String str) {
        if (isEmpty(str))
            return str;
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;
        for (char c : str.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                result.append(c);
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
    }

    /**
     * Extracts initials from a string (first letter of each word).
     */
    public static String initials(String str) {
        if (isEmpty(str))
            return str;
        StringBuilder result = new StringBuilder();
        for (String word : words(str)) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)));
            }
        }
        return result.toString();
    }

    /**
     * Returns the common prefix of a collection of strings.
     */
    public static String commonPrefix(Collection<String> strings) {
        if (strings == null || strings.isEmpty())
            return "";
        String first = strings.iterator().next();
        if (first == null)
            return "";

        int prefixLen = first.length();
        for (String s : strings) {
            if (s == null)
                return "";
            while (prefixLen > 0 && (s.length() < prefixLen || !s.substring(0, prefixLen).equals(first.substring(0, prefixLen)))) {
                prefixLen--;
            }
        }
        return first.substring(0, prefixLen);
    }

    /**
     * Returns the common suffix of a collection of strings.
     */
    public static String commonSuffix(Collection<String> strings) {
        if (strings == null || strings.isEmpty())
            return "";
        String first = strings.iterator().next();
        if (first == null)
            return "";

        int suffixLen = first.length();
        for (String s : strings) {
            if (s == null)
                return "";
            while (suffixLen > 0 && (s.length() < suffixLen ||
                   !s.substring(s.length() - suffixLen).equals(first.substring(first.length() - suffixLen)))) {
                suffixLen--;
            }
        }
        return first.substring(first.length() - suffixLen);
    }

    /**
     * Abbreviates a string to a maximum length, breaking at word boundaries.
     */
    public static String abbreviate(String str, int maxLength) {
        return abbreviate(str, maxLength, "...");
    }

    /**
     * Abbreviates a string to a maximum length with a custom suffix.
     */
    public static String abbreviate(String str, int maxLength, String suffix) {
        if (str == null || str.length() <= maxLength)
            return str;
        if (maxLength <= suffix.length())
            return suffix.substring(0, maxLength);

        int targetLen = maxLength - suffix.length();
        if (targetLen <= 0)
            return suffix;

        // Try to break at word boundary
        int breakPoint = str.lastIndexOf(' ', targetLen);
        if (breakPoint <= 0) {
            breakPoint = targetLen;
        }
        return str.substring(0, breakPoint).trim() + suffix;
    }
}
