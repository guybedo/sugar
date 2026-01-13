package com.akalea.sugar;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Test;

import com.akalea.sugar.internal.Either;
import com.akalea.sugar.internal.Try;

public class TryTest {

    @Test
    public void testOfSuccess() {
        Try<Integer> t = Try.of(() -> 42);
        assertTrue(t.isSuccess());
        assertFalse(t.isFailure());
        assertEquals((Integer) 42, t.get());
    }

    @Test
    public void testOfFailure() {
        Try<Integer> t = Try.of(() -> {
            throw new RuntimeException("error");
        });
        assertTrue(t.isFailure());
        assertFalse(t.isSuccess());
        assertNotNull(t.getException());
        assertEquals("error", t.getException().getMessage());
    }

    @Test
    public void testSuccess() {
        Try<String> t = Try.success("hello");
        assertTrue(t.isSuccess());
        assertEquals("hello", t.get());
    }

    @Test
    public void testFailure() {
        Exception ex = new RuntimeException("test error");
        Try<String> t = Try.failure(ex);
        assertTrue(t.isFailure());
        assertEquals(ex, t.getException());
    }

    @Test(expected = NoSuchElementException.class)
    public void testSuccessGetException() {
        Try<Integer> t = Try.success(42);
        t.getException();
    }

    @Test(expected = RuntimeException.class)
    public void testFailureGet() {
        Try<Integer> t = Try.failure(new RuntimeException("error"));
        t.get();
    }

    @Test
    public void testGetOrElseSuccess() {
        Try<Integer> t = Try.success(42);
        assertEquals((Integer) 42, t.getOrElse(0));
        assertEquals((Integer) 42, t.getOrElse(() -> 0));
    }

    @Test
    public void testGetOrElseFailure() {
        Try<Integer> t = Try.failure(new RuntimeException("error"));
        assertEquals((Integer) 0, t.getOrElse(0));
        assertEquals((Integer) 99, t.getOrElse(() -> 99));
    }

    @Test
    public void testGetOrNull() {
        assertEquals((Integer) 42, Try.success(42).getOrNull());
        assertNull(Try.failure(new RuntimeException()).getOrNull());
    }

    @Test
    public void testMapSuccess() {
        Try<Integer> t = Try.success(21);
        Try<Integer> mapped = t.map(x -> x * 2);
        assertTrue(mapped.isSuccess());
        assertEquals((Integer) 42, mapped.get());
    }

    @Test
    public void testMapSuccessThrows() {
        Try<Integer> t = Try.success(21);
        Try<Integer> mapped = t.map(x -> {
            throw new RuntimeException("map error");
        });
        assertTrue(mapped.isFailure());
    }

    @Test
    public void testMapFailure() {
        Try<Integer> t = Try.failure(new RuntimeException("original"));
        Try<Integer> mapped = t.map(x -> x * 2);
        assertTrue(mapped.isFailure());
        assertEquals("original", mapped.getException().getMessage());
    }

    @Test
    public void testFlatMapSuccess() {
        Try<Integer> t = Try.success(21);
        Try<Integer> flatMapped = t.flatMap(x -> Try.success(x * 2));
        assertTrue(flatMapped.isSuccess());
        assertEquals((Integer) 42, flatMapped.get());
    }

    @Test
    public void testFlatMapSuccessReturnsFailure() {
        Try<Integer> t = Try.success(21);
        Try<Integer> flatMapped = t.flatMap(x -> Try.failure(new RuntimeException("inner")));
        assertTrue(flatMapped.isFailure());
    }

    @Test
    public void testFlatMapSuccessThrows() {
        Try<Integer> t = Try.success(21);
        Try<Integer> flatMapped = t.flatMap(x -> {
            throw new RuntimeException("flatMap error");
        });
        assertTrue(flatMapped.isFailure());
    }

    @Test
    public void testFlatMapFailure() {
        Try<Integer> t = Try.failure(new RuntimeException("original"));
        Try<Integer> flatMapped = t.flatMap(x -> Try.success(x * 2));
        assertTrue(flatMapped.isFailure());
    }

    @Test
    public void testFilterSuccess() {
        Try<Integer> t = Try.success(42);
        Try<Integer> filtered = t.filter(x -> x > 10);
        assertTrue(filtered.isSuccess());
        assertEquals((Integer) 42, filtered.get());
    }

    @Test
    public void testFilterSuccessNoMatch() {
        Try<Integer> t = Try.success(5);
        Try<Integer> filtered = t.filter(x -> x > 10);
        assertTrue(filtered.isFailure());
    }

