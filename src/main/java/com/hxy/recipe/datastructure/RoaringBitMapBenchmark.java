package com.hxy.recipe.datastructure;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RandomUtil;
import com.hxy.recipe.util.RunnableUtil;
import lombok.extern.slf4j.Slf4j;
import org.roaringbitmap.FastAggregation;
import org.roaringbitmap.RoaringBitmap;

/**
 * 构造roaringbitmap相对较慢，但是进行and/or运算很快
 * [17:01:38][heapMemoryUsed: 27 mb, nonHeapMemoryUsed: 14 mb]
 * [17:01:43][heapMemoryUsed: 69 mb, nonHeapMemoryUsed: 15 mb]
 * [17:01:48][heapMemoryUsed: 136 mb, nonHeapMemoryUsed: 15 mb]
 * [17:01:53][heapMemoryUsed: 173 mb, nonHeapMemoryUsed: 15 mb]
 * [17:02:03][constructed]
 * [17:02:25][constructed]
 * [17:02:33][heapMemoryUsed: 216 mb, nonHeapMemoryUsed: 15 mb]
 * [17:02:48][constructed]
 * [17:02:58][heapMemoryUsed: 219 mb, nonHeapMemoryUsed: 15 mb]
 * [17:03:10][constructed]
 * [17:03:18][heapMemoryUsed: 237 mb, nonHeapMemoryUsed: 15 mb]
 * [17:03:23][heapMemoryUsed: 264 mb, nonHeapMemoryUsed: 15 mb]
 * [17:03:33][constructed]
 * [17:03:33][construct cost 114731 millis]
 * [17:03:38][heapMemoryUsed: 716 mb, nonHeapMemoryUsed: 15 mb]
 * [17:03:42][run cost 8833 millis]
 */
@Slf4j
public class RoaringBitMapBenchmark {

    private static final int CARDINALITY = 1_0000_0000;
    private static final int LOOP_TIMES = 100;

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        RoaringBitmap bitMap1 = randomRoaringBitMap();
        RoaringBitmap bitMap2 = randomRoaringBitMap();
        log.info("construct 2 big bitmap cost {} millis", System.currentTimeMillis() - start);

        log.info("bitMap1.cardinality: {}", bitMap1.getCardinality());
        log.info("bitMap2.cardinality: {}", bitMap2.getCardinality());

        long cost = BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> FastAggregation.and(bitMap1, bitMap2), LOOP_TIMES));
        log.info("run cost {} millis", cost);
    }

    private static RoaringBitmap randomRoaringBitMap() {
        RoaringBitmap roaringBitmap = new RoaringBitmap();
        for (int rand : RandomUtil.randomIntArray(CARDINALITY)) {
            roaringBitmap.add(rand);
        }
        log.info("constructed");
        return roaringBitmap;
    }

}
