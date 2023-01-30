package com.hxy.recipe.performance;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.RamUsageEstimator;
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMapUnsafe;
import org.eclipse.collections.impl.map.mutable.primitive.IntIntHashMap;
import org.openjdk.jol.info.ClassLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ConcurrentHashMapSize {

    public static void main(String[] args) {
        Map<Integer, Integer> s1 = new ConcurrentHashMap<>();
        Map<Integer, Integer> s2 = new ConcurrentHashMapUnsafe<>();
        Map<Integer, Integer> s3 = new HashMap<>();
        IntIntHashMap s4 = new IntIntHashMap();

        for (int i = 1; i <= 10000; i++) {
            s1.put(i, i);
            s2.put(i, i);
            s3.put(i, i);
            s4.put(i, i);

            boolean print = i < 32 || (i < 1000 && i % 100 == 0) || (i % 1000 == 0);
            if (print) {
                log.info("i -> {}, s1 size -> {}", i, RamUsageEstimator.humanSizeOf(s1));
                log.info("i -> {}, s2 size -> {}", i, RamUsageEstimator.humanSizeOf(s2));
                log.info("i -> {}, s3 size -> {}", i, RamUsageEstimator.humanSizeOf(s3));
                log.info("i -> {}, s4 size -> {}", i, RamUsageEstimator.humanSizeOf(s4));
                printObjectLayout("s1", s1);
                printObjectLayout("s2", s2);
                printObjectLayout("s3", s3);
                printObjectLayout("s4", s4);
            }

            Utils.sleepInSeconds(1L);
        }
    }

    private static void printObjectLayout(String prompt, Object object) {
        log.info(" " + prompt + " with " + ClassLayout.parseInstance(object).toPrintable() + "\n");
    }

}
