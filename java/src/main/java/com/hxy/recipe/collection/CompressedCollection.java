package com.hxy.recipe.collection;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.RamUsageEstimator;
import org.eclipse.collections.impl.map.mutable.primitive.AbstractMutableDoubleValuesMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntDoubleHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Slf4j
public class CompressedCollection {

    private static final int K1 = 10;
    private static final int K2 = 10;
    private static final int K3 = 10;
    private static final int K4 = 10;

    private static final int K1_BASE = 1;
    private static final int K2_BASE = K1_BASE * K1;
    private static final int K3_BASE = K2_BASE * K2;
    private static final int K4_BASE = K3_BASE * K3;

    public static void main(String[] args) {
        process();
    }

    private static void process() {
        IntDoubleHashMap k1234v1 = new IntDoubleHashMap();
        IntObjectHashMap<IntObjectHashMap<IntObjectHashMap<IntDoubleHashMap>>> k1k2k3k4v1 = new IntObjectHashMap<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        IntStream.range(0, K1).forEach(k1 -> {
            int k_1 = random.nextInt(K1);
            IntStream.range(0, K2).forEach(k2 -> {
                int k_2 = random.nextInt(K2);
                IntStream.range(0, K3).forEach(k3 -> {
                    int k_3 = random.nextInt(K3);
                    IntStream.range(0, K4).forEach(k4 -> {
                        int k_4 = random.nextInt(K4);
                        k1k2k3k4v1
                                .getIfAbsentPut(k_1, IntObjectHashMap::new)
                                .getIfAbsentPut(k_2, IntObjectHashMap::new)
                                .getIfAbsentPut(k_3, IntDoubleHashMap::new)
                                .put(k_4, 1D);
                        k1234v1.put(k_1 * K1_BASE + k_2 * K2_BASE + k_3 * K3_BASE + k_4 * K4_BASE, 1D);
                    });
                });
            });
        });

        int size1 = k1k2k3k4v1.values()
                .stream()
                .mapToInt(t1 -> t1.values()
                        .stream()
                        .mapToInt(t2 -> t2.values()
                                .stream()
                                .mapToInt(AbstractMutableDoubleValuesMap::size)
                                .sum())
                        .sum())
                .sum();

        log.info("k1k2k3k4v1.size() -> {}", size1);
        log.info("k1k2k3k4v1.humanSize() -> {}", RamUsageEstimator.humanSizeOf(k1k2k3k4v1));

        log.info("k1234v1.size() -> {}", k1234v1.size());
        log.info("k1234v1.humanSize() -> {}", RamUsageEstimator.humanSizeOf(k1234v1));

        Utils.sleepInSeconds(100L);
    }

}
