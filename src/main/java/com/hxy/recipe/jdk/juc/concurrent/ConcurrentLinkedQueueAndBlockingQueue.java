package com.hxy.recipe.jdk.juc.concurrent;

import com.hxy.recipe.util.BenchmarkUtil;
import lombok.extern.slf4j.Slf4j;
import org.spark_project.jetty.util.BlockingArrayQueue;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * [parallel -> 12]
 * [per-size -> 10000]
 * [linkedBlockingQueue cost 10 millis]
 * [linkedBlockingQueue.size(): 120000]
 * [concurrentLinkedQueue cost 15 millis]
 * [concurrentLinkedQueue.size(): 120000]
 * [blockingArrayQueue cost 189 millis]
 * [blockingArrayQueue.size(): 120000]
 */
@Slf4j
public class ConcurrentLinkedQueueAndBlockingQueue {

    private static final int PARALLEL = Runtime.getRuntime().availableProcessors();
    private static final int SIZE = 10000;

    public static void main(String[] args) {
        run();
        run();

        try {
            TimeUnit.SECONDS.sleep(5L);
        } catch (InterruptedException ignore) {

        }

        run();
    }

    private static void run() {
        String[] input = new String[SIZE];
        for (int i = 0; i < SIZE; i++) {
            input[i] = String.valueOf(i);
        }

        log.info("parallel -> {}", PARALLEL);
        log.info("per-size -> {}", SIZE);

        LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>();
        long linkedBlockingQueueCost = BenchmarkUtil.multiRun(() -> {
            for (int i = 0; i < SIZE; i++) {
                linkedBlockingQueue.add(input[i]);
            }
        }, PARALLEL);
        log.info("linkedBlockingQueue cost {} millis", linkedBlockingQueueCost);
        log.info("linkedBlockingQueue.size(): {}", linkedBlockingQueue.size());

        ConcurrentLinkedQueue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        long concurrentLinkedQueueCost = BenchmarkUtil.multiRun(() -> {
            for (int i = 0; i < SIZE; i++) {
                concurrentLinkedQueue.add(input[i]);
            }
        }, PARALLEL);
        log.info("concurrentLinkedQueue cost {} millis", concurrentLinkedQueueCost);
        log.info("concurrentLinkedQueue.size(): {}", concurrentLinkedQueue.size());

        BlockingArrayQueue<String> blockingArrayQueue = new BlockingArrayQueue<>();
        long blockingArrayQueueCost = BenchmarkUtil.multiRun(() -> {
            for (int i = 0; i < SIZE; i++) {
                blockingArrayQueue.add(input[i]);
            }
        }, PARALLEL);
        log.info("blockingArrayQueue cost {} millis", blockingArrayQueueCost);
        log.info("blockingArrayQueue.size(): {}", blockingArrayQueue.size());
    }

}
