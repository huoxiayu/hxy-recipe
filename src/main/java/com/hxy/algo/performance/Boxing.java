package com.hxy.algo.performance;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RunnableUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class Boxing {

    public static void main(String[] args) {
        int[] num = new int[1000];
        Arrays.fill(num, 1);
        List<Integer> list = IntStream.of(num).boxed().collect(Collectors.toList());

        AtomicInteger s1 = new AtomicInteger();
        long cost1 = BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> {
            for (int i = 0; i < num.length; i++) {
                s1.addAndGet(num[i]);
            }

        }));
        log.info("s1 {}, cost: {}", s1, cost1);

        AtomicInteger s2 = new AtomicInteger();
        long cost2 = BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> {
            for (int i = 0; i < list.size(); i++) {
                Integer integer = list.get(i);
                s2.addAndGet(integer);
            }
        }));
        log.info("s2 {}, cost: {}", s2, cost2);
    }

}
