package com.hxy.recipe.datastructure;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.hxy.recipe.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import net.agkn.hll.HLL;

@Slf4j
public class HyperLogLogStart {

    private static final int LOG_2_M = 14;
    private static final int REG_WIDTH = 5;
    private static final int SEED = 147852369;

    public static void main(String[] args) {
        HLL hll1 = generateHLL(10_0000, 0, 100_0000);
        HLL hll2 = generateHLL(10_0000, 100_0000, 200_0000);

        hll1.union(hll2);
        log.info("union cardinality {}", hll1.cardinality());
    }

    private static HLL generateHLL(int cardinality, int lowerBound, int upperBound) {
        int[] nums = RandomUtil.randomIntArray(cardinality, lowerBound, upperBound);

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
