package com.hxy.recipe.performance;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * eclipse collection中的2中ConcurrentHashMap比JDK中的要快
 */
@Slf4j
public class ConcurrentMapPerformance {

    private static final int SIZE = 1000_0000;
    private static final int PARALLEL = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        run(false);

        Utils.sleepInSeconds(5L);

        while (true) {
            run(true);
        }
    }

    private static void run(boolean flag) {
        List<Integer> numList = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            numList.add(i);
        }

        System.gc();
        Utils.sleepInSeconds(1L);

        org.eclipse.collections.impl.map.mutable.ConcurrentHashMapUnsafe<Integer, Integer> eUnsafeConcurrentHashMap = new org.eclipse.collections.impl.map.mutable.ConcurrentHashMapUnsafe<>(SIZE);
        long eUnsafeConcurrentHashMapPutTime = BenchmarkUtil.multiRun(() -> {
            numList.forEach(num -> eUnsafeConcurrentHashMap.put(num, num));
        }, PARALLEL);
        Assert.isTrue(eUnsafeConcurrentHashMap.size() == SIZE, "eq");

        System.gc();
        Utils.sleepInSeconds(1L);

        long eUnsafeConcurrentHashMapGetTime = BenchmarkUtil.multiRun(() -> {
            for (int i = 0; i < SIZE; i++) {
                eUnsafeConcurrentHashMap.get(i);
            }
        }, PARALLEL);

        eUnsafeConcurrentHashMap.clear();

        System.gc();
        Utils.sleepInSeconds(1L);

        org.eclipse.collections.impl.map.mutable.ConcurrentHashMap<Integer, Integer> eConcurrentHashMap = new org.eclipse.collections.impl.map.mutable.ConcurrentHashMap<>(SIZE);
        long eConcurrentHashMapPutTime = BenchmarkUtil.multiRun(() -> {
            numList.forEach(num -> eConcurrentHashMap.put(num, num));
        }, PARALLEL);
        Assert.isTrue(eConcurrentHashMap.size() == SIZE, "eq");

        System.gc();
        Utils.sleepInSeconds(1L);

        long eConcurrentHashMapGetTime = BenchmarkUtil.multiRun(() -> {
            for (int i = 0; i < SIZE; i++) {
                eConcurrentHashMap.get(i);
            }
        }, PARALLEL);

        eConcurrentHashMap.clear();

        System.gc();
        Utils.sleepInSeconds(1L);

        if (flag) {
            log.info("eConcurrentHashMapPutTime {}", eConcurrentHashMapPutTime);
            log.info("eUnsafeConcurrentHashMapPutTime {}", eUnsafeConcurrentHashMapPutTime);

            log.info("eConcurrentHashMapGetTime {}", eConcurrentHashMapGetTime);
            log.info("eUnsafeConcurrentHashMapGetTime {}", eUnsafeConcurrentHashMapGetTime);
        }
    }

}
