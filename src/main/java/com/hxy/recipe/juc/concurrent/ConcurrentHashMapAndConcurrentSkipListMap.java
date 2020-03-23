package com.hxy.recipe.juc.concurrent;

import com.hxy.recipe.util.BenchmarkUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Slf4j
public class ConcurrentHashMapAndConcurrentSkipListMap {

    public static void main(String[] args) {
        int len = 100_0000;
        List<String> stringList = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            stringList.add(UUID.randomUUID().toString());
        }

        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();
        long concurrentHashMapTime = BenchmarkUtil.multiRun(() -> {
            stringList.forEach(string -> concurrentHashMap.put(string, string));
        });
        log.info("concurrentHashMap cost {} millis size {}", concurrentHashMapTime, concurrentHashMap.size());

        ConcurrentSkipListMap<String, String> concurrentSkipListMap = new ConcurrentSkipListMap<>();
        long concurrentSkipListMapTime = BenchmarkUtil.multiRun(() -> {
            stringList.forEach(string -> concurrentSkipListMap.put(string, string));
        });
        log.info("concurrentSkipListMap cost {} millis size {}", concurrentSkipListMapTime, concurrentSkipListMap.size());

    }

}
