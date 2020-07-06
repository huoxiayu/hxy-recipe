package com.hxy.algo.performance;

import com.hxy.recipe.util.BenchmarkUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class SortPerformance {

    private static final int TIMES = 30_0000;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < TIMES; i++) {
            list.add(ThreadLocalRandom.current().nextInt(TIMES));
        }
        log.info("add cost {} millis", System.currentTimeMillis() - start);

        long cost = BenchmarkUtil.singleRun(() -> list.sort(Integer::compare));
        log.info("sort cost {} millis", cost);
    }

}
