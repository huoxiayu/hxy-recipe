package com.hxy.recipe.map;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MapCascade {

    private static final int S1 = 50;
    private static final int S2 = 100;
    private static final int S3 = 5000;

    public static void main(String[] args) {
        //  132 213 231 312 321
        for (int i = 0; i < 5; i++) {
            log.info("i -> {}", i);
            test(50, 500, 500, new ConcurrentHashMap<>(), false);
        }

        Utils.sleepInSeconds(15);

        test(S1, S2, S3, new ConcurrentHashMap<>(), true);
        Utils.sleepInSeconds(3);

        test(S1, S3, S2, new ConcurrentHashMap<>(), true);
        Utils.sleepInSeconds(3);

        test(S2, S1, S3, new ConcurrentHashMap<>(), true);
        Utils.sleepInSeconds(3);

        test(S2, S3, S1, new ConcurrentHashMap<>(), true);
        Utils.sleepInSeconds(3);

        test(S3, S1, S2, new ConcurrentHashMap<>(), true);
        Utils.sleepInSeconds(3);

        test(S3, S2, S1, new ConcurrentHashMap<>(), true);
        Utils.sleepInSeconds(3);
    }

    private static void test(int s1,
                             int s2,
                             int s3,
                             Map<Integer, Map<Integer, Map<Integer, Integer>>> mp123,
                             boolean cost) {
        for (int i = 0; i < s1; i++) {
            for (int j = 0; j < s2; j++) {
                for (int k = 0; k < s3; k++) {
                    mp123.computeIfAbsent(i, any -> new ConcurrentHashMap<>())
                            .computeIfAbsent(j, any -> new ConcurrentHashMap<>())
                            .put(k, 1);
                }
            }
        }

        long start = System.currentTimeMillis();
        BenchmarkUtil.multiRun(() -> {
            for (int i = 0; i < s1; i++) {
                for (int j = 0; j < s2; j++) {
                    for (int k = 0; k < s3; k++) {
                        if (!(mp123.get(i).get(j).get(k) == 1)) {
                            throw new IllegalStateException("error");
                        }
                    }
                }
            }
        });

        long end = System.currentTimeMillis();
        if (cost) {
            log.info("cost {} millis", end - start);
        }
    }

}
