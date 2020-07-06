package com.hxy.algo.performance;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@Slf4j
public class MapPerformanceStart {

    public static void main(String[] args) {
        Object dummy = new Object();
        int iRange = 1000;
        int jRange = 10000;

        // 1、使用ConcurrentHashMap且冲突较多
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Object>> concurrentConflict = new ConcurrentHashMap<>();
        long start = System.currentTimeMillis();
        IntStream.rangeClosed(1, iRange).parallel()
            .forEach(i -> IntStream.rangeClosed(1, jRange).parallel()
                .forEach(j -> concurrentConflict.computeIfAbsent(j, any -> new ConcurrentHashMap<>())
                    .putIfAbsent(i * jRange + j, dummy))
            );
        long cost = System.currentTimeMillis() - start;
        log.info("concurrent conflict cost {} millis", cost);

        // 2、使用ConcurrentHashMap且无冲突
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Object>> concurrentNoConflict = new ConcurrentHashMap<>();
        start = System.currentTimeMillis();
        IntStream.rangeClosed(1, iRange).parallel()
            .forEach(i -> IntStream.rangeClosed(1, jRange).parallel()
                .forEach(j -> concurrentNoConflict.computeIfAbsent(i, any -> new ConcurrentHashMap<>())
                    .putIfAbsent(i * jRange + j, dummy))
            );
        cost = System.currentTimeMillis() - start;
        log.info("concurrent no conflict cost {} millis", cost);

        // 3、单线程ConcurrentHashMap
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Object>> concurrentHashMap = new ConcurrentHashMap<>();
        start = System.currentTimeMillis();
        IntStream.rangeClosed(1, iRange)
            .forEach(i -> IntStream.rangeClosed(1, jRange)
                .forEach(j -> concurrentHashMap.computeIfAbsent(j, any -> new ConcurrentHashMap<>())
                    .putIfAbsent(i * jRange + j, dummy))
            );
        cost = System.currentTimeMillis() - start;
        log.info("concurrent single thread cost {} millis", cost);

        // 4、单线程HashMap
        Map<Integer, Map<Integer, Object>> hashMap = new HashMap<>();
        start = System.currentTimeMillis();
        IntStream.rangeClosed(1, iRange)
            .forEach(i -> IntStream.rangeClosed(1, jRange)
                .forEach(j -> hashMap.computeIfAbsent(j, any -> new HashMap<>())
                    .putIfAbsent(i * jRange + j, dummy))
            );
        cost = System.currentTimeMillis() - start;
        log.info("hash-map cost {} millis", cost);
    }

}
