package com.hxy.algo.performance;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RunnableUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class RecursionAndIterate {

    public static void main(String[] args) {
        int[] num = new int[10];
        Arrays.fill(num, 1);

        AtomicInteger s1 = new AtomicInteger();
        long cost1 = BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> {
            recursion(s1, 0, num);
        }, 1000_0000));
        log.info("s1: {}, cost: {}", s1, cost1);

        AtomicInteger s2 = new AtomicInteger();
        long cost2 = BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> {
            iterate(s2, num);
        }, 1000_0000));
        log.info("s2: {}, cost: {}", s2, cost2);
    }

    private static void recursion(AtomicInteger sum, int i, int[] num) {
        if (i >= num.length) {
            return;
        }

        sum.addAndGet(num[i]);
        recursion(sum, i + 1, num);
    }

    private static void iterate(AtomicInteger sum, int[] num) {
        for (int i = 0; i < num.length; i++) {
            sum.addAndGet(num[i]);
        }
    }

}
