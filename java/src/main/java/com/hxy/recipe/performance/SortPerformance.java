package com.hxy.recipe.performance;

import com.hxy.recipe.util.BenchmarkUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class SortPerformance {

    private static final int TIMES = 100_0000;

    public static void main(String[] args) {
        while (true) {
            long start = System.currentTimeMillis();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < TIMES; i++) {
                list.add(ThreadLocalRandom.current().nextInt(TIMES));
            }
            log.info("add cost {} millis", System.currentTimeMillis() - start);

            long cost = BenchmarkUtil.singleRun(() -> list.sort(Integer::compare));
            log.info("sort cost {} millis", cost);

            int[] intArray = new int[TIMES];
            for (int i = 0; i < TIMES; i++) {
                intArray[i] = ThreadLocalRandom.current().nextInt(TIMES);
            }

            long intArraySortCost = BenchmarkUtil.singleRun(() -> Arrays.sort(intArray));
            log.info("intArraySort cost {} millis", intArraySortCost);
        }
    }

}
