package com.hxy.recipe.collection;

import lombok.extern.slf4j.Slf4j;

import java.util.IdentityHashMap;

@Slf4j
public class IdentityHashMapStart {

    private static final int NUM = 1_0000_0000;

    public static void main(String[] args) {
        IdentityHashMap<Integer, Integer> iMap = new IdentityHashMap<>();
        for (int i = 0; i < 100; i++) {
            iMap.put(Integer.valueOf(NUM), 1);
        }
        log.info("size -> {}", iMap.size());
        log.info("content -> {}", iMap);
    }

}
