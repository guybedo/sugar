package com.akalea.sugar;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * File and resource handling utilities.
 */
public interface Files {

    /**
     * Reads all lines from a file.
     */
    public static List<String> lines(String path) {
        return lines(Paths.get(path), StandardCharsets.UTF_8);
    }

    /**
     * Reads all lines from a file.
     */
    public static List<String> lines(Path path) {
        return lines(path, StandardCharsets.UTF_8);
    }

    /**
     * Reads all lines from a file with specified charset.
     */
    public static List<String> lines(String path, Charset charset) {
        return lines(Paths.get(path), charset);
    }

    /**
     * Reads all lines from a file with specified charset.
     */
    public static List<String> lines(Path path, Charset charset) {
        try {
            return java.nio.file.Files.readAllLines(path, charset);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + path, e);
        }
    }

    /**
     * Reads all content from a file as a single string.
     */
    public static String read(String path) {
        return read(Paths.get(path), StandardCharsets.UTF_8);
    }

    /**
     * Reads all content from a file as a single string.
     */
    public static String read(Path path) {
        return read(path, StandardCharsets.UTF_8);
    }

    /**
     * Reads all content from a file as a single string with specified charset.
     */
    public static String read(Path path, Charset charset) {
        try {
            return java.nio.file.Files.readString(path, charset);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + path, e);
        }
    }

    /**
     * Writes content to a file.
     */
    public static void write(String path, String content) {
        write(Paths.get(path), content, StandardCharsets.UTF_8);
    }

    /**
     * Writes content to a file.
     */
    public static void write(Path path, String content) {
        write(path, content, StandardCharsets.UTF_8);
    }

    /**
     * Writes content to a file with specified charset.
     */
    public static void write(Path path, String content, Charset charset) {
        try {
            java.nio.file.Files.writeString(path, content, charset);
        } catch (IOException e) {
            throw new RuntimeException("Error writing file: " + path, e);
        }
    }

    /**
     * Writes lines to a file.
     */
    public static void writeLines(String path, List<String> lines) {
        writeLines(Paths.get(path), lines, StandardCharsets.UTF_8);
    }

    /**
     * Writes lines to a file.
     */
    public static void writeLines(Path path, List<String> lines) {
        writeLines(path, lines, StandardCharsets.UTF_8);
    }

    /**
     * Writes lines to a file with specified charset.
     */
    public static void writeLines(Path path, List<String> lines, Charset charset) {
        try {
            java.nio.file.Files.write(path, lines, charset);
        } catch (IOException e) {
            throw new RuntimeException("Error writing file: " + path, e);
        }
    }

    /**
     * Appends content to a file.
     */
    public static void append(String path, String content) {
        append(Paths.get(path), content);
    }

    /**
     * Appends content to a file.
     */
    public static void append(Path path, String content) {
        try {
            java.nio.file.Files.writeString(
                path,
                content,
                java.nio.file.StandardOpenOption.CREATE,
                java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Error appending to file: " + path, e);
        }
    }

    /**
     * Executes a function with an AutoCloseable resource, ensuring proper cleanup.
     * Similar to try-with-resources but functional style.
     */
    public static <R extends AutoCloseable, T> T using(
            Supplier<R> resourceSupplier,
            Function<R, T> function) {
        try (R resource = resourceSupplier.get()) {
            return function.apply(resource);
        } catch (Exception e) {
            throw new RuntimeException("Error using resource", e);
        }
    }

    /**
     * Executes a consumer with an AutoCloseable resource, ensuring proper cleanup.
     */
    public static <R extends AutoCloseable> void using(
            Supplier<R> resourceSupplier,
            java.util.function.Consumer<R> consumer) {
        try (R resource = resourceSupplier.get()) {
            consumer.accept(resource);
        } catch (Exception e) {
            throw new RuntimeException("Error using resource", e);
        }
    }

    /**
     * Reads lines lazily using a BufferedReader and processes them.
     */
    public static <T> T withLines(String path, Function<java.util.stream.Stream<String>, T> processor) {
        return withLines(Paths.get(path), processor);
    }

    /**
     * Reads lines lazily using a BufferedReader and processes them.
     */
    public static <T> T withLines(Path path, Function<java.util.stream.Stream<String>, T> processor) {
        try (BufferedReader reader = java.nio.file.Files.newBufferedReader(path)) {
            return processor.apply(reader.lines());
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + path, e);
        }
    }

    /**
     * Checks if a file exists.
     */
    public static boolean exists(String path) {
        return java.nio.file.Files.exists(Paths.get(path));
    }

    /**
     * Checks if a file exists.
     */
    public static boolean exists(Path path) {
        return java.nio.file.Files.exists(path);
    }

    /**
     * Checks if a path is a directory.
     */
    public static boolean isDirectory(String path) {
        return java.nio.file.Files.isDirectory(Paths.get(path));
    }

    /**
     * Checks if a path is a directory.
     */
    public static boolean isDirectory(Path path) {
        return java.nio.file.Files.isDirectory(path);
    }

    /**
     * Checks if a path is a regular file.
     */
    public static boolean isFile(String path) {
        return java.nio.file.Files.isRegularFile(Paths.get(path));
    }

    /**
     * Checks if a path is a regular file.
     */
    public static boolean isFile(Path path) {
        return java.nio.file.Files.isRegularFile(path);
    }

    /**
     * Lists files in a directory.
     */
    public static List<Path> listFiles(String path) {
        return listFiles(Paths.get(path));
    }

    /**
     * Lists files in a directory.
     */
    public static List<Path> listFiles(Path path) {
        try (java.util.stream.Stream<Path> stream = java.nio.file.Files.list(path)) {
            List<Path> result = new ArrayList<>();
            stream.forEach(result::add);
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Error listing directory: " + path, e);
        }
    }

    /**
     * Creates a directory including any necessary parent directories.
     */
    public static Path mkdirs(String path) {
        return mkdirs(Paths.get(path));
    }

    /**
     * Creates a directory including any necessary parent directories.
     */
    public static Path mkdirs(Path path) {
        try {
            return java.nio.file.Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException("Error creating directory: " + path, e);
        }
    }

    /**
     * Deletes a file or empty directory.
     */
    public static void delete(String path) {
        delete(Paths.get(path));
    }

    /**
     * Deletes a file or empty directory.
     */
    public static void delete(Path path) {
        try {
            java.nio.file.Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting: " + path, e);
        }
    }
}
