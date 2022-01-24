package com.hxy.recipe.util;

import jdk.internal.misc.Unsafe;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
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

    public static void randomSleepInMillis() {
        sleepInMillis(ThreadLocalRandom.current().nextInt(1, 100));
    }

    public static void assertEx(Runnable runnable, Class<?> exClazz) {
        try {
            runnable.run();
            throw new AssertionError("no exception");
        } catch (Exception e) {
            if (e.getClass() != exClazz) {
                String err = String.format("ex %s is not %s", e.getClass().getSimpleName(), exClazz.getSimpleName());
                throw new AssertionError(err);
            } else {
                log.info("assert ex {} success", e.getClass().getSimpleName());
            }
        }
    }

    public static Unsafe getUnsafe() {
        try {
            return Unsafe.getUnsafe();
        } catch (Exception e) {
            log.error("direct get unsafe fail:", e);
            try {
                Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                return (Unsafe) field.get(null);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void join() {
        while (true) {
            sleepInSeconds(1000L);
        }
    }

}
