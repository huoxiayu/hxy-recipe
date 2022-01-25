package com.hxy.recipe.performance;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.map.mutable.primitive.IntIntHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC
 */
@Slf4j
public class MapPerformance {

    private static final int SIZE = 1000_0000;

    public static void main(String[] args) {
        run(false);
        run(false);

        Utils.sleepInSeconds(5L);

        while (true) {
            run(true);
        }
    }

    private static void run(boolean flag) {
        List<Integer> intList = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            intList.add(i);
        }

        IntIntHashMap i2iMap = new IntIntHashMap(SIZE);
        long i2iMapPutTime = BenchmarkUtil.singleRun(() -> {
            intList.forEach(i -> i2iMap.put(i, i));
        });
        Assert.isTrue(i2iMap.size() == SIZE, "eq");

        long i2iMapGetTime = BenchmarkUtil.singleRun(() -> intList.forEach(i2iMap::get));

        IntObjectHashMap<Integer> i2IMap = new IntObjectHashMap<>(SIZE);
        long i2IMapPutTime = BenchmarkUtil.singleRun(() -> {
            intList.forEach(i -> i2IMap.put(i, i));
        });
        Assert.isTrue(i2IMap.size() == SIZE, "eq");

        long i2IMapGetTime = BenchmarkUtil.singleRun(() -> intList.forEach(i2IMap::get));

        Map<Integer, Integer> hashMap = new HashMap<>(SIZE);
        long hashMapPutTime = BenchmarkUtil.singleRun(() -> {
            intList.forEach(i -> hashMap.put(i, i));
        });
        Assert.isTrue(hashMap.size() == SIZE, "eq");

        long hashMapGetTime = BenchmarkUtil.singleRun(() -> intList.forEach(hashMap::get));

        Map<Integer, Integer> linkedHashMap = new LinkedHashMap<>(SIZE);
        long linkedHashMapPutTime = BenchmarkUtil.singleRun(() -> intList.forEach(i ->
                linkedHashMap.put(i, i))
        );
        Assert.isTrue(linkedHashMap.size() == SIZE, "eq");

        long linkedHashMapGetTime = BenchmarkUtil.singleRun(() -> intList.forEach(linkedHashMap::get));

        if (flag) {
            log.info("i2iMapPutTime {}", i2iMapPutTime);
            log.info("i2iMapPutTime {}", i2iMapGetTime);

            log.info("i2IMapPutTime {}", i2IMapPutTime);
            log.info("i2IMapGetTime {}", i2IMapGetTime);

            log.info("hashMapPutTime {}", hashMapPutTime);
            log.info("hashMapGetTime {}", hashMapGetTime);

            log.info("linkedHashMapPutTime {}", linkedHashMapPutTime);
            log.info("linkedHashMapGetTime {}", linkedHashMapGetTime);
        }
    }

}
