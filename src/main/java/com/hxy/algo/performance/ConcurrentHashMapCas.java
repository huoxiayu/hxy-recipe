package com.hxy.algo.performance;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

@Slf4j
public class ConcurrentHashMapCas {

    public static void main(String[] args) throws InterruptedException {
        int size = 1000_0000;

        ConcurrentHashMap<Integer, AtomicInteger> map1 = new ConcurrentHashMap<>(size);
        Runnable runnable1 = () -> {
            for (int i = 0; i < size; i++) {
                map1.computeIfAbsent(i, any -> new AtomicInteger()).getAndIncrement();
            }
        };

        run(runnable1);

        ConcurrentHashMap<Integer, AtomicInteger> map2 = new ConcurrentHashMap<>(size);
        Runnable runnable2 = () -> {
            int random = ThreadLocalRandom.current().nextInt(1000);
            for (int i = random; i < size + random; i++) {
                map2.computeIfAbsent(i % size, any -> new AtomicInteger()).getAndIncrement();
            }
        };

        run(runnable2);
    }

    private static void run(Runnable runnable) throws InterruptedException {
        LongAdder sumCost = new LongAdder();
        log.info("cores -> {}", Utils.CORES);
        ExecutorService executorService = Utils.newExecutors("thread-group-" + ThreadLocalRandom.current().nextInt(10000));
        for (int core = 0; core < Utils.CORES; core++) {
            executorService.execute(() -> {
                long start = System.currentTimeMillis();
                runnable.run();
                sumCost.add(System.currentTimeMillis() - start);
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        log.info("sumCost {}", sumCost);
    }

}
