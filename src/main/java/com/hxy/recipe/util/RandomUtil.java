package com.hxy.recipe.util;

import lombok.NoArgsConstructor;
import org.eclipse.collections.impl.set.mutable.primitive.IntHashSet;

import java.util.concurrent.ThreadLocalRandom;

@NoArgsConstructor
public final class RandomUtil {

    private static final int EXTEND = 10;

    public static int[] randomIntArray(int cardinality) {
        return randomIntArray(cardinality, cardinality * EXTEND);
    }

    public static int[] randomIntArray(int cardinality, int upperBound) {
        IntHashSet intHashSet = new IntHashSet(cardinality);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int added = 0;
        while (added < cardinality) {
            int rand = random.nextInt(upperBound);
            if (intHashSet.add(rand)) {
                added++;
            }
        }

        return intHashSet.toArray();
    }

}
