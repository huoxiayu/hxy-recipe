package com.hxy.recipe.datastructure;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.JvmUtil;
import com.hxy.recipe.util.RunnableUtil;
import lombok.extern.slf4j.Slf4j;
import org.roaringbitmap.FastAggregation;
import org.roaringbitmap.RoaringBitmap;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    private static final int UPPER_BOUND = 3_0000_0000;
    private static final int TIMES = 1_0000_0000;

    public static void main(String[] args) {
        JvmUtil.monitor(5000L);
        long start = System.currentTimeMillis();
        List<RoaringBitmap> bitMapList = IntStream.rangeClosed(1, 5)
            .mapToObj(ignore -> randomRoaringBitMap())
            .collect(Collectors.toList());
        log.info("construct cost {} millis", System.currentTimeMillis() - start);

        long cost = BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(
            () -> FastAggregation.and(bitMapList.iterator()).getCardinality(), 100)
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
        log.info("constructed");
        return roaringBitmap;
    }

}
