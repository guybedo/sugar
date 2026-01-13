package com.akalea.sugar;

import static com.akalea.sugar.Functions.*;
import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

public class FunctionsTest {

    @Test
    public void testCompose2() {
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        Function<Integer, Integer> composed = compose(f, g);
        assertEquals((Integer) 6, composed.apply(2)); // (2 + 1) * 2 = 6
    }

    @Test
    public void testPipe() {
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        Function<Integer, Integer> piped = pipe(f, g);
        assertEquals((Integer) 6, piped.apply(2)); // same as compose
    }

    @Test
    public void testCompose3() {
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        Function<Integer, Integer> h = x -> x - 3;
        Function<Integer, Integer> composed = compose(f, g, h);
        assertEquals((Integer) 3, composed.apply(2)); // ((2 + 1) * 2) - 3 = 3
    }

    @Test
    public void testCompose4() {
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        Function<Integer, Integer> h = x -> x - 3;
        Function<Integer, Integer> i = x -> x * 10;
        Function<Integer, Integer> composed = compose(f, g, h, i);
        assertEquals((Integer) 30, composed.apply(2)); // (((2 + 1) * 2) - 3) * 10 = 30
    }

    @Test
    public void testPartial() {
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        Function<Integer, Integer> add5 = partial(add, 5);
        assertEquals((Integer) 8, add5.apply(3));
    }

    @Test
    public void testPartial2() {
        BiFunction<Integer, Integer, Integer> subtract = (a, b) -> a - b;
        Function<Integer, Integer> subtractFrom10 = partial2(subtract, 10);
        assertEquals(Integer.valueOf(-5), subtractFrom10.apply(5)); // 5 - 10 = -5
    }

    @Test
    public void testCurry() {
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        Function<Integer, Function<Integer, Integer>> curried = curry(add);
        assertEquals((Integer) 8, curried.apply(5).apply(3));
    }

    @Test
    public void testUncurry() {
        Function<Integer, Function<Integer, Integer>> curried = a -> b -> a + b;
        BiFunction<Integer, Integer, Integer> uncurried = uncurry(curried);
        assertEquals((Integer) 8, uncurried.apply(5, 3));
    }

    @Test
    public void testFlip() {
        BiFunction<String, Integer, String> repeat = (s, n) -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) sb.append(s);
            return sb.toString();
        };
        BiFunction<Integer, String, String> flipped = flip(repeat);
        assertEquals("aaa", flipped.apply(3, "a"));
    }

    @Test
    public void testMemoizeFunction() {
        AtomicInteger callCount = new AtomicInteger(0);
        Function<Integer, Integer> expensive = x -> {
            callCount.incrementAndGet();
            return x * 2;
        };
        Function<Integer, Integer> memoized = memoize(expensive);

        assertEquals((Integer) 10, memoized.apply(5));
        assertEquals((Integer) 10, memoized.apply(5));
        assertEquals((Integer) 10, memoized.apply(5));
        assertEquals(1, callCount.get()); // Only called once

        assertEquals((Integer) 20, memoized.apply(10));
        assertEquals(2, callCount.get()); // Called again for new input
    }

    @Test
    public void testMemoizeSupplier() {
        AtomicInteger callCount = new AtomicInteger(0);
        Supplier<Integer> expensive = () -> {
            callCount.incrementAndGet();
            return 42;
        };
        Supplier<Integer> memoized = memoize(expensive);

        assertEquals((Integer) 42, memoized.get());
        assertEquals((Integer) 42, memoized.get());
        assertEquals((Integer) 42, memoized.get());
        assertEquals(1, callCount.get()); // Only called once
    }

    @Test
    public void testConstant() {
        Function<String, Integer> always42 = constant(42);
        assertEquals((Integer) 42, always42.apply("anything"));
        assertEquals((Integer) 42, always42.apply("whatever"));
    }

    @Test
    public void testIdentity() {
        Function<String, String> id = identity();
        assertEquals("hello", id.apply("hello"));
    }

    @Test
    public void testAndThen() {
        StringBuilder sb = new StringBuilder();
        Function<String, String> log = s -> {
            sb.append("logged: ").append(s);
            return "logged";
        };
        Function<String, Integer> length = s -> s.length();
        Function<String, Integer> combined = andThen(log, length);

        assertEquals((Integer) 5, combined.apply("hello"));
        assertEquals("logged: hello", sb.toString());
    }

    @Test
    public void testUncheckedFunction() {
        Functions.ThrowingFunction<String, Integer> throwing = s -> {
            if (s.isEmpty()) throw new Exception("empty");
            return s.length();
        };
        Function<String, Integer> safe = unchecked(throwing);
        assertEquals((Integer) 5, safe.apply("hello"));
    }

    @Test(expected = RuntimeException.class)
    public void testUncheckedFunctionThrows() {
        Functions.ThrowingFunction<String, Integer> throwing = s -> {
            throw new Exception("error");
        };
        Function<String, Integer> safe = unchecked(throwing);
        safe.apply("hello");
    }

    @Test
    public void testUncheckedSupplier() {
        Functions.ThrowingSupplier<Integer> throwing = () -> 42;
        Supplier<Integer> safe = unchecked(throwing);
        assertEquals((Integer) 42, safe.get());
    }

    @Test(expected = RuntimeException.class)
    public void testUncheckedSupplierThrows() {
        Functions.ThrowingSupplier<Integer> throwing = () -> {
            throw new Exception("error");
        };
        Supplier<Integer> safe = unchecked(throwing);
        safe.get();
    }

    @Test
    public void testUncheckedRunnable() {
        final int[] counter = {0};
        Functions.ThrowingRunnable throwing = () -> counter[0] = 42;
        Runnable safe = unchecked(throwing);
        safe.run();
        assertEquals(42, counter[0]);
    }

    @Test(expected = RuntimeException.class)
    public void testUncheckedRunnableThrows() {
        Functions.ThrowingRunnable throwing = () -> {
            throw new Exception("error");
        };
        Runnable safe = unchecked(throwing);
        safe.run();
    }
}
