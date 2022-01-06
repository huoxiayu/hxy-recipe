package com.hxy.recipe.performance;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;
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

        run(true);
    }

    private static void run(boolean flag) {
        List<String> strList = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            strList.add(String.valueOf(i));
        }

        Map<String, String> hashMap = new HashMap<>(SIZE);
        long hashMapPutTime = BenchmarkUtil.singleRun(() -> {
            strList.forEach(str -> hashMap.put(str, str));
        });
        Assert.isTrue(hashMap.size() == SIZE, "eq");

        long hashMapGetTime = BenchmarkUtil.singleRun(() -> strList.forEach(hashMap::get));

        Map<String, String> linkedHashMap = new LinkedHashMap<>(SIZE);
        long linkedHashMapPutTime = BenchmarkUtil.singleRun(() -> strList.forEach(str ->
                linkedHashMap.put(str, str))
        );
        Assert.isTrue(linkedHashMap.size() == SIZE, "eq");

        long linkedHashMapGetTime = BenchmarkUtil.singleRun(() -> strList.forEach(linkedHashMap::get));

        if (flag) {
            log.info("hashMapPutTime {}", hashMapPutTime);
            log.info("hashMapGetTime {}", hashMapGetTime);

            log.info("linkedHashMapPutTime {}", linkedHashMapPutTime);
            log.info("linkedHashMapGetTime {}", linkedHashMapGetTime);
        }
    }

}
