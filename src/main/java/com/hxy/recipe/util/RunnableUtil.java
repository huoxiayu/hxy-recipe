package com.hxy.recipe.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RunnableUtil {

    private static final int DEFAULT_TIMES = 100_0000;

    public static Runnable loopRunnable(Runnable runnable) {
        return loopRunnable(runnable, DEFAULT_TIMES);
    }

    public static Runnable loopRunnable(Runnable runnable, int loopTimes) {
        return loopTimes <= 1 ? runnable : () -> {
            for (int i = 0; i < loopTimes; i++) {
                runnable.run();
            }
        };
    }

}
