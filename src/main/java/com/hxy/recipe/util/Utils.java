package com.hxy.recipe.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utils {

    public static final int PORT = 8888;
    public static final int CORES = Runtime.getRuntime().availableProcessors();

    public static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(CORES, newThreadFactory("scheduler"));

    public static ScheduledExecutorService newSingleScheduledExecutors(String prefix) {
        return Executors.newSingleThreadScheduledExecutor(newThreadFactory(prefix));
    }

    private static final Map<String, ExecutorService> nameExecutorServiceMap = new ConcurrentHashMap<>();

    public static ExecutorService newSingleExecutors(String prefix) {
        return newExecutors(prefix, 1);
    }

    public static ExecutorService newExecutors(String prefix) {
        return newExecutors(prefix, CORES * 2);
    }

    public static ExecutorService newExecutors(String prefix, int coolPoolSize) {
        return nameExecutorServiceMap.computeIfAbsent(prefix, k -> Executors.newFixedThreadPool(coolPoolSize, newThreadFactory(prefix)));
    }

    public static ThreadFactory newThreadFactory(String prefix) {
        return new ThreadFactory() {
            private final AtomicInteger CNT = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, prefix + CNT.incrementAndGet());
                thread.setDaemon(true);
                return thread;
            }
        };
    }

    public static void sleep() {
        sleepInSeconds(60L);
    }

    public static void sleepInMillis(long timeInMillis) {
        try {
            if (timeInMillis > 0L) {
                TimeUnit.MILLISECONDS.sleep(timeInMillis);
            }
        } catch (InterruptedException ignored) {
        }
    }

    public static void sleepInSeconds(long timeInSeconds) {
        sleepInMillis(timeInSeconds * 1000L);
    }

    public static void sleepInMinutes(long timeInMinutes) {
        sleepInSeconds(timeInMinutes * 60L);
    }

    public static void randomSleepInMillis() {
        sleepInMillis(ThreadLocalRandom.current().nextInt(1, 100));
    }

}
