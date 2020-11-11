package com.hxy.recipe.datastructure;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RandomUtil;
import it.unimi.dsi.fastutil.ints.Int2IntAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2IntSortedMap;

import java.util.function.Supplier;

/**
 * 简单测试
 * INFO [20/11/09 15:37:37] main com.hxy.recipe.util.RandomUtil:33 [generate random int array cost 737 millis]
 * INFO [20/11/09 15:37:40] main com.hxy.recipe.util.BenchmarkUtil:15 [rbTree cost 1936 millis]
 * INFO [20/11/09 15:37:43] main com.hxy.recipe.util.BenchmarkUtil:15 [avlTree cost 1279 millis]
 * 可以看出avl-tree的性能比rb-tree要更好一点
 */
public class SortedMapPerformance {

    public static void main(String[] args) {
        sortedMapPerformance();
    }

    private static void sortedMapPerformance() {
        int[] randomIntArray = RandomUtil.randomIntArray(100_0000);
        Int2IntSortedMap rbTree = newInt2IntSortedMap(randomIntArray, Int2IntRBTreeMap::new);
        BenchmarkUtil.singleRun(runnable(rbTree, randomIntArray), "rbTree");

        Int2IntSortedMap avlTree = newInt2IntSortedMap(randomIntArray, Int2IntAVLTreeMap::new);
        BenchmarkUtil.singleRun(runnable(avlTree, randomIntArray), "avlTree");
    }

    private static Int2IntSortedMap newInt2IntSortedMap(int[] randomIntArray, Supplier<Int2IntSortedMap> supplier) {
        Int2IntSortedMap int2IntSortedMap = supplier.get();
        for (int rand : randomIntArray) {
            int2IntSortedMap.put(rand, rand);
        }
        return int2IntSortedMap;
    }

    private static Runnable runnable(Int2IntSortedMap int2IntSortedMap, int[] randomIntArray) {
        return () -> {
            for (int rand : randomIntArray) {
                int2IntSortedMap.get(rand);
                int2IntSortedMap.get(rand << 1);
                int2IntSortedMap.get(rand + 1);
                int2IntSortedMap.get(-rand);
            }

            for (Int2IntMap.Entry entry : int2IntSortedMap.int2IntEntrySet()) {
                entry.getIntKey();
                entry.getIntValue();
            }
        };
    }

}
