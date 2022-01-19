package com.hxy.recipe.compute;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RunnableUtil;
import com.hxy.recipe.util.Utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

public class GCThroughput {

    private static final int RANGE = 1000_0000;
    private static final int MOD = 100;

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) run();

        Utils.sleepInSeconds(5L);
        System.gc();
        Utils.sleepInSeconds(5L);

        run();
    }

    public static void run() {
        ConcurrentHashMap<Integer, LongAdder> int2Adder = new ConcurrentHashMap<>();
        Runnable runnable = RunnableUtil.loopRunnable(() -> {
            IntStream.range(0, RANGE).parallel().forEach(i -> {
                int2Adder.computeIfAbsent(i % MOD, any -> new LongAdder()).add(i);
            });
        }, 10);
        BenchmarkUtil.singleRun(runnable, "compute");
    }

}
