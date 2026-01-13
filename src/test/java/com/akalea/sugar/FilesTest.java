package com.akalea.sugar;

import static com.akalea.sugar.Files.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FilesTest {

    private Path tempDir;
    private Path tempFile;

    @Before
    public void setUp() throws IOException {
        tempDir = java.nio.file.Files.createTempDirectory("sugar-test");
        tempFile = tempDir.resolve("test.txt");
    }

    @After
    public void tearDown() throws IOException {
        // Clean up
        if (tempFile != null && java.nio.file.Files.exists(tempFile)) {
            java.nio.file.Files.delete(tempFile);
        }
        if (tempDir != null && java.nio.file.Files.exists(tempDir)) {
            java.nio.file.Files.walkFileTree(tempDir, new java.nio.file.SimpleFileVisitor<Path>() {
                @Override
                public java.nio.file.FileVisitResult visitFile(Path file, java.nio.file.attribute.BasicFileAttributes attrs) throws IOException {
                    java.nio.file.Files.delete(file);
                    return java.nio.file.FileVisitResult.CONTINUE;
                }
                @Override
                public java.nio.file.FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    java.nio.file.Files.delete(dir);
                    return java.nio.file.FileVisitResult.CONTINUE;
                }
            });
        }
    }

    // ==================== Lines Tests ====================

    @Test
    public void testLinesString() throws IOException {
        java.nio.file.Files.write(tempFile, Arrays.asList("line1", "line2", "line3"));
        List<String> lines = lines(tempFile.toString());
        assertEquals(Arrays.asList("line1", "line2", "line3"), lines);
    }

    @Test
    public void testLinesPath() throws IOException {
        java.nio.file.Files.write(tempFile, Arrays.asList("a", "b"));
        List<String> lines = lines(tempFile);
        assertEquals(Arrays.asList("a", "b"), lines);
    }

    @Test
    public void testLinesWithCharset() throws IOException {
        java.nio.file.Files.write(tempFile, Arrays.asList("hello"), StandardCharsets.UTF_8);
        List<String> lines = lines(tempFile.toString(), StandardCharsets.UTF_8);
        assertEquals(Arrays.asList("hello"), lines);
    }

    @Test
    public void testLinesPathWithCharset() throws IOException {
        java.nio.file.Files.write(tempFile, Arrays.asList("hello"), StandardCharsets.UTF_8);
        List<String> lines = lines(tempFile, StandardCharsets.UTF_8);
        assertEquals(Arrays.asList("hello"), lines);
    }

    @Test(expected = RuntimeException.class)
    public void testLinesNonExistent() {
        lines(tempDir.resolve("nonexistent.txt"));
    }

    // ==================== Read Tests ====================

    @Test
    public void testReadString() throws IOException {
        java.nio.file.Files.writeString(tempFile, "hello world");
        String content = read(tempFile.toString());
        assertEquals("hello world", content);
    }

    @Test
    public void testReadPath() throws IOException {
        java.nio.file.Files.writeString(tempFile, "test content");
        String content = read(tempFile);
        assertEquals("test content", content);
    }

    @Test
    public void testReadPathWithCharset() throws IOException {
        java.nio.file.Files.writeString(tempFile, "utf8 content", StandardCharsets.UTF_8);
        String content = read(tempFile, StandardCharsets.UTF_8);
        assertEquals("utf8 content", content);
    }

    @Test(expected = RuntimeException.class)
    public void testReadNonExistent() {
        read(tempDir.resolve("nonexistent.txt"));
    }

    // ==================== Write Tests ====================

    @Test
    public void testWriteString() throws IOException {
        write(tempFile.toString(), "hello");
        assertEquals("hello", java.nio.file.Files.readString(tempFile));
    }

    @Test
    public void testWritePath() throws IOException {
        write(tempFile, "world");
        assertEquals("world", java.nio.file.Files.readString(tempFile));
    }

    @Test
    public void testWritePathWithCharset() throws IOException {
        write(tempFile, "utf8", StandardCharsets.UTF_8);
        assertEquals("utf8", java.nio.file.Files.readString(tempFile));
    }

    @Test(expected = RuntimeException.class)
    public void testWriteToInvalidPath() {
        write(tempDir.resolve("nonexistent/file.txt"), "content");
    }

    // ==================== WriteLines Tests ====================

    @Test
    public void testWriteLinesString() throws IOException {
        writeLines(tempFile.toString(), Arrays.asList("a", "b", "c"));
        assertEquals(Arrays.asList("a", "b", "c"), java.nio.file.Files.readAllLines(tempFile));
    }

    @Test
    public void testWriteLinesPath() throws IOException {
        writeLines(tempFile, Arrays.asList("1", "2"));
        assertEquals(Arrays.asList("1", "2"), java.nio.file.Files.readAllLines(tempFile));
    }

    @Test
    public void testWriteLinesPathWithCharset() throws IOException {
        writeLines(tempFile, Arrays.asList("x", "y"), StandardCharsets.UTF_8);
        assertEquals(Arrays.asList("x", "y"), java.nio.file.Files.readAllLines(tempFile));
    }

    @Test(expected = RuntimeException.class)
    public void testWriteLinesToInvalidPath() {
        writeLines(tempDir.resolve("nonexistent/file.txt"), Arrays.asList("a"));
    }

    // ==================== Append Tests ====================

    @Test
    public void testAppendString() throws IOException {
        write(tempFile.toString(), "hello");
        append(tempFile.toString(), " world");
        assertEquals("hello world", read(tempFile));
    }

    @Test
    public void testAppendPath() throws IOException {
        write(tempFile, "a");
        append(tempFile, "b");
        assertEquals("ab", read(tempFile));
    }

    @Test
    public void testAppendToNewFile() {
        Path newFile = tempDir.resolve("new.txt");
        append(newFile, "content");
        assertEquals("content", read(newFile));
    }

    @Test(expected = RuntimeException.class)
    public void testAppendToInvalidPath() {
        append(tempDir.resolve("nonexistent/file.txt"), "content");
    }

    // ==================== Using Tests ====================

    @Test
    public void testUsingWithResult() throws IOException {
        write(tempFile, "test");
        String result = using(
            () -> {
                try {
                    return java.nio.file.Files.newBufferedReader(tempFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            },
            reader -> {
                try {
                    return reader.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        );
        assertEquals("test", result);
    }

    @Test
    public void testUsingConsumer() throws IOException {
        write(tempFile, "data");
        StringBuilder sb = new StringBuilder();
        using(
            () -> {
                try {
                    return java.nio.file.Files.newBufferedReader(tempFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            },
            reader -> {
                try {
                    sb.append(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        );
        assertEquals("data", sb.toString());
    }

    @Test(expected = RuntimeException.class)
    public void testUsingThrows() {
        using(
            () -> { throw new RuntimeException("resource error"); },
            resource -> "result"
        );
    }

    // ==================== WithLines Tests ====================

    @Test
    public void testWithLinesString() throws IOException {
        writeLines(tempFile.toString(), Arrays.asList("1", "2", "3"));
        long count = withLines(tempFile.toString(), stream -> stream.count());
        assertEquals(3L, count);
    }

    @Test
    public void testWithLinesPath() throws IOException {
        writeLines(tempFile, Arrays.asList("a", "b"));
        String first = withLines(tempFile, stream -> stream.findFirst().orElse(""));
        assertEquals("a", first);
    }

    @Test(expected = RuntimeException.class)
    public void testWithLinesNonExistent() {
        withLines(tempDir.resolve("nonexistent.txt"), stream -> stream.count());
    }

    // ==================== Exists Tests ====================

    @Test
    public void testExistsString() throws IOException {
        assertFalse(exists(tempFile.toString()));
        write(tempFile, "");
        assertTrue(exists(tempFile.toString()));
    }

    @Test
    public void testExistsPath() throws IOException {
        assertFalse(exists(tempFile));
        write(tempFile, "");
        assertTrue(exists(tempFile));
    }

    // ==================== IsDirectory Tests ====================

    @Test
    public void testIsDirectoryString() {
        assertTrue(isDirectory(tempDir.toString()));
    }

    @Test
    public void testIsDirectoryPath() {
        assertTrue(isDirectory(tempDir));
    }

    @Test
    public void testIsDirectoryFalse() throws IOException {
        write(tempFile, "");
        assertFalse(isDirectory(tempFile));
    }

    // ==================== IsFile Tests ====================

    @Test
    public void testIsFileString() throws IOException {
        write(tempFile, "");
        assertTrue(isFile(tempFile.toString()));
    }

    @Test
    public void testIsFilePath() throws IOException {
        write(tempFile, "");
        assertTrue(isFile(tempFile));
    }

    @Test
    public void testIsFileFalse() {
        assertFalse(isFile(tempDir));
    }

    // ==================== ListFiles Tests ====================

    @Test
    public void testListFilesString() throws IOException {
        write(tempDir.resolve("a.txt"), "");
        write(tempDir.resolve("b.txt"), "");
        List<Path> files = listFiles(tempDir.toString());
        assertEquals(2, files.size());
    }

    @Test
    public void testListFilesPath() throws IOException {
        write(tempDir.resolve("x.txt"), "");
        List<Path> files = listFiles(tempDir);
        assertEquals(1, files.size());
    }

    @Test(expected = RuntimeException.class)
    public void testListFilesNonExistent() {
        listFiles(tempDir.resolve("nonexistent"));
    }

    // ==================== Mkdirs Tests ====================

    @Test
    public void testMkdirsString() {
        Path nested = tempDir.resolve("a/b/c");
        mkdirs(nested.toString());
        assertTrue(java.nio.file.Files.isDirectory(nested));
    }

    @Test
    public void testMkdirsPath() {
        Path nested = tempDir.resolve("x/y");
        mkdirs(nested);
        assertTrue(java.nio.file.Files.isDirectory(nested));
    }

    @Test(expected = RuntimeException.class)
    public void testMkdirsFailure() throws IOException {
        // Create a file where we want a directory
        write(tempFile, "");
        mkdirs(tempFile.resolve("subdir"));
    }

    // ==================== Delete Tests ====================

    @Test
    public void testDeleteString() throws IOException {
        write(tempFile, "");
        assertTrue(java.nio.file.Files.exists(tempFile));
        delete(tempFile.toString());
        assertFalse(java.nio.file.Files.exists(tempFile));
    }

    @Test
    public void testDeletePath() throws IOException {
        write(tempFile, "");
        delete(tempFile);
        assertFalse(java.nio.file.Files.exists(tempFile));
    }

    @Test
    public void testDeleteNonExistent() {
        // Should not throw
        delete(tempDir.resolve("nonexistent.txt"));
    }

    @Test(expected = RuntimeException.class)
    public void testDeleteNonEmptyDirectory() throws IOException {
        Path subdir = tempDir.resolve("subdir");
        mkdirs(subdir);
        write(subdir.resolve("file.txt"), "");
        delete(subdir); // Should fail - directory not empty
    }
}
