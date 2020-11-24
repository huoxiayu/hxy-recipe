package com.hxy.recipe.datastructure.hyperloglog;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.hxy.recipe.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import net.agkn.hll.HLL;
import org.apache.lucene.util.RamUsageEstimator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class NetHyperLogLogStart {

    private static final int LOG_2_M = 14;
    private static final int REG_WIDTH = 5;
    private static final int SEED = 147852369;

    public static void main(String[] args) {
        int size = 100_0000;
        int[] sortedIntArray = RandomUtil.sortedIntArray(size);

        HLL hll1 = new HLL(LOG_2_M, REG_WIDTH);
        Arrays.stream(sortedIntArray).filter(n -> n % 2 == 0).forEach(hll1::addRaw);
        log.info("hll1 {}", hll1.cardinality());

        HLL hll2 = new HLL(LOG_2_M, REG_WIDTH);

        Arrays.stream(sortedIntArray).filter(n -> n % 2 == 1).forEach(hll2::addRaw);
        log.info("hll2 {}", hll2.cardinality());

        hll1.union(hll2);
        log.info("hll1 after merge {}", hll1.cardinality());

        hll();
    }

    private static void hll() {
        HLL hll1 = generateHLL(10_0000);
        HLL hll2 = generateHLL(10_0000);

        hll1.union(hll2);
        log.info("union cardinality {}", hll1.cardinality());
        log.info("union size {}", RamUsageEstimator.humanSizeOf(hll1));

        HLL bigHll = generateHLL(1_0000_0000);
        log.info("bigHll cardinality {}", bigHll.cardinality());
        log.info("bigHll size {}", RamUsageEstimator.humanSizeOf(bigHll));

        HLL bigBigHll = generateHLL(2_0000_0000);
        log.info("bigBigHll cardinality {}", bigBigHll.cardinality());
        log.info("bigBigHll size {}", RamUsageEstimator.humanSizeOf(bigBigHll));
    }

    private static HLL generateHLL(int cardinality) {
        int[] nums = RandomUtil.sortedIntArray(cardinality);

        long start = System.currentTimeMillis();
        HLL hll = new HLL(LOG_2_M, REG_WIDTH);
        HashFunction hashFunc = Hashing.murmur3_128(SEED);
        for (int num : nums) {
            long hash = hashFunc.newHasher().putInt(num).hash().asLong();
            hll.addRaw(hash);
        }

        log.info("hll cardinality {}, generate cost {} millis", hll.cardinality(), System.currentTimeMillis() - start);
        return hll;
    }

}
