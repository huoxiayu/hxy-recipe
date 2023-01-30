package com.hxy.recipe.concurrent;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RandomUtil;
import com.hxy.recipe.util.RunnableUtil;
import com.hxy.recipe.util.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.IntStream;

@Slf4j
public class ConcurrentSkipListStart {

    @AllArgsConstructor
    private static class Bean {
        private final int dummy;

        public static Bean newBean() {
            int dummy = 0;
            for (int i = 1; i < 10000; i++) {
                dummy += i;
            }

            return new Bean(dummy);
        }
    }

    public static void main(String[] args) {
        int size = 100_0000;
        int[] randomIntArray = RandomUtil.randomIntArray(size);

        RunnableUtil.loopRunnable(() -> {
            run(new ConcurrentHashMap<>(), randomIntArray);
            run(new ConcurrentSkipListMap<>(), randomIntArray);
        }, 10);

        Utils.sleepInSeconds(10L);

        BenchmarkUtil.singleRun(run(new ConcurrentHashMap<>(), randomIntArray), "concurrent hash map");
        BenchmarkUtil.singleRun(run(new ConcurrentSkipListMap<>(), randomIntArray), "concurrent skip list");
    }

    private static Runnable run(ConcurrentMap<Integer, Bean> map, int[] input) {
        return () -> IntStream.range(0, Utils.CORES).parallel().forEach(any1 -> {
            for (int i = 0; i < input.length; i++) {
                map.computeIfAbsent(input[i], any2 -> Bean.newBean());
            }
        });
    }

}
