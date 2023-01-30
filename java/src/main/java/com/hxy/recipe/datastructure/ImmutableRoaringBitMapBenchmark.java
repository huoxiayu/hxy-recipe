package com.hxy.recipe.datastructure;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RandomUtil;
import com.hxy.recipe.util.RunnableUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.RamUsageEstimator;
import org.roaringbitmap.buffer.BufferFastAggregation;
import org.roaringbitmap.buffer.ImmutableRoaringBitmap;

@Slf4j
public class ImmutableRoaringBitMapBenchmark {

    private static final int CARDINALITY = 100_0000;
    private static final int LOOP_TIMES = 1;

    public static void main(String[] args) {
        size();
        performance();
    }

    private static void size() {
        int[] cs = {100, 1000, 1_0000, 10_0000, 100_0000, 1000_0000, 1_0000_0000};
        for (int cardinality : cs) {
            String humanSize = RamUsageEstimator.humanSizeOf(
                    randomRoaringBitMap(cardinality)
            );
            log.info("{} rbm size is: {}", cardinality, humanSize);
        }
    }

    private static void performance() {
        long start = System.currentTimeMillis();
        ImmutableRoaringBitmap bitMap1 = randomRoaringBitMap(CARDINALITY);
        ImmutableRoaringBitmap bitMap2 = randomRoaringBitMap(CARDINALITY);
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

    private static void benchmark(ImmutableRoaringBitmap bitMap1,
                                  ImmutableRoaringBitmap bitMap2) {
        long cost = BenchmarkUtil.singleRun(
                RunnableUtil.loopRunnable(
                        () -> BufferFastAggregation.and(bitMap1, bitMap2),
                        LOOP_TIMES
                )
        );
        log.info("run cost {} millis", cost);
    }

    private static ImmutableRoaringBitmap randomRoaringBitMap(int cardinality) {
        ImmutableRoaringBitmap roaringBitmap = ImmutableRoaringBitmap.bitmapOf(
                RandomUtil.randomIntArray(cardinality)
        );
        log.info("constructed");
        return roaringBitmap;
    }

}
