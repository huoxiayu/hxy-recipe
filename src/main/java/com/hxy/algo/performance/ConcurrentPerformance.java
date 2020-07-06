package com.hxy.algo.performance;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ConcurrentPerformance {

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger ai = new AtomicInteger();
        ConcurrentSkipListMap<Integer, Integer> skipList = new ConcurrentSkipListMap<>();
        ExecutorService es = Utils.newExecutors("thread-");
        for (int loop = 1; loop <= 20; loop++) {
            es.execute(() -> {
                Utils.sleepInSeconds(2L);
                for (int i = 0; i < 20; i++) {
                    skipList.computeIfAbsent(i, k -> ai.getAndIncrement());
                }
            });
        }

        es.shutdown();
        es.awaitTermination(10L, TimeUnit.SECONDS);

        log.info("size: {}", skipList.size());

        int[] intList = new int[skipList.size()];
        skipList.forEach((k, v) -> intList[k] = v);
        Arrays.sort(intList);

        for (int intItem : intList) {
            log.info("" + intItem);
        }
    }

}
