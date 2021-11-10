package com.hxy.recipe.collection;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.LogUtil;
import com.koloboke.collect.map.hash.HashIntIntMap;
import com.koloboke.collect.map.hash.HashIntIntMaps;
import it.unimi.dsi.fastutil.ints.Int2LongMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;

import java.util.Map;

public class FastCollectionStart {

    private static final int WARM_UP_TIMES = 10;
    private static final int TIMES = 100_0000;

    public static void main(String[] args) {
        for (int i = 0; i < WARM_UP_TIMES; i++) {
            run();
        }

        LogUtil.newLine();
        run();
    }

    private static void run() {
        hppc();
        eclipse();
        fastUtil();
        koloboke();
    }

    private static void hppc() {
        com.carrotsearch.hppc.IntLongHashMap hppcMap = new com.carrotsearch.hppc.IntLongHashMap();
        BenchmarkUtil.singleRun(() -> {
            for (int i = 0; i < TIMES; i++) {
                hppcMap.put(i, i);
            }
        }, "hppc-map-put");

        BenchmarkUtil.singleRun(() -> {
            for (int i = 0; i < TIMES; i++) {
                hppcMap.get(i);
            }
        }, "hppc-map-get");
    }

    private static void eclipse() {
        org.eclipse.collections.impl.map.mutable.primitive.IntLongHashMap eclipseMap = new org.eclipse.collections.impl.map.mutable.primitive.IntLongHashMap();
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
        Int2LongMap fastUtilMap = new Int2LongOpenHashMap();
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
        HashIntIntMap kolobokeMap = HashIntIntMaps.newMutableMap();
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
