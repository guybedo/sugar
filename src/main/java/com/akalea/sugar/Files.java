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

    // ==================== New File Operations ====================

    /**
     * Copies a file to a destination.
     */
    public static Path copy(String source, String dest) {
        return copy(Paths.get(source), Paths.get(dest));
    }

    /**
     * Copies a file to a destination.
     */
    public static Path copy(Path source, Path dest) {
        try {
            return java.nio.file.Files.copy(source, dest, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error copying file: " + source + " to " + dest, e);
        }
    }

    /**
     * Copies a file with optional overwrite control.
     */
    public static Path copy(Path source, Path dest, boolean overwrite) {
        try {
            if (overwrite) {
                return java.nio.file.Files.copy(source, dest, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            } else {
                return java.nio.file.Files.copy(source, dest);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error copying file: " + source + " to " + dest, e);
        }
    }

    /**
     * Moves a file to a destination.
     */
    public static Path move(String source, String dest) {
        return move(Paths.get(source), Paths.get(dest));
    }

    /**
     * Moves a file to a destination.
     */
    public static Path move(Path source, Path dest) {
        try {
            return java.nio.file.Files.move(source, dest, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error moving file: " + source + " to " + dest, e);
        }
    }

    /**
     * Recursively deletes a file or directory.
     */
    public static void deleteRecursively(String path) {
        deleteRecursively(Paths.get(path));
    }

    /**
     * Recursively deletes a file or directory.
     */
    public static void deleteRecursively(Path path) {
        try {
            if (java.nio.file.Files.isDirectory(path)) {
                try (java.util.stream.Stream<Path> walk = java.nio.file.Files.walk(path)) {
                    walk.sorted(java.util.Comparator.reverseOrder())
                        .forEach(p -> {
                            try {
                                java.nio.file.Files.delete(p);
                            } catch (IOException e) {
                                throw new RuntimeException("Error deleting: " + p, e);
                            }
                        });
                }
            } else {
                java.nio.file.Files.deleteIfExists(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deleting recursively: " + path, e);
        }
    }

    /**
     * Gets the file extension.
     */
    public static String extension(String path) {
        return extension(Paths.get(path));
    }

    /**
     * Gets the file extension.
     */
    public static String extension(Path path) {
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(dotIndex + 1) : "";
    }

    /**
     * Gets the base name (filename without extension).
     */
    public static String baseName(String path) {
        return baseName(Paths.get(path));
    }

    /**
     * Gets the base name (filename without extension).
     */
    public static String baseName(Path path) {
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
    }

    /**
     * Gets the file size in bytes.
     */
    public static long size(String path) {
        return size(Paths.get(path));
    }

    /**
     * Gets the file size in bytes.
     */
    public static long size(Path path) {
        try {
            return java.nio.file.Files.size(path);
        } catch (IOException e) {
            throw new RuntimeException("Error getting file size: " + path, e);
        }
    }

    /**
     * Creates a file or updates its modification time (like Unix touch).
     */
    public static Path touch(String path) {
        return touch(Paths.get(path));
    }

    /**
     * Creates a file or updates its modification time (like Unix touch).
     */
    public static Path touch(Path path) {
        try {
            if (java.nio.file.Files.exists(path)) {
                java.nio.file.Files.setLastModifiedTime(path,
                    java.nio.file.attribute.FileTime.fromMillis(System.currentTimeMillis()));
            } else {
                java.nio.file.Files.createFile(path);
            }
            return path;
        } catch (IOException e) {
            throw new RuntimeException("Error touching file: " + path, e);
        }
    }

    /**
     * Lists files recursively in a directory.
     */
    public static List<Path> listFilesRecursively(String path) {
        return listFilesRecursively(Paths.get(path));
    }

    /**
     * Lists files recursively in a directory.
     */
    public static List<Path> listFilesRecursively(Path path) {
        try (java.util.stream.Stream<Path> walk = java.nio.file.Files.walk(path)) {
            List<Path> result = new ArrayList<>();
            walk.filter(java.nio.file.Files::isRegularFile).forEach(result::add);
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Error listing files recursively: " + path, e);
        }
    }

    /**
     * Reads bytes from a file.
     */
    public static byte[] readBytes(String path) {
        return readBytes(Paths.get(path));
    }

    /**
     * Reads bytes from a file.
     */
    public static byte[] readBytes(Path path) {
        try {
            return java.nio.file.Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException("Error reading bytes from file: " + path, e);
        }
    }

    /**
     * Writes bytes to a file.
     */
    public static void writeBytes(String path, byte[] bytes) {
        writeBytes(Paths.get(path), bytes);
    }

    /**
     * Writes bytes to a file.
     */
    public static void writeBytes(Path path, byte[] bytes) {
        try {
            java.nio.file.Files.write(path, bytes);
        } catch (IOException e) {
            throw new RuntimeException("Error writing bytes to file: " + path, e);
        }
    }

    /**
     * Gets the parent directory path.
     */
    public static Path parent(String path) {
        return Paths.get(path).getParent();
    }

    /**
     * Gets the parent directory path.
     */
    public static Path parent(Path path) {
        return path.getParent();
    }

    /**
     * Gets the file name.
     */
    public static String fileName(String path) {
        return Paths.get(path).getFileName().toString();
    }

    /**
     * Gets the file name.
     */
    public static String fileName(Path path) {
        return path.getFileName().toString();
    }

    /**
     * Checks if a file is readable.
     */
    public static boolean isReadable(String path) {
        return java.nio.file.Files.isReadable(Paths.get(path));
    }

    /**
     * Checks if a file is readable.
     */
    public static boolean isReadable(Path path) {
        return java.nio.file.Files.isReadable(path);
    }

    /**
     * Checks if a file is writable.
     */
    public static boolean isWritable(String path) {
        return java.nio.file.Files.isWritable(Paths.get(path));
    }

    /**
     * Checks if a file is writable.
     */
    public static boolean isWritable(Path path) {
        return java.nio.file.Files.isWritable(path);
    }

    /**
     * Checks if a path is empty (file is 0 bytes or directory is empty).
     */
    public static boolean isEmpty(String path) {
        return isEmpty(Paths.get(path));
    }

    /**
     * Checks if a path is empty (file is 0 bytes or directory is empty).
     */
    public static boolean isEmpty(Path path) {
        try {
            if (java.nio.file.Files.isDirectory(path)) {
                try (java.util.stream.Stream<Path> entries = java.nio.file.Files.list(path)) {
                    return entries.findFirst().isEmpty();
                }
            } else {
                return java.nio.file.Files.size(path) == 0;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error checking if path is empty: " + path, e);
        }
    }
}
