package com.hxy.algo.performance;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RunnableUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.set.mutable.primitive.LongHashSet;
import org.spark_project.jetty.util.ConcurrentHashSet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
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
                s2.addAndGet(list.get(i));
            }
        }));
        log.info("s2 {}, cost: {}", s2, cost2);

        Set<Integer> integerSet = new HashSet<>();
        Set<Integer> concurrentIntegerSet = new ConcurrentHashSet<>();
        ConcurrentSkipListSet<Integer> concurrentSkipListSet = new ConcurrentSkipListSet<>();
        LongHashSet intSet = new LongHashSet();
        for (int i = 0; i < 10000; i++) {
            integerSet.add(i);
            concurrentIntegerSet.add(i);
            concurrentSkipListSet.add(i);
            intSet.add(i);
        }

        log.info("cost: {}", BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> {
            integerSet.contains(99999);
        })));

        log.info("cost: {}", BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> {
            concurrentIntegerSet.contains(99999);
        })));

        log.info("cost: {}", BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> {
            concurrentSkipListSet.contains(99999);
        })));

        log.info("cost: {}", BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> {
            intSet.contains(99999);
        })));
    }

}
