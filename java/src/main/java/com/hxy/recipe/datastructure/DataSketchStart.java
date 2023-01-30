package com.hxy.recipe.datastructure;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.datasketches.frequencies.ItemsSketch;
import org.apache.lucene.util.RamUsageEstimator;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataSketchStart {

    public static void main(String[] args) {
        ItemsSketch<String> sketch = new ItemsSketch<>(1 << 16);
        System.out.println("humanSize: " + RamUsageEstimator.humanSizeOf(sketch));

        List<Pair<Integer, String>> timeAndItemPairList = IntStream.rangeClosed(1, 100_000)
                .mapToObj(i -> Pair.of(i, i + "_" + UUID.randomUUID()))
                .collect(Collectors.toList());
        System.out.println("stage 1");

        timeAndItemPairList.forEach(pair -> {
            Integer left = pair.getLeft();
            String right = pair.getRight();
            sketch.update(right, left);
        });
        System.out.println("stage 2");

        timeAndItemPairList.forEach(pair -> {
            String item = pair.getRight();
            if (ThreadLocalRandom.current().nextInt(1000) < 1) {
                System.out.println("item: " + item + " estimate -> " + sketch.getEstimate(item));
            }
        });

        System.out.println("humanSize: " + RamUsageEstimator.humanSizeOf(sketch));
    }

}
