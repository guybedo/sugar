package com.akalea.sugar;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Test;

import com.akalea.sugar.internal.Either;

public class EitherTest {

    @Test
    public void testLeft() {
        Either<String, Integer> e = Either.left("error");
        assertTrue(e.isLeft());
        assertFalse(e.isRight());
        assertEquals("error", e.getLeft());
    }

    @Test
    public void testRight() {
        Either<String, Integer> e = Either.right(42);
        assertTrue(e.isRight());
        assertFalse(e.isLeft());
        assertEquals((Integer) 42, e.getRight());
    }

    @Test(expected = NoSuchElementException.class)
    public void testLeftGetRight() {
        Either<String, Integer> e = Either.left("error");
        e.getRight();
    }

    @Test(expected = NoSuchElementException.class)
    public void testRightGetLeft() {
        Either<String, Integer> e = Either.right(42);
        e.getLeft();
    }

    @Test
    public void testGetOrElseValueRight() {
        Either<String, Integer> e = Either.right(42);
        assertEquals((Integer) 42, e.getOrElse(0));
    }

    @Test
    public void testGetOrElseValueLeft() {
        Either<String, Integer> e = Either.left("error");
        assertEquals((Integer) 0, e.getOrElse(0));
    }

    @Test
    public void testGetOrElseFunctionRight() {
        Either<String, Integer> e = Either.right(42);
        assertEquals((Integer) 42, e.getOrElse(s -> s.length()));
    }

    @Test
    public void testGetOrElseFunctionLeft() {
        Either<String, Integer> e = Either.left("error");
        assertEquals((Integer) 5, e.getOrElse(s -> s.length()));
    }

    @Test
    public void testMapRight() {
        Either<String, Integer> e = Either.right(21);
        Either<String, Integer> mapped = e.map(x -> x * 2);
        assertTrue(mapped.isRight());
        assertEquals((Integer) 42, mapped.getRight());
    }

    @Test
    public void testMapLeft() {
        Either<String, Integer> e = Either.left("error");
        Either<String, Integer> mapped = e.map(x -> x * 2);
        assertTrue(mapped.isLeft());
        assertEquals("error", mapped.getLeft());
    }

    @Test
    public void testMapLeftRight() {
        Either<String, Integer> e = Either.right(42);
        Either<Integer, Integer> mapped = e.mapLeft(s -> s.length());
        assertTrue(mapped.isRight());
        assertEquals((Integer) 42, mapped.getRight());
    }

    @Test
    public void testMapLeftLeft() {
        Either<String, Integer> e = Either.left("error");
        Either<Integer, Integer> mapped = e.mapLeft(s -> s.length());
        assertTrue(mapped.isLeft());
        assertEquals((Integer) 5, mapped.getLeft());
    }

    @Test
    public void testFlatMapRight() {
        Either<String, Integer> e = Either.right(21);
        Either<String, Integer> flatMapped = e.flatMap(x -> Either.right(x * 2));
        assertTrue(flatMapped.isRight());
        assertEquals((Integer) 42, flatMapped.getRight());
    }

    @Test
    public void testFlatMapRightReturnsLeft() {
        Either<String, Integer> e = Either.right(21);
        Either<String, Integer> flatMapped = e.flatMap(x -> Either.left("new error"));
        assertTrue(flatMapped.isLeft());
        assertEquals("new error", flatMapped.getLeft());
    }

    @Test
    public void testFlatMapLeft() {
        Either<String, Integer> e = Either.left("error");
        Either<String, Integer> flatMapped = e.flatMap(x -> Either.right(x * 2));
        assertTrue(flatMapped.isLeft());
        assertEquals("error", flatMapped.getLeft());
    }

    @Test
    public void testFoldRight() {
        Either<String, Integer> e = Either.right(42);
        String result = e.fold(l -> "left: " + l, r -> "right: " + r);
        assertEquals("right: 42", result);
    }

    @Test
    public void testFoldLeft() {
        Either<String, Integer> e = Either.left("error");
        String result = e.fold(l -> "left: " + l, r -> "right: " + r);
        assertEquals("left: error", result);
    }

    @Test
    public void testSwapRight() {
        Either<String, Integer> e = Either.right(42);
        Either<Integer, String> swapped = e.swap();
        assertTrue(swapped.isLeft());
        assertEquals((Integer) 42, swapped.getLeft());
    }

    @Test
    public void testSwapLeft() {
        Either<String, Integer> e = Either.left("error");
        Either<Integer, String> swapped = e.swap();
        assertTrue(swapped.isRight());
        assertEquals("error", swapped.getRight());
    }

    @Test
    public void testOnRightRight() {
        StringBuilder sb = new StringBuilder();
        Either<String, Integer> e = Either.right(42);
        Either<String, Integer> result = e.onRight(x -> sb.append(x));
        assertEquals("42", sb.toString());
        assertSame(e, result);
    }

    @Test
    public void testOnRightLeft() {
        StringBuilder sb = new StringBuilder();
        Either<String, Integer> e = Either.left("error");
        Either<String, Integer> result = e.onRight(x -> sb.append(x));
        assertEquals("", sb.toString());
        assertSame(e, result);
    }

    @Test
    public void testOnLeftRight() {
        StringBuilder sb = new StringBuilder();
        Either<String, Integer> e = Either.right(42);
        Either<String, Integer> result = e.onLeft(x -> sb.append(x));
        assertEquals("", sb.toString());
        assertSame(e, result);
    }

    @Test
    public void testOnLeftLeft() {
        StringBuilder sb = new StringBuilder();
        Either<String, Integer> e = Either.left("error");
        Either<String, Integer> result = e.onLeft(x -> sb.append(x));
        assertEquals("error", sb.toString());
        assertSame(e, result);
    }

    @Test
    public void testToOptionalRight() {
        Either<String, Integer> e = Either.right(42);
        Optional<Integer> opt = e.toOptional();
        assertTrue(opt.isPresent());
        assertEquals((Integer) 42, opt.get());
    }

    @Test
    public void testToOptionalRightNull() {
        Either<String, Integer> e = Either.right(null);
        Optional<Integer> opt = e.toOptional();
        assertFalse(opt.isPresent());
    }

    @Test
    public void testToOptionalLeft() {
        Either<String, Integer> e = Either.left("error");
        Optional<Integer> opt = e.toOptional();
        assertFalse(opt.isPresent());
    }
}
