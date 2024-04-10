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

public class Parallel {

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

    public static <T, R> List<R> pMap(List<T> objects, Function<T, R> function, int threadCount) {
        Map<Integer, R> results = java.util.Collections.synchronizedMap(new HashMap());
        List<Runnable> tasks =
            map(
                enumerate(objects),
                o -> {
                    return (Runnable) () -> {
                        results.put(o.getKey(), function.apply(o.getValue()));
                    };
                });
        execute(tasks, threadCount);
        return map(sorted(results.keySet()), i -> results.get(i));
    }

    public static <T> void pEach(List<T> objects, Consumer<T> function, int threadCount) {
        List<Runnable> tasks =
            map(
                objects,
                o -> (Runnable) () -> {
                    function.accept(o);
                });
        execute(tasks, threadCount);
    }
}
