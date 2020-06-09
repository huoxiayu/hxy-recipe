package com.hxy.recipe.datastructure;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RunnableUtil;
import lombok.extern.slf4j.Slf4j;
import org.roaringbitmap.FastAggregation;
import org.roaringbitmap.RoaringBitmap;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class RoaringBitMapBenchmark {

    private static final int UPPER_BOUND = 3_0000_0000;
    private static final int TIMES = 1_0000_0000;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        List<RoaringBitmap> bitMapList = IntStream.rangeClosed(1, 10)
            .mapToObj(ignore -> randomRoaringBitMap())
            .collect(Collectors.toList());
        log.info("construct cost {} millis", System.currentTimeMillis() - start);

        long cost = BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(
            () -> FastAggregation.and(bitMapList.iterator()).getCardinality(), 10000)
        );
        log.info("run cost {} millis", cost);
    }

    private static RoaringBitmap randomRoaringBitMap() {
        return randomRoaringBitMap(TIMES, UPPER_BOUND);
    }

    private static RoaringBitmap randomRoaringBitMap(int times, int upperBound) {
        ThreadLocalRandom current = ThreadLocalRandom.current();
        RoaringBitmap roaringBitmap = new RoaringBitmap();
        for (int i = 0; i < times; i++) {
            roaringBitmap.add(current.nextInt(upperBound));
        }
        return roaringBitmap;
    }

}
