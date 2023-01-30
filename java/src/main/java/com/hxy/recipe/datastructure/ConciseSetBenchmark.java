package com.hxy.recipe.datastructure;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RandomUtil;
import com.hxy.recipe.util.RunnableUtil;
import it.uniroma3.mat.extendedset.intset.ConciseSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.RamUsageEstimator;

// 10w data: run cost 7ms~9ms
// 1w data:  run cost 0ms~1ms
@Slf4j
public class ConciseSetBenchmark {

    private static final int CARDINALITY = 100_0000;
    private static final int LOOP_TIMES = 1;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        ConciseSet bitMap1 = randomConciseSet();
        ConciseSet bitMap2 = randomConciseSet();
        log.info("construct 2 big bitmap cost {} millis", System.currentTimeMillis() - start);

        log.info("size of 1 is: {}", RamUsageEstimator.humanSizeOf(bitMap1));
        log.info("size of 2 is: {}", RamUsageEstimator.humanSizeOf(bitMap2));

        log.info("bitMap1.cardinality: {}", bitMap1.size());
        log.info("bitMap2.cardinality: {}", bitMap2.size());

        int times = 10_0000;
        for (int i = 0; i < times; i++) {
            benchmark(bitMap1, bitMap2);
        }
    }

    private static void benchmark(ConciseSet bitMap1, ConciseSet bitMap2) {
        long cost = BenchmarkUtil.singleRun(
                RunnableUtil.loopRunnable(
                        () -> bitMap1.intersection(bitMap2),
                        LOOP_TIMES
                )
        );
        log.info("run cost {} millis", cost);
    }

    private static ConciseSet randomConciseSet() {
        ConciseSet conciseSet = new ConciseSet();
        for (int rand : RandomUtil.randomIntArray(CARDINALITY)) {
            conciseSet.add(rand);
        }
        log.info("constructed");
        return conciseSet;
    }

}
