package com.hxy.recipe.jdk.juc.forkjoin;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Slf4j
public class ForkJoinPool {

    static {
        int parallel = 1000;
        System.setProperty(
                "java.util.concurrent.ForkJoinPool.common.parallelism",
                String.valueOf(parallel)
        );
        log.info("set parallel -> {}", parallel);
    }

    public static void main(String[] args) {
        while (true) {
            ConcurrentHashSet<String> set = new ConcurrentHashSet<>();
            BenchmarkUtil.singleRun(() -> {
                AtomicInteger cnt = new AtomicInteger();
                IntStream.iterate(1, i -> i + 1)
                        .limit(5000L)
                        .parallel()
                        .forEach(any -> {
                            Utils.sleepInMillis(30L);
                            cnt.incrementAndGet();
                            // log.info("process end");
                            set.add(Thread.currentThread().getName());
                        });
                log.info("cnt -> {}", cnt);
            }, "total process");
            log.info("set.size() -> {}", set.size());

            System.gc();
            Utils.sleepInSeconds(5L);
        }
    }

}
