package com.akalea.sugar;

import static com.akalea.sugar.Collections.*;
import static com.akalea.sugar.Parallel.*;
import static org.junit.Assert.*;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.akalea.sugar.internal.Timed;

public class ParallelTest {

    // ==================== background Tests ====================

    @Test
    public void testBackground() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        Thread t = background(() -> counter.incrementAndGet(), Duration.ofMillis(100));
        Thread.sleep(250);
        t.interrupt();
        assertTrue(counter.get() >= 2);
    }

    // ==================== pMap Tests ====================

    @Test
    public void testPMap() {
        List<Integer> input = list(1, 2, 3, 4, 5);
        List<Integer> result = pMap(input, x -> x * 2);
        assertEquals(list(2, 4, 6, 8, 10), result);
    }

    @Test
    public void testPMapWithThreadCount() {
        List<Integer> input = list(1, 2, 3);
        List<Integer> result = pMap(input, x -> x * 2, 2);
        assertEquals(list(2, 4, 6), result);
    }

    @Test
    public void testPMapWithExecutor() {
        List<Integer> input = list(1, 2, 3);
        java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(2);
        List<Integer> result = pMap(input, x -> x * 2, executor);
        assertEquals(list(2, 4, 6), result);
        executor.shutdown();
    }

    // ==================== pEach Tests ====================

    @Test
    public void testPEach() {
        AtomicInteger sum = new AtomicInteger(0);
        List<Integer> input = list(1, 2, 3, 4, 5);
        pEach(input, x -> sum.addAndGet(x));
        assertEquals(15, sum.get());
    }

    @Test
    public void testPEachWithThreadCount() {
        AtomicInteger sum = new AtomicInteger(0);
        List<Integer> input = list(1, 2, 3);
        pEach(input, x -> sum.addAndGet(x), 2);
        assertEquals(6, sum.get());
    }

    // ==================== compute Tests ====================

    @Test
    public void testCompute() {
        List<java.util.function.Supplier<Integer>> suppliers = list(
            () -> 1,
            () -> 2,
            () -> 3
        );
        List<Integer> results = compute(suppliers, 3);
        assertEquals(list(1, 2, 3), results);
    }

    // ==================== timed Tests ====================

    @Test
    public void testTimedSupplier() {
        Timed<Integer> result = timed(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return 42;
        });
        assertEquals((Integer) 42, result.getValue());
        assertTrue(result.getMillis() >= 45);
    }

    @Test
    public void testTimedRunnable() {
        final int[] counter = {0};
        Runnable r = () -> counter[0] = 42;
        Timed<Void> result = timed(r);
        assertEquals(42, counter[0]);
    }

    // ==================== retry Tests ====================

    @Test
    public void testRetrySuccess() {
        AtomicInteger attempts = new AtomicInteger(0);
        String result = retry(() -> {
            if (attempts.incrementAndGet() < 3) {
                throw new RuntimeException("fail");
            }
            return "success";
        }, 5, Duration.ofMillis(10));
        assertEquals("success", result);
        assertEquals(3, attempts.get());
    }

    @Test(expected = RuntimeException.class)
    public void testRetryAllFail() {
        retry(() -> {
            throw new RuntimeException("always fail");
        }, 3, Duration.ofMillis(10));
    }

    @Test
    public void testRetryRunnable() {
        AtomicInteger attempts = new AtomicInteger(0);
        retry(() -> {
            if (attempts.incrementAndGet() < 2) {
                throw new RuntimeException("fail");
            }
        }, 3, Duration.ofMillis(10));
        assertEquals(2, attempts.get());
    }

    // ==================== retryWithBackoff Tests ====================

    @Test
    public void testRetryWithBackoffSuccess() {
        AtomicInteger attempts = new AtomicInteger(0);
        String result = retryWithBackoff(() -> {
            if (attempts.incrementAndGet() < 2) {
                throw new RuntimeException("fail");
            }
            return "success";
        }, 5, Duration.ofMillis(10), 2.0);
        assertEquals("success", result);
    }

    @Test(expected = RuntimeException.class)
    public void testRetryWithBackoffAllFail() {
        retryWithBackoff(() -> {
            throw new RuntimeException("always fail");
        }, 2, Duration.ofMillis(10), 2.0);
    }

    // ==================== timeout Tests ====================

    @Test
    public void testTimeoutSuccess() {
        Optional<Integer> result = timeout(() -> 42, Duration.ofSeconds(1));
        assertTrue(result.isPresent());
        assertEquals((Integer) 42, result.get());
    }

    @Test
    public void testTimeoutExceeded() {
        Optional<Integer> result = timeout(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return 42;
        }, Duration.ofMillis(50));
        assertFalse(result.isPresent());
    }

    @Test
    public void testTimeoutWithDefault() {
        Integer result = timeout(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return 42;
        }, Duration.ofMillis(50), 0);
        assertEquals((Integer) 0, result);
    }

    @Test
    public void testTimeoutRunnableSuccess() {
        boolean completed = timeout(() -> {
            // fast operation
        }, Duration.ofSeconds(1));
        assertTrue(completed);
    }

    @Test
    public void testTimeoutRunnableExceeded() {
        boolean completed = timeout(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, Duration.ofMillis(50));
        assertFalse(completed);
    }

    // ==================== delay Tests ====================

    @Test
    public void testDelayDuration() {
        long start = System.currentTimeMillis();
        delay(Duration.ofMillis(100));
        long elapsed = System.currentTimeMillis() - start;
        assertTrue(elapsed >= 90);
    }

    @Test
    public void testDelayMillis() {
        long start = System.currentTimeMillis();
        delay(100);
        long elapsed = System.currentTimeMillis() - start;
        assertTrue(elapsed >= 90);
    }

    // ==================== schedule Tests ====================

    @Test
    public void testSchedule() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        java.util.concurrent.ScheduledFuture<?> future = schedule(
            () -> counter.incrementAndGet(),
            Duration.ofMillis(0),
            Duration.ofMillis(50)
        );
        Thread.sleep(130);
        future.cancel(true);
        assertTrue(counter.get() >= 2);
    }

    @Test
    public void testScheduleOnce() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        java.util.concurrent.ScheduledFuture<?> future = scheduleOnce(
            () -> counter.incrementAndGet(),
            Duration.ofMillis(50)
        );
        Thread.sleep(100);
        assertEquals(1, counter.get());
    }

    // ==================== race Tests ====================

    @Test
    public void testRace() {
        Integer result = race(
            () -> {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return 1;
            },
            () -> 2, // This should win
            () -> {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return 3;
            }
        );
        assertEquals((Integer) 2, result);
    }

    // ==================== awaitAll Tests ====================

    @Test
    public void testAwaitAllVarargs() {
        List<Integer> results = awaitAll(
            () -> 1,
            () -> 2,
            () -> 3
        );
        assertEquals(list(1, 2, 3), results);
    }

    @Test
    public void testAwaitAllList() {
        List<java.util.function.Supplier<Integer>> suppliers = list(
            () -> 1,
            () -> 2
        );
        List<Integer> results = awaitAll(suppliers);
        assertEquals(list(1, 2), results);
    }

    @Test
    public void testAwaitAllWithTimeout() {
        List<java.util.function.Supplier<Integer>> suppliers = list(
            () -> 1,
            () -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return 2;
            }
        );
        List<Optional<Integer>> results = awaitAll(suppliers, Duration.ofMillis(100));
        assertEquals(2, results.size());
        assertTrue(results.get(0).isPresent());
        assertFalse(results.get(1).isPresent()); // Timed out
    }

    // ==================== debounce Tests ====================

    @Test
    public void testDebounce() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        Runnable debounced = debounce(() -> counter.incrementAndGet(), Duration.ofMillis(100));

        // Call multiple times quickly
        debounced.run();
        debounced.run();
        debounced.run();

        Thread.sleep(50); // Still within quiet period
        assertEquals(0, counter.get()); // Should not have run yet

        Thread.sleep(100); // After quiet period
        assertEquals(1, counter.get()); // Should have run once
    }

    // ==================== throttle Tests ====================

    @Test
    public void testThrottle() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        Runnable throttled = throttle(() -> counter.incrementAndGet(), Duration.ofMillis(100));

        throttled.run(); // Should execute
        throttled.run(); // Should be throttled
        throttled.run(); // Should be throttled

        assertEquals(1, counter.get());

        Thread.sleep(150); // Wait for throttle period
        throttled.run(); // Should execute again

        assertEquals(2, counter.get());
    }
}
