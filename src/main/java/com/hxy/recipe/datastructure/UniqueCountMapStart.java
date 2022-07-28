package com.hxy.recipe.datastructure;

import org.apache.datasketches.hllmap.UniqueCountMap;
import org.apache.lucene.util.RamUsageEstimator;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class UniqueCountMapStart {

    public static void main(String[] args) {
        UniqueCountMap sketch = new UniqueCountMap(1 << 10, 4);
        System.out.println("humanSize: " + RamUsageEstimator.humanSizeOf(sketch));

        IntStream.rangeClosed(1, 1000).forEach(v -> {
            byte[] k = int2Bytes(v);
            for (int i = 0; i < v; i++) {
                boolean yes = ThreadLocalRandom.current().nextInt(2) < 1;
                String str = yes ? "hxy" : UUID.randomUUID().toString();
                sketch.update(k, str.getBytes(StandardCharsets.UTF_8));
            }
        });

        System.out.println("stage 2");

        IntStream.rangeClosed(1, 1000).forEach(v -> {
            double estimate = sketch.getEstimate(int2Bytes(v));
            System.out.println("item: " + v + " estimate -> " + estimate);
        });

        System.out.println("humanSize: " + RamUsageEstimator.humanSizeOf(sketch));
    }

    private static byte[] int2Bytes(int i) {
        return new byte[]{
                (byte) (i >>> 24 & 0XFF),
                (byte) (i >>> 16 & 0XFF),
                (byte) (i >>> 8 & 0XFF),
                (byte) (i & 0XFF),
        };
    }

}
