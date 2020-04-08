package com.hxy.recipe.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RunnableUtil {

    private static final int DEFAULT_TIMES = 100_0000;

    public interface ExceptionRunnable {
        void run() throws Exception;
    }

    private static Runnable deException(ExceptionRunnable exceptionRunnable) {
        return () -> {
            try {
                exceptionRunnable.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static Runnable loopExceptionRunnable(ExceptionRunnable exceptionRunnable) {
        return loopRunnable(deException(exceptionRunnable));
    }

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
