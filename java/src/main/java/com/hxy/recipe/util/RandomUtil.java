package com.hxy.recipe.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.set.mutable.primitive.IntHashSet;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@NoArgsConstructor
public final class RandomUtil {

    private static final int EXTEND = 100;

    public static int[] sortedIntArray(int cardinality) {
        int[] intArray = new int[cardinality];
        for (int i = 0; i < cardinality; i++) {
            intArray[i] = i;
        }
        return intArray;
    }

    public static int[] randomIntArray(int cardinality) {
        return randomIntArray(cardinality, 0, cardinality * EXTEND);
    }

    public static int[] randomIntArray(int cardinality, int lowerBound, int upperBound) {
        long start = System.currentTimeMillis();
        IntHashSet intHashSet = new IntHashSet(cardinality);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int added = 0;
        while (added < cardinality) {
            int rand = random.nextInt(upperBound) + lowerBound;
            if (intHashSet.add(rand)) {
                added++;
            }
        }

        int[] intArray = intHashSet.toArray();
        long cost = System.currentTimeMillis() - start;
        log.info("generate random int array cost {} millis", cost);
        return intArray;
    }

}
