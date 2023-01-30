package com.hxy.recipe.performance;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMapUnsafe;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ConcurrentHashMapUnsafePerformance {

    private static final int NCPU = Runtime.getRuntime().availableProcessors();
    private static final int SIZE = 1000_0000;

    public static void main(String[] args) {
        List<String> strList = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            String str = String.valueOf(i);
            strList.add(str);
        }

        while (true) {
            System.gc();
            Utils.sleepInSeconds(1L);

            ConcurrentHashMapUnsafe<String, String> putIfAbsentMap = new ConcurrentHashMapUnsafe<>(SIZE);
            long putIfAbsentTime = BenchmarkUtil.multiRun(() -> {
                for (String str : strList) {
                    putIfAbsentMap.putIfAbsent(str, str);
                }
            }, NCPU);
            log.info("putIfAbsentTime -> {}", putIfAbsentTime);

            System.gc();
            Utils.sleepInSeconds(1L);

            ConcurrentHashMapUnsafe<String, String> putMap = new ConcurrentHashMapUnsafe<>(SIZE);
            long putTime = BenchmarkUtil.multiRun(() -> {
                for (String str : strList) {
                    putMap.put(str, str);
                }
            }, NCPU);
            log.info("putTime -> {}", putTime);
        }
    }

}
