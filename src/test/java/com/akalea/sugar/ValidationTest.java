package com.akalea.sugar;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.akalea.sugar.internal.Either;
import com.akalea.sugar.internal.Validation;

public class ValidationTest {

    @Test
    public void testValid() {
        Validation<String, Integer> v = Validation.valid(42);
        assertTrue(v.isValid());
        assertFalse(v.isInvalid());
        assertEquals((Integer) 42, v.getValue());
        assertTrue(v.getErrors().isEmpty());
    }

    @Test
    public void testInvalidSingle() {
        Validation<String, Integer> v = Validation.invalid("error");
        assertTrue(v.isInvalid());
        assertFalse(v.isValid());
        assertEquals(1, v.getErrors().size());
        assertEquals("error", v.getErrors().get(0));
    }

    @Test
    public void testInvalidList() {
        List<String> errors = Arrays.asList("error1", "error2");
        Validation<String, Integer> v = Validation.invalid(errors);
        assertTrue(v.isInvalid());
        assertEquals(2, v.getErrors().size());
    }

    @Test
    public void testFromPredicateTrue() {
        Validation<String, Integer> v = Validation.fromPredicate(42, x -> x > 0, "must be positive");
        assertTrue(v.isValid());
        assertEquals((Integer) 42, v.getValue());
    }

