package com.hxy.recipe.datastructure;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RunnableUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class TraverseMapStart {

    private static final int TIMES = 1000000;

    public static void main(String[] args) {
        Map<String, Integer> hashMap = new HashMap<>(TIMES);
        Map<String, Integer> linkedHashMap = new LinkedHashMap<>(TIMES);
        for (int i = 0; i < TIMES; i++) {
            hashMap.put(String.valueOf(i), i);
            linkedHashMap.put(String.valueOf(i), i);
        }

        run(hashMap);
        run(linkedHashMap);
    }

    private static void run(Map<String, Integer> map) {
        AtomicInteger sum = new AtomicInteger();
        long cost = BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> map.values().forEach(sum::addAndGet), 1000));
        log.info("cost {} millis", cost);
    }

}
