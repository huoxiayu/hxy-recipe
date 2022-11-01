package com.hxy.recipe.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class ParallelBenchmark {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(
            Runtime.getRuntime().availableProcessors() << 2
    );

    private static final ScheduledExecutorService runnable = Executors.newScheduledThreadPool(
            Runtime.getRuntime().availableProcessors() << 2
    );

    public static void main(String[] args) {
        int qpsNum = 100;
        AtomicLong totalCost = new AtomicLong(0L);
        AtomicLong cnt = new AtomicLong(0L);
        // 即使period到了，前一个runnable没有执行完，也不会开始下一个任务
        int ONE_SECOND_IN_MICROSECONDS = 1000000;
        scheduler.scheduleAtFixedRate(() -> runnable.execute(() -> {
            long start = System.currentTimeMillis();
            slow_method();
            long cost = System.currentTimeMillis() - start;
            totalCost.addAndGet(cost);
        }), 0, ONE_SECOND_IN_MICROSECONDS / qpsNum, TimeUnit.MICROSECONDS);

        scheduler.scheduleAtFixedRate(
                () -> log.info("totalCost " + totalCost + " cnt " + cnt),
                0L,
                1L,
                TimeUnit.SECONDS
        );
    }

    public static void slow_method() {
        try {
            TimeUnit.MILLISECONDS.sleep(100L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
