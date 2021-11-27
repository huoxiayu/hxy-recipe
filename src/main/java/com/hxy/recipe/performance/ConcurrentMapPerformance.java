package com.hxy.recipe.performance;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.jctools.maps.NonBlockingHashMap;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Slf4j
public class ConcurrentMapPerformance {

    private static final int SIZE = 1000_0000;
    private static final int PARALLEL = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        run(false);

        Utils.sleepInSeconds(5L);

        run(true);
    }

    private static void run(boolean flag) {
        List<Integer> numList = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            numList.add(i);
        }

        org.eclipse.collections.impl.map.mutable.ConcurrentHashMapUnsafe<Integer, Integer> eUnsafeConcurrentHashMap = new org.eclipse.collections.impl.map.mutable.ConcurrentHashMapUnsafe<>(SIZE);
        long eUnsafeConcurrentHashMapPutTime = BenchmarkUtil.multiRun(() -> {
            numList.forEach(num -> eUnsafeConcurrentHashMap.put(num, num));
        }, PARALLEL);
        Assert.isTrue(eUnsafeConcurrentHashMap.size() == SIZE, "eq");

        long eUnsafeConcurrentHashMapGetTime = BenchmarkUtil.multiRun(() -> {
            for (int i = 0; i < SIZE; i++) {
                eUnsafeConcurrentHashMap.get(i);
            }
        }, PARALLEL);

        eUnsafeConcurrentHashMap.clear();

        ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>(SIZE);
        long concurrentHashMapPutTime = BenchmarkUtil.multiRun(() -> {
            numList.forEach(num -> concurrentHashMap.put(num, num));
        }, PARALLEL);
        Assert.isTrue(concurrentHashMap.size() == SIZE, "eq");

        long concurrentHashMapGetTime = BenchmarkUtil.multiRun(() -> {
            for (int i = 0; i < SIZE; i++) {
                concurrentHashMap.get(i);
            }
        }, PARALLEL);

        concurrentHashMap.clear();

        ConcurrentSkipListMap<Integer, Integer> concurrentSkipListMap = new ConcurrentSkipListMap<>();
        long concurrentSkipListPutTime = BenchmarkUtil.multiRun(() -> {
            numList.forEach(num -> concurrentSkipListMap.put(num, num));
        }, PARALLEL);
        Assert.isTrue(concurrentSkipListMap.size() == SIZE, "eq");

        long concurrentSkipListGetTime = BenchmarkUtil.multiRun(() -> {
            for (int i = 0; i < SIZE; i++) {
                concurrentSkipListMap.get(i);
            }
        }, PARALLEL);

        concurrentSkipListMap.clear();

        NonBlockingHashMap<Integer, Integer> nonBlockingHashMap = new NonBlockingHashMap<>(SIZE);
        long nonBlockingHashMapPutTime = BenchmarkUtil.multiRun(() -> {
            numList.forEach(num -> nonBlockingHashMap.put(num, num));
        }, PARALLEL);
        Assert.isTrue(nonBlockingHashMap.size() == SIZE, "eq");

        long nonBlockingHashMapGetTime = BenchmarkUtil.multiRun(() -> {
            for (int i = 0; i < SIZE; i++) {
                nonBlockingHashMap.get(i);
            }
        }, PARALLEL);

        nonBlockingHashMap.clear();

        org.eclipse.collections.impl.map.mutable.ConcurrentHashMap<Integer, Integer> eConcurrentHashMap = new org.eclipse.collections.impl.map.mutable.ConcurrentHashMap<>(SIZE);
        long eConcurrentHashMapPutTime = BenchmarkUtil.multiRun(() -> {
            numList.forEach(num -> eConcurrentHashMap.put(num, num));
        }, PARALLEL);
        Assert.isTrue(eConcurrentHashMap.size() == SIZE, "eq");

        long eConcurrentHashMapGetTime = BenchmarkUtil.multiRun(() -> {
            for (int i = 0; i < SIZE; i++) {
                eConcurrentHashMap.get(i);
            }
        }, PARALLEL);

        eConcurrentHashMap.clear();

        if (flag) {
            log.info("concurrentHashMapPutTime {}", concurrentHashMapPutTime);
            log.info("concurrentSkipListPutTime {}", concurrentSkipListPutTime);
            log.info("nonBlockingHashMapPutTime {}", nonBlockingHashMapPutTime);
            log.info("eConcurrentHashMapPutTime {}", eConcurrentHashMapPutTime);
            log.info("eUnsafeConcurrentHashMapPutTime {}", eUnsafeConcurrentHashMapPutTime);

            log.info("concurrentHashMapGetTime {}", concurrentHashMapGetTime);
            log.info("concurrentSkipListGetTime {}", concurrentSkipListGetTime);
            log.info("nonBlockingHashMapGetTime {}", nonBlockingHashMapGetTime);
            log.info("eConcurrentHashMapGetTime {}", eConcurrentHashMapGetTime);
            log.info("eUnsafeConcurrentHashMapGetTime {}", eUnsafeConcurrentHashMapGetTime);
        }
    }

}
