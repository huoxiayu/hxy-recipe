package com.hxy.recipe.jdk.juc.forkjoin;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Slf4j
public class ForkJoinPool {

    static {
        int parallel = 1;
        System.setProperty(
                "java.util.concurrent.ForkJoinPool.common.parallelism",
                String.valueOf(parallel)
        );
    }

    public static void main(String[] args) {
        BenchmarkUtil.singleRun(() -> {
            AtomicInteger cnt = new AtomicInteger();
            IntStream.iterate(1, i -> i + 1)
                    .limit(50L)
                    .parallel()
                    .forEach(any -> {
                        Utils.sleepInSeconds(1L);
                        cnt.incrementAndGet();
                        log.info("process end");
                    });
            log.info("cnt -> {}", cnt);
        }, "total process");
    }

}
