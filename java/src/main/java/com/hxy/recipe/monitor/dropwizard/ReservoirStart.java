package com.hxy.recipe.monitor.dropwizard;

import com.codahale.metrics.ExponentiallyDecayingReservoir;
import com.codahale.metrics.Reservoir;
import com.codahale.metrics.SlidingTimeWindowArrayReservoir;
import com.codahale.metrics.SlidingTimeWindowReservoir;
import com.codahale.metrics.SlidingWindowReservoir;
import com.codahale.metrics.Snapshot;
import com.hxy.recipe.util.LogUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @see com.codahale.metrics.EWMA
 * @see Reservoir
 * 蓄水池算法，以极低的开销统计无限数据流的数据分布情况
 * 如均值、中位数、p75、p95分布的数值等
 * reference:
 *
 *
 * @see <a href="https://www.helpsystems.com/resources/guides/unix-load-average-part-1-how-it-works">part1</a>
 * @see <a href="https://www.helpsystems.com/resources/guides/unix-load-average-part-2-not-your-average-average">part2</a>
 */
@Slf4j
public class ReservoirStart {

    private static final int DATA_SET_SIZE = 100_0000;
    private static final List<Integer> DATA_SET = new ArrayList<>(DATA_SET_SIZE);

    static {
        for (int i = 0; i < DATA_SET_SIZE; i++) {
            DATA_SET.add(i);
        }
        Collections.shuffle(DATA_SET);
    }

    public static void main(String[] args) {
        update(new ExponentiallyDecayingReservoir());
        LogUtil.newLine();

        update(new SlidingWindowReservoir(1000));
        LogUtil.newLine();

        update(new SlidingTimeWindowReservoir(100L, TimeUnit.MILLISECONDS));
        LogUtil.newLine();

        update(new SlidingTimeWindowArrayReservoir(100L, TimeUnit.MILLISECONDS));
    }

    private static void update(Reservoir reservoir) {
        for (int round = 0; round < 10; round++) {
            for (int v : DATA_SET) {
                reservoir.update(v);
            }
        }

        Snapshot snapshot = reservoir.getSnapshot();
        log.info("{} min {}", reservoir.getClass().getSimpleName(), snapshot.getMin());
        log.info("{} mean {}", reservoir.getClass().getSimpleName(), snapshot.getMean());
        log.info("{} p75 {}", reservoir.getClass().getSimpleName(), snapshot.get75thPercentile());
        log.info("{} p95 {}", reservoir.getClass().getSimpleName(), snapshot.get95thPercentile());
        log.info("{} p99 {}", reservoir.getClass().getSimpleName(), snapshot.get99thPercentile());
        log.info("{} p999 {}", reservoir.getClass().getSimpleName(), snapshot.get999thPercentile());
    }

}
