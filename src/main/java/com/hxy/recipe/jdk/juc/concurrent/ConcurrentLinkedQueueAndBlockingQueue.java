package com.hxy.recipe.jdk.juc.concurrent;

import com.hxy.recipe.util.BenchmarkUtil;
import lombok.extern.slf4j.Slf4j;
import org.spark_project.jetty.util.BlockingArrayQueue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class ConcurrentLinkedQueueAndBlockingQueue {

    public static void main(String[] args) {
        int size = 10000;
        String[] input = new String[size];
        for (int i = 0; i < size; i++) {
            input[i] = String.valueOf(i);
        }

        Queue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        long concurrentLinkedQueueCost = BenchmarkUtil.multiRun(() -> {
            for (int i = 0; i < size; i++) {
                concurrentLinkedQueue.add(input[i]);
            }
        }, 20);
        log.info("concurrentLinkedQueue cost {} millis", concurrentLinkedQueueCost);
        log.info("concurrentLinkedQueue.size(): {}", concurrentLinkedQueue.size());

        Queue<String> blockingArrayQueue = new BlockingArrayQueue<>();
        long blockingArrayQueueCost = BenchmarkUtil.multiRun(() -> {
            for (int i = 0; i < size; i++) {
                blockingArrayQueue.add(input[i]);
            }
        });
        log.info("blockingArrayQueue cost {} millis", blockingArrayQueueCost);
        log.info("blockingArrayQueue.size(): {}", blockingArrayQueue.size());
    }

}
