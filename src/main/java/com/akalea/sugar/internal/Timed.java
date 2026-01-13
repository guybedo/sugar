package com.akalea.sugar.internal;

import java.time.Duration;

/**
 * A wrapper that holds a value along with its computation duration.
 */
public class Timed<T> {

    private final T value;
    private final Duration duration;
    private final long startTimeMs;
    private final long endTimeMs;

    private Timed(T value, Duration duration, long startTimeMs, long endTimeMs) {
        this.value = value;
        this.duration = duration;
        this.startTimeMs = startTimeMs;
        this.endTimeMs = endTimeMs;
    }

    /**
     * Creates a Timed result from a supplier, measuring execution time.
     */
    public static <T> Timed<T> of(java.util.function.Supplier<T> supplier) {
        long start = System.currentTimeMillis();
        T result = supplier.get();
        long end = System.currentTimeMillis();
        return new Timed<>(result, Duration.ofMillis(end - start), start, end);
    }

    /**
     * Creates a Timed result with nanosecond precision.
     */
    public static <T> Timed<T> ofNano(java.util.function.Supplier<T> supplier) {
        long startNano = System.nanoTime();
        long startMs = System.currentTimeMillis();
        T result = supplier.get();
        long endNano = System.nanoTime();
        long endMs = System.currentTimeMillis();
        return new Timed<>(result, Duration.ofNanos(endNano - startNano), startMs, endMs);
    }

    /**
     * Times a runnable (void operation).
     */
    public static Timed<Void> ofRunnable(Runnable runnable) {
        return of(() -> {
            runnable.run();
            return null;
        });
    }

    /**
     * Gets the computed value.
     */
    public T getValue() {
        return value;
    }

    /**
     * Gets the computation duration.
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     * Gets the duration in milliseconds.
     */
    public long getMillis() {
        return duration.toMillis();
    }

    /**
     * Gets the duration in nanoseconds.
     */
    public long getNanos() {
        return duration.toNanos();
    }

    /**
     * Gets the duration in seconds.
     */
    public double getSeconds() {
        return duration.toMillis() / 1000.0;
    }

    /**
     * Gets the start time in milliseconds since epoch.
     */
    public long getStartTimeMs() {
        return startTimeMs;
    }

    /**
     * Gets the end time in milliseconds since epoch.
     */
    public long getEndTimeMs() {
        return endTimeMs;
    }

    /**
     * Maps the value while preserving timing info.
     */
    public <R> Timed<R> map(java.util.function.Function<T, R> mapper) {
        return new Timed<>(mapper.apply(value), duration, startTimeMs, endTimeMs);
    }

    @Override
    public String toString() {
        return "Timed{value=" + value + ", duration=" + duration.toMillis() + "ms}";
    }
}