    @Test
    public void testFromPredicateFalse() {
        Validation<String, Integer> v = Validation.fromPredicate(-1, x -> x > 0, "must be positive");
        assertTrue(v.isInvalid());
        assertEquals("must be positive", v.getErrors().get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidGetValue() {
        Validation<String, Integer> v = Validation.invalid("error");
        v.getValue();
    }

    @Test
    public void testGetOrElseValid() {
        Validation<String, Integer> v = Validation.valid(42);
        assertEquals((Integer) 42, v.getOrElse(0));
    }

    @Test
    public void testGetOrElseInvalid() {
        Validation<String, Integer> v = Validation.invalid("error");
        assertEquals((Integer) 0, v.getOrElse(0));
    }

    @Test
    public void testMapValid() {
        Validation<String, Integer> v = Validation.valid(21);
        Validation<String, Integer> mapped = v.map(x -> x * 2);
        assertTrue(mapped.isValid());
        assertEquals((Integer) 42, mapped.getValue());
    }

    @Test
    public void testMapInvalid() {
        Validation<String, Integer> v = Validation.invalid("error");
        Validation<String, Integer> mapped = v.map(x -> x * 2);
        assertTrue(mapped.isInvalid());
        assertEquals("error", mapped.getErrors().get(0));
    }

    @Test
    public void testMapErrorsValid() {
        Validation<String, Integer> v = Validation.valid(42);
        Validation<Integer, Integer> mapped = v.mapErrors(s -> s.length());
        assertTrue(mapped.isValid());
        assertEquals((Integer) 42, mapped.getValue());
    }

    @Test
    public void testMapErrorsInvalid() {
        Validation<String, Integer> v = Validation.invalid("error");
        Validation<Integer, Integer> mapped = v.mapErrors(s -> s.length());
        assertTrue(mapped.isInvalid());
        assertEquals((Integer) 5, mapped.getErrors().get(0));
    }

    @Test
    public void testToEitherValid() {
        Validation<String, Integer> v = Validation.valid(42);
        Either<List<String>, Integer> either = v.toEither();
        assertTrue(either.isRight());
        assertEquals((Integer) 42, either.getRight());
    }

    @Test
    public void testToEitherInvalid() {
        Validation<String, Integer> v = Validation.invalid("error");
        Either<List<String>, Integer> either = v.toEither();
        assertTrue(either.isLeft());
        assertEquals(1, either.getLeft().size());
        assertEquals("error", either.getLeft().get(0));
    }

    @Test
    public void testCombine2BothValid() {
        Validation<String, Integer> v1 = Validation.valid(10);
        Validation<String, Integer> v2 = Validation.valid(20);
        Validation<String, Integer> combined = Validation.combine(v1, v2, (a, b) -> a + b);
        assertTrue(combined.isValid());
        assertEquals((Integer) 30, combined.getValue());
    }

    @Test
    public void testCombine2OneInvalid() {
        Validation<String, Integer> v1 = Validation.valid(10);
        Validation<String, Integer> v2 = Validation.invalid("error2");
        Validation<String, Integer> combined = Validation.combine(v1, v2, (a, b) -> a + b);
        assertTrue(combined.isInvalid());
        assertEquals(1, combined.getErrors().size());
    }

    @Test
    public void testCombine2BothInvalid() {
        Validation<String, Integer> v1 = Validation.invalid("error1");
        Validation<String, Integer> v2 = Validation.invalid("error2");
        Validation<String, Integer> combined = Validation.combine(v1, v2, (a, b) -> a + b);
        assertTrue(combined.isInvalid());
        assertEquals(2, combined.getErrors().size());
        assertTrue(combined.getErrors().contains("error1"));
        assertTrue(combined.getErrors().contains("error2"));
    }

    @Test
    public void testCombine3AllValid() {
        Validation<String, Integer> v1 = Validation.valid(10);
        Validation<String, Integer> v2 = Validation.valid(20);
        Validation<String, Integer> v3 = Validation.valid(30);
        Validation<String, Integer> combined = Validation.combine(v1, v2, v3, (a, b, c) -> a + b + c);
        assertTrue(combined.isValid());
        assertEquals((Integer) 60, combined.getValue());
    }

    @Test
    public void testCombine3SomeInvalid() {
        Validation<String, Integer> v1 = Validation.invalid("error1");
        Validation<String, Integer> v2 = Validation.valid(20);
        Validation<String, Integer> v3 = Validation.invalid("error3");
        Validation<String, Integer> combined = Validation.combine(v1, v2, v3, (a, b, c) -> a + b + c);
        assertTrue(combined.isInvalid());
        assertEquals(2, combined.getErrors().size());
    }

    @Test
    public void testCombine4AllValid() {
        Validation<String, Integer> v1 = Validation.valid(10);
        Validation<String, Integer> v2 = Validation.valid(20);
        Validation<String, Integer> v3 = Validation.valid(30);
        Validation<String, Integer> v4 = Validation.valid(40);
        Validation<String, Integer> combined = Validation.combine(v1, v2, v3, v4, (a, b, c, d) -> a + b + c + d);
        assertTrue(combined.isValid());
        assertEquals((Integer) 100, combined.getValue());
    }

    @Test
    public void testCombine4SomeInvalid() {
        Validation<String, Integer> v1 = Validation.invalid("error1");
        Validation<String, Integer> v2 = Validation.invalid("error2");
        Validation<String, Integer> v3 = Validation.valid(30);
        Validation<String, Integer> v4 = Validation.invalid("error4");
        Validation<String, Integer> combined = Validation.combine(v1, v2, v3, v4, (a, b, c, d) -> a + b + c + d);
        assertTrue(combined.isInvalid());
        assertEquals(3, combined.getErrors().size());
    }

    @Test
    public void testSequenceAllValid() {
        List<Validation<String, Integer>> validations = Arrays.asList(
            Validation.valid(1),
            Validation.valid(2),
            Validation.valid(3)
        );
        Validation<String, List<Integer>> result = Validation.sequence(validations);
        assertTrue(result.isValid());
        assertEquals(Arrays.asList(1, 2, 3), result.getValue());
    }

    @Test
    public void testSequenceSomeInvalid() {
        List<Validation<String, Integer>> validations = Arrays.asList(
            Validation.valid(1),
            Validation.invalid("error1"),
            Validation.valid(3),
            Validation.invalid("error2")
        );
        Validation<String, List<Integer>> result = Validation.sequence(validations);
        assertTrue(result.isInvalid());
        assertEquals(2, result.getErrors().size());
    }
}
