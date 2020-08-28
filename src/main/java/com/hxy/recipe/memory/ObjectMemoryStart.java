package com.hxy.recipe.memory;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.RamUsageEstimator;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ObjectMemoryStart {

    private static final int TIMES = 10_0000;
    private static final Object VALUE = new Object();

    private static class IntClass {
        private int useless;
    }

    private static class IntegerClass {
        private Integer useless;
    }

    public static void main(String[] args) {
        log.info("size of a object {}", RamUsageEstimator.humanSizeOf(new Object()));
        log.info("size of a integer {}", RamUsageEstimator.humanSizeOf(100000));
        log.info("size of a int[] {}", RamUsageEstimator.humanSizeOf(new int[10000]));
        log.info("size of a object[] {}", RamUsageEstimator.humanSizeOf(new Object[10000]));
        log.info("size of a IntClass[] {}", RamUsageEstimator.humanSizeOf(new IntClass[10000]));
        log.info("size of a IntegerClass[] {}", RamUsageEstimator.humanSizeOf(new IntegerClass[10000]));

        performanceTestForConcurrentHashMap(new ConcurrentHashMap<>());
        performanceTestForConcurrentHashMap(new ConcurrentHashMap<>(TIMES));
    }

    private static void performanceTestForConcurrentHashMap(ConcurrentHashMap<Integer, Object> i2o) {
        log.info("memory test start");
        long start = System.currentTimeMillis();
        for (int i = 0; i < TIMES; i++) {
            i2o.put(i * 100 + 31, VALUE);
        }

        long cost = System.currentTimeMillis() - start;
        log.info("cost {} millis", cost);
        log.info("size of concurrent hash map {}", RamUsageEstimator.humanSizeOf(i2o));
    }

}
