package com.hxy.recipe.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utils {

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

    public static void sleep(long timeInSeconds) {
        try {
            if (timeInSeconds > 0) {
                TimeUnit.SECONDS.sleep(timeInSeconds);
            }
        } catch (InterruptedException ignored) {
        }
    }

}
