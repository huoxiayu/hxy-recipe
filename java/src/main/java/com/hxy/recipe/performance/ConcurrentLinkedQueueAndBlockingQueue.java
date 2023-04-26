package com.hxy.recipe.performance;

import com.hxy.recipe.util.BenchmarkUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * [parallel -> 12]
 * [per-size -> 100000]
 * [linkedBlockingQueue cost 155 millis]
 * [linkedBlockingQueue.size(): 1200000]
 * [concurrentLinkedQueue cost 116 millis]
 * [concurrentLinkedQueue.size(): 1200000]
 * [blockingArrayQueue cost 58 millis]
 * [blockingArrayQueue.size(): 1200000]
 */
@Slf4j
public class ConcurrentLinkedQueueAndBlockingQueue {

    private static final int PARALLEL = Runtime.getRuntime().availableProcessors();
    private static final int SIZE = 10_0000;
    private static final int TOTAL_SIZE = SIZE * PARALLEL;

    public static void main(String[] args) {
        int n = 10;
        for (int i = 0; i < n; i++) {
            log.info("times -> {}", i);
            run(i == n - 1);
            try {
                TimeUnit.SECONDS.sleep(5L);
            } catch (InterruptedException ignore) {

            }
        }
    }

    private static void run(boolean flag) {
        String[] input = new String[SIZE];
        for (int i = 0; i < SIZE; i++) {
            input[i] = String.valueOf(i);
        }

        LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>(TOTAL_SIZE);
        long linkedBlockingQueueCost = BenchmarkUtil.multiRun(() -> {
            for (int i = 0; i < SIZE; i++) {
                linkedBlockingQueue.add(input[i]);
            }
        }, PARALLEL);

        ConcurrentLinkedQueue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        long concurrentLinkedQueueCost = BenchmarkUtil.multiRun(() -> {
            for (int i = 0; i < SIZE; i++) {
                concurrentLinkedQueue.add(input[i]);
            }
        }, PARALLEL);

        ArrayBlockingQueue<String> blockingArrayQueue = new ArrayBlockingQueue<>(TOTAL_SIZE);
        long blockingArrayQueueCost = BenchmarkUtil.multiRun(() -> {
            for (int i = 0; i < SIZE; i++) {
                blockingArrayQueue.add(input[i]);
            }
        }, PARALLEL);

        if (flag) {
            log.info("parallel -> {}", PARALLEL);
            log.info("per-size -> {}", SIZE);

            log.info("linkedBlockingQueue cost {} millis", linkedBlockingQueueCost);
            log.info("linkedBlockingQueue.size(): {}", linkedBlockingQueue.size());

            log.info("concurrentLinkedQueue cost {} millis", concurrentLinkedQueueCost);
            log.info("concurrentLinkedQueue.size(): {}", concurrentLinkedQueue.size());

            log.info("blockingArrayQueue cost {} millis", blockingArrayQueueCost);
            log.info("blockingArrayQueue.size(): {}", blockingArrayQueue.size());
        }
    }

}
