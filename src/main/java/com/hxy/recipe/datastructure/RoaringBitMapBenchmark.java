package com.hxy.recipe.datastructure;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RandomUtil;
import com.hxy.recipe.util.RunnableUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.RamUsageEstimator;
import org.roaringbitmap.FastAggregation;
import org.roaringbitmap.RoaringBitmap;

/**
 * [100 rbm size is: 424 bytes]
 * [1000 rbm size is: 2.6 KB]
 * [10000 rbm size is: 22.7 KB]
 * [100000 rbm size is: 225.4 KB]
 * [1000000 rbm size is: 2.2 MB]
 * [10000000 rbm size is: 22 MB]
 */
// 100w data: run cost 12ms~20ms
// 10w data:  run cost 1ms~2ms
// 1w data:   run cost 0ms~1ms
@Slf4j
public class RoaringBitMapBenchmark {

    private static final int CARDINALITY = 100_0000;
    private static final int LOOP_TIMES = 1;

    public static void main(String[] args) {
        size();
        performance();
    }

    private static void size() {
        int[] cs = {100, 1000, 1_0000, 10_0000, 100_0000, 1000_0000, 1_0000_0000};
        for (int cardinality : cs) {
            String humanSize = RamUsageEstimator.humanSizeOf(randomRoaringBitMap(cardinality));
            log.info("{} rbm size is: {}", cardinality, humanSize);
        }
    }

    private static void performance() {
        long start = System.currentTimeMillis();
        RoaringBitmap bitMap1 = randomRoaringBitMap(CARDINALITY);
        RoaringBitmap bitMap2 = randomRoaringBitMap(CARDINALITY);
        log.info("construct 2 big bitmap cost {} millis", System.currentTimeMillis() - start);

        log.info("size of 1 is: {}", RamUsageEstimator.humanSizeOf(bitMap1));
        log.info("size of 2 is: {}", RamUsageEstimator.humanSizeOf(bitMap2));

        log.info("bitMap1.cardinality: {}", bitMap1.getCardinality());
        log.info("bitMap2.cardinality: {}", bitMap2.getCardinality());

        int times = 10_0000;
        for (int i = 0; i < times; i++) {
            benchmark(bitMap1, bitMap2);
        }
    }

    private static void benchmark(RoaringBitmap bitMap1, RoaringBitmap bitMap2) {
        long cost = BenchmarkUtil.singleRun(
                RunnableUtil.loopRunnable(
                        () -> FastAggregation.and(bitMap1, bitMap2),
                        LOOP_TIMES
                )
        );
        log.info("run cost {} millis", cost);
    }

    private static RoaringBitmap randomRoaringBitMap(int cardinality) {
        RoaringBitmap roaringBitmap = new RoaringBitmap();
        for (int rand : RandomUtil.randomIntArray(cardinality)) {
            roaringBitmap.add(rand);
        }
        log.info("constructed");
        return roaringBitmap;
    }

}
