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

    public static <T> void pEach(List<T> objects, Consumer<T> function) {
        pEach(objects, function, Runtime.getRuntime().availableProcessors());
    }

    public static <T> void pEach(List<T> objects, Consumer<T> function, int threadCount) {
        List<Runnable> tasks = map(objects, o -> () -> function.accept(o));
        execute(tasks, threadCount);
    }
}
