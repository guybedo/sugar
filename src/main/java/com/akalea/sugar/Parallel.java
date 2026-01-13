package com.akalea.sugar;

import static com.akalea.sugar.Collections.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Parallel {

    public static Thread background(Runnable runnable, Duration delay) {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    runnable.run();
                    Thread.currentThread().sleep(delay.getSeconds() * 1000);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t.start();
        return t;
    }

    private static void execute(List<Runnable> tasks, int threadCount) {
        execute(tasks, threadCount, null);
    }

    private static void execute(List<Runnable> tasks, int threadCount, Duration maxDuration) {
        try {
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            List<Future> futures = map(tasks, t -> executor.submit(t));
            executor.shutdown();
            if (maxDuration != null)
                executor.awaitTermination(maxDuration.getSeconds(), TimeUnit.SECONDS);
            else
                forEach(futures, f -> {
                    try {
                        f.get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void execute(List<Runnable> tasks, ExecutorService executor) {
        List<Future> futures = map(tasks, t -> executor.submit(t));
        forEach(futures, f -> {
            try {
                f.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static <T> List<T> compute(List<Supplier<T>> functions, int threadCount) {
        Map<Integer, T> results = java.util.Collections.synchronizedMap(new HashMap());
        List<Runnable> tasks =
            map(
                enumerate(functions),
                f -> () -> results.put(f.getKey(), f.getValue().get()));
        execute(tasks, threadCount);
        return map(sorted(results.keySet()), i -> results.get(i));
    }

    public static <T, R> List<R> pMap(List<T> objects, Function<T, R> function) {
        return pMap(objects, function, Runtime.getRuntime().availableProcessors());
    }

    public static <T, R> List<R> pMap(List<T> objects, Function<T, R> function, int threadCount) {
        Map<Integer, R> results = java.util.Collections.synchronizedMap(new HashMap());
        List<Runnable> tasks =
            map(
                enumerate(objects),
                o -> () -> results.put(o.getKey(), function.apply(o.getValue())));
        execute(tasks, threadCount);
        return map(sorted(results.keySet()), i -> results.get(i));
    }

    public static <T, R> List<R> pMap(
        List<T> objects,
        Function<T, R> function,
        ExecutorService executor) {
        Map<Integer, R> results = java.util.Collections.synchronizedMap(new HashMap());
        List<Runnable> tasks =
            map(
                enumerate(objects),
                o -> () -> results.put(o.getKey(), function.apply(o.getValue())));
        execute(tasks, executor);
        return map(sorted(results.keySet()), i -> results.get(i));
    }

    public static <T> void pEach(List<T> objects, Consumer<T> function) {
        pEach(objects, function, Runtime.getRuntime().availableProcessors());
    }

    public static <T> void pEach(List<T> objects, Consumer<T> function, int threadCount) {
        List<Runnable> tasks = map(objects, o -> () -> function.accept(o));
        execute(tasks, threadCount);
    }

    // ==================== Timing & Execution Utilities ====================

    /**
     * Measures execution time of a supplier.
     */
    public static <T> com.akalea.sugar.internal.Timed<T> timed(Supplier<T> supplier) {
        return com.akalea.sugar.internal.Timed.of(supplier);
    }

    /**
     * Measures execution time of a runnable.
     */
    public static com.akalea.sugar.internal.Timed<Void> timed(Runnable runnable) {
        return com.akalea.sugar.internal.Timed.ofRunnable(runnable);
    }

    /**
     * Retries an operation with fixed delay between attempts.
     */
    public static <T> T retry(Supplier<T> supplier, int maxAttempts, Duration delayBetweenAttempts) {
        Exception lastException = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return supplier.get();
            } catch (Exception e) {
                lastException = e;
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(delayBetweenAttempts.toMillis());
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Retry interrupted", ie);
                    }
                }
            }
        }
        throw new RuntimeException("All " + maxAttempts + " attempts failed", lastException);
    }

    /**
     * Retries an operation with exponential backoff.
     */
    public static <T> T retryWithBackoff(
            Supplier<T> supplier,
            int maxAttempts,
            Duration initialDelay,
            double multiplier) {
        Exception lastException = null;
        long delayMs = initialDelay.toMillis();
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return supplier.get();
            } catch (Exception e) {
                lastException = e;
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(delayMs);
                        delayMs = (long) (delayMs * multiplier);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Retry interrupted", ie);
                    }
                }
            }
        }
        throw new RuntimeException("All " + maxAttempts + " attempts failed", lastException);
    }

    /**
     * Retries a runnable with fixed delay between attempts.
     */
    public static void retry(Runnable runnable, int maxAttempts, Duration delayBetweenAttempts) {
        retry(() -> {
            runnable.run();
            return null;
        }, maxAttempts, delayBetweenAttempts);
    }

    /**
     * Executes a supplier with a timeout.
     * Returns empty Optional if timeout is exceeded.
     */
    public static <T> java.util.Optional<T> timeout(Supplier<T> supplier, Duration timeout) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<T> future = executor.submit(supplier::get);
        try {
            return java.util.Optional.ofNullable(future.get(timeout.toMillis(), TimeUnit.MILLISECONDS));
        } catch (java.util.concurrent.TimeoutException e) {
            future.cancel(true);
            return java.util.Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error executing with timeout", e);
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * Executes a supplier with a timeout.
     * Returns defaultValue if timeout is exceeded.
     */
    public static <T> T timeout(Supplier<T> supplier, Duration timeout, T defaultValue) {
        return timeout(supplier, timeout).orElse(defaultValue);
    }

    /**
     * Executes a runnable with a timeout.
     * Returns true if completed within timeout, false otherwise.
     */
    public static boolean timeout(Runnable runnable, Duration timeout) {
        return timeout(() -> {
            runnable.run();
            return true;
        }, timeout).orElse(false);
    }

    /**
     * Delays execution by the specified duration.
     */
    public static void delay(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Delay interrupted", e);
        }
    }

    /**
     * Delays execution by the specified milliseconds.
     */
    public static void delay(long millis) {
        delay(Duration.ofMillis(millis));
    }

    /**
     * Runs a task periodically at a fixed rate.
     * Returns the scheduled future for cancellation.
     */
    public static java.util.concurrent.ScheduledFuture<?> schedule(
            Runnable task,
            Duration initialDelay,
            Duration period) {
        java.util.concurrent.ScheduledExecutorService scheduler =
            java.util.concurrent.Executors.newSingleThreadScheduledExecutor();
        return scheduler.scheduleAtFixedRate(
            task,
            initialDelay.toMillis(),
            period.toMillis(),
            TimeUnit.MILLISECONDS);
    }

    /**
     * Runs a task once after a delay.
     */
    public static java.util.concurrent.ScheduledFuture<?> scheduleOnce(Runnable task, Duration delay) {
        java.util.concurrent.ScheduledExecutorService scheduler =
            java.util.concurrent.Executors.newSingleThreadScheduledExecutor();
        return scheduler.schedule(task, delay.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Runs multiple suppliers concurrently and returns the first result.
     */
    @SafeVarargs
    public static <T> T race(Supplier<T>... suppliers) {
        ExecutorService executor = Executors.newFixedThreadPool(suppliers.length);
        try {
            List<java.util.concurrent.Callable<T>> callables =
                map(list(suppliers), s -> (java.util.concurrent.Callable<T>) s::get);
            return executor.invokeAny(callables);
        } catch (Exception e) {
            throw new RuntimeException("Race execution failed", e);
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * Runs multiple suppliers concurrently and waits for all to complete.
     */
    @SafeVarargs
    public static <T> List<T> awaitAll(Supplier<T>... suppliers) {
        return awaitAll(list(suppliers));
    }

    /**
     * Runs multiple suppliers concurrently and waits for all to complete.
     */
    public static <T> List<T> awaitAll(List<Supplier<T>> suppliers) {
        return compute(suppliers, Runtime.getRuntime().availableProcessors());
    }

    /**
     * Runs multiple suppliers concurrently with a timeout.
     * Returns results for completed tasks, empty for timed-out tasks.
     */
    public static <T> List<java.util.Optional<T>> awaitAll(
            List<Supplier<T>> suppliers,
            Duration timeout) {
        ExecutorService executor = Executors.newFixedThreadPool(
            Math.min(suppliers.size(), Runtime.getRuntime().availableProcessors()));
        try {
            List<Future<T>> futures = map(suppliers, s -> executor.submit(s::get));
            List<java.util.Optional<T>> results = new java.util.ArrayList<>();
            for (Future<T> future : futures) {
                try {
                    results.add(java.util.Optional.ofNullable(
                        future.get(timeout.toMillis(), TimeUnit.MILLISECONDS)));
                } catch (java.util.concurrent.TimeoutException e) {
                    future.cancel(true);
                    results.add(java.util.Optional.empty());
                }
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException("Await all failed", e);
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * Debounces a function - only executes after the specified quiet period.
     * Returns a wrapper that should be called instead of the original function.
     */
    public static Runnable debounce(Runnable runnable, Duration quietPeriod) {
        final java.util.concurrent.atomic.AtomicReference<java.util.concurrent.ScheduledFuture<?>> scheduled =
            new java.util.concurrent.atomic.AtomicReference<>();
        final java.util.concurrent.ScheduledExecutorService scheduler =
            java.util.concurrent.Executors.newSingleThreadScheduledExecutor();

        return () -> {
            java.util.concurrent.ScheduledFuture<?> prev = scheduled.get();
            if (prev != null) {
                prev.cancel(false);
            }
            scheduled.set(scheduler.schedule(runnable, quietPeriod.toMillis(), TimeUnit.MILLISECONDS));
        };
    }

    /**
     * Throttles a function - only allows execution once per interval.
     * Returns a wrapper that should be called instead of the original function.
     */
    public static Runnable throttle(Runnable runnable, Duration interval) {
        final java.util.concurrent.atomic.AtomicLong lastRun =
            new java.util.concurrent.atomic.AtomicLong(0);

        return () -> {
            long now = System.currentTimeMillis();
            if (now - lastRun.get() >= interval.toMillis()) {
                lastRun.set(now);
                runnable.run();
            }
        };
    }
}
