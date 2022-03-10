package com.hxy.recipe.collection;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.LogUtil;
import com.koloboke.collect.map.hash.HashIntIntMap;
import com.koloboke.collect.map.hash.HashIntIntMaps;
import it.unimi.dsi.fastutil.ints.Int2LongMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;

public class FastCollectionStart {

    private static final int WARM_UP_TIMES = 10;
    private static final int TIMES = 1000_0000;

    public static void main(String[] args) {
        for (int i = 0; i < WARM_UP_TIMES; i++) {
            run();
        }

        LogUtil.newLine();
        run();
    }

    private static void run() {
        eclipse();
        fastUtil();
        koloboke();
    }

    private static void eclipse() {
        org.eclipse.collections.impl.map.mutable.primitive.IntLongHashMap eclipseMap = new org.eclipse.collections.impl.map.mutable.primitive.IntLongHashMap(TIMES);
        BenchmarkUtil.singleRun(() -> {
            for (int i = 0; i < TIMES; i++) {
                eclipseMap.put(i, i);
            }
        }, "eclipse-map-put");

        BenchmarkUtil.singleRun(() -> {
            for (int i = 0; i < TIMES; i++) {
                eclipseMap.get(i);
            }
        }, "eclipse-map-get");
    }

    private static void fastUtil() {
        Int2LongMap fastUtilMap = new Int2LongOpenHashMap(TIMES);
        BenchmarkUtil.singleRun(() -> {
            for (int i = 0; i < TIMES; i++) {
                fastUtilMap.put(i, i);
            }
        }, "fast-util-map-put");

        BenchmarkUtil.singleRun(() -> {
            for (int i = 0; i < TIMES; i++) {
                fastUtilMap.get(i);
            }
        }, "fast-util-map-get");
    }

    private static void koloboke() {
        HashIntIntMap kolobokeMap = HashIntIntMaps.newMutableMap(TIMES);
        BenchmarkUtil.singleRun(() -> {
            for (int i = 0; i < TIMES; i++) {
                kolobokeMap.put(i, i);
            }
        }, "koloboke-map-put");

        BenchmarkUtil.singleRun(() -> {
            for (int i = 0; i < TIMES; i++) {
                kolobokeMap.get(i);
            }
        }, "koloboke-map-get");
    }

}