    @Test
    public void testFilterSuccessThrows() {
        Try<Integer> t = Try.success(42);
        Try<Integer> filtered = t.filter(x -> {
            throw new RuntimeException("filter error");
        });
        assertTrue(filtered.isFailure());
    }

    @Test
    public void testFilterFailure() {
        Try<Integer> t = Try.failure(new RuntimeException("original"));
        Try<Integer> filtered = t.filter(x -> x > 10);
        assertTrue(filtered.isFailure());
    }

    @Test
    public void testRecoverSuccess() {
        Try<Integer> t = Try.success(42);
        Try<Integer> recovered = t.recover(e -> 0);
        assertTrue(recovered.isSuccess());
        assertEquals((Integer) 42, recovered.get());
    }

    @Test
    public void testRecoverFailure() {
        Try<Integer> t = Try.failure(new RuntimeException("error"));
        Try<Integer> recovered = t.recover(e -> 99);
        assertTrue(recovered.isSuccess());
        assertEquals((Integer) 99, recovered.get());
    }

    @Test
    public void testRecoverFailureThrows() {
        Try<Integer> t = Try.failure(new RuntimeException("error"));
        Try<Integer> recovered = t.recover(e -> {
            throw new RuntimeException("recover error");
        });
        assertTrue(recovered.isFailure());
    }

    @Test
    public void testRecoverWithSuccess() {
        Try<Integer> t = Try.success(42);
        Try<Integer> recovered = t.recoverWith(e -> Try.success(0));
        assertTrue(recovered.isSuccess());
        assertEquals((Integer) 42, recovered.get());
    }

    @Test
    public void testRecoverWithFailure() {
        Try<Integer> t = Try.failure(new RuntimeException("error"));
        Try<Integer> recovered = t.recoverWith(e -> Try.success(99));
        assertTrue(recovered.isSuccess());
        assertEquals((Integer) 99, recovered.get());
    }

    @Test
    public void testRecoverWithFailureThrows() {
        Try<Integer> t = Try.failure(new RuntimeException("error"));
        Try<Integer> recovered = t.recoverWith(e -> {
            throw new RuntimeException("recoverWith error");
        });
        assertTrue(recovered.isFailure());
    }

    @Test
    public void testOnSuccessSuccess() {
        StringBuilder sb = new StringBuilder();
        Try<Integer> t = Try.success(42);
        Try<Integer> result = t.onSuccess(x -> sb.append(x));
        assertEquals("42", sb.toString());
        assertSame(t, result);
    }

    @Test
    public void testOnSuccessFailure() {
        StringBuilder sb = new StringBuilder();
        Try<Integer> t = Try.failure(new RuntimeException("error"));
        Try<Integer> result = t.onSuccess(x -> sb.append(x));
        assertEquals("", sb.toString());
        assertSame(t, result);
    }

    @Test
    public void testOnFailureSuccess() {
        StringBuilder sb = new StringBuilder();
        Try<Integer> t = Try.success(42);
        Try<Integer> result = t.onFailure(e -> sb.append(e.getMessage()));
        assertEquals("", sb.toString());
        assertSame(t, result);
    }

    @Test
    public void testOnFailureFailure() {
        StringBuilder sb = new StringBuilder();
        Try<Integer> t = Try.failure(new RuntimeException("error"));
        Try<Integer> result = t.onFailure(e -> sb.append(e.getMessage()));
        assertEquals("error", sb.toString());
        assertSame(t, result);
    }

    @Test
    public void testToOptionalSuccess() {
        Try<Integer> t = Try.success(42);
        Optional<Integer> opt = t.toOptional();
        assertTrue(opt.isPresent());
        assertEquals((Integer) 42, opt.get());
    }

    @Test
    public void testToOptionalSuccessNull() {
        Try<Integer> t = Try.success(null);
        Optional<Integer> opt = t.toOptional();
        assertFalse(opt.isPresent());
    }

    @Test
    public void testToOptionalFailure() {
        Try<Integer> t = Try.failure(new RuntimeException("error"));
        Optional<Integer> opt = t.toOptional();
        assertFalse(opt.isPresent());
    }

    @Test
    public void testToEitherSuccess() {
        Try<Integer> t = Try.success(42);
        Either<Exception, Integer> either = t.toEither();
        assertTrue(either.isRight());
        assertEquals((Integer) 42, either.getRight());
    }

    @Test
    public void testToEitherFailure() {
        RuntimeException ex = new RuntimeException("error");
        Try<Integer> t = Try.failure(ex);
        Either<Exception, Integer> either = t.toEither();
        assertTrue(either.isLeft());
        assertEquals(ex, either.getLeft());
    }
}
