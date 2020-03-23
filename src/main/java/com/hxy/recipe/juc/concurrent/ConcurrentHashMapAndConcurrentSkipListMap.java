package com.hxy.recipe.juc.concurrent;

import com.hxy.recipe.util.BenchmarkUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ConcurrentHashMapAndConcurrentSkipListMap {

    public static void main(String[] args) {
        int len = 500_0000;
        List<Integer> numList = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            numList.add(i);
        }

        ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        long concurrentHashMapTime = BenchmarkUtil.multiRun(() -> {
            numList.forEach(num -> concurrentHashMap.put(num, num));
        });
        log.info("concurrentHashMap cost {} millis size {}", concurrentHashMapTime, concurrentHashMap.size());

        ConcurrentSkipListMap<Integer, Integer> concurrentSkipListMap = new ConcurrentSkipListMap<>();
        long concurrentSkipListMapTime = BenchmarkUtil.multiRun(() -> {
            numList.forEach(num -> concurrentSkipListMap.put(num, num));
        });
        log.info("concurrentSkipListMap cost {} millis size {}", concurrentSkipListMapTime, concurrentSkipListMap.size());

        AtomicInteger hashSum = new AtomicInteger();
        long hashRetrieveTime = BenchmarkUtil.multiRun(() -> {
            hashSum.addAndGet(concurrentHashMap.values().stream().mapToInt(Integer::intValue).sum());
        });
        log.info("concurrentHashMap cost {} millis sum {}", hashRetrieveTime, hashSum);

        AtomicInteger skipListSum = new AtomicInteger();
        long skipListRetrieveTime = BenchmarkUtil.multiRun(() -> {
            skipListSum.addAndGet(concurrentSkipListMap.values().stream().mapToInt(Integer::intValue).sum());
        });
        log.info("concurrentSkipListMap cost {} millis sum {}", skipListRetrieveTime, skipListSum);
    }

}
