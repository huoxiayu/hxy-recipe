package com.hxy.recipe.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BenchmarkUtil {

    public static long singleRun(Runnable run) {
        long start = System.currentTimeMillis();
        run.run();
        return System.currentTimeMillis() - start;
    }

    public static long multiRun(Runnable runnable) {
        return multiRun(runnable, Utils.CORES);
    }

    public static long multiRun(Runnable runnable, int threadNum) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadNum; i++) {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            threads.add(thread);
        }
        long start = System.currentTimeMillis();
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException ignored) {

            }
        }
        return System.currentTimeMillis() - start;
    }

}
