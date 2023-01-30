package com.hxy.recipe.datastructure;

import com.hxy.recipe.util.RandomUtil;
import com.liveramp.hyperminhash.BetaMinHash;
import com.liveramp.hyperminhash.BetaMinHashCombiner;
import com.liveramp.hyperminhash.SketchCombiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.RamUsageEstimator;

import java.util.List;

@Slf4j
public class HyperMinHashStart {

    private static final int CARDINALITY = 10_0000;

    public static void main(String[] args) {
        BetaMinHash sketch1 = toBetaMinHash(CARDINALITY);
        long cardinality1 = sketch1.cardinality();
        log.info("cardinality1 -> {}", cardinality1);
        log.info("size of 1 -> {}", RamUsageEstimator.humanSizeOf(sketch1));

        BetaMinHash sketch2 = toBetaMinHash(CARDINALITY);
        long cardinality2 = sketch2.cardinality();
        log.info("cardinality2 -> {}", cardinality2);
        log.info("size of 2 -> {}", RamUsageEstimator.humanSizeOf(sketch2));

        SketchCombiner<BetaMinHash> combiner = BetaMinHashCombiner.getInstance();
        List<BetaMinHash> sketchList = List.of(sketch1, sketch2);
        for (int i = 0; i < 10_000; i++) {
            performance(combiner, sketchList);
        }
    }

    private static BetaMinHash toBetaMinHash(int cardinality) {
        BetaMinHash sketch = new BetaMinHash();
        for (int element : RandomUtil.randomIntArray(cardinality)) {
            sketch.offer(new byte[]{
                    (byte) (255 & element >> 24),
                    (byte) (255 & element >> 16),
                    (byte) (255 & element >> 8),
                    (byte) (255 & element)
            });
        }

        return sketch;
    }

    private static void performance(SketchCombiner<BetaMinHash> combiner,
                                    List<BetaMinHash> sketchList) {
        long start = System.currentTimeMillis();
        long intersectionCardinality = combiner.intersectionCardinality(
                sketchList
        );
        long cost = System.currentTimeMillis() - start;
        log.info("intersectionCardinality -> {}", intersectionCardinality);
        log.info("cost.in.millis -> {}", cost);
    }

}
