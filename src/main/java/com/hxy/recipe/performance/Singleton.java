package com.hxy.recipe.performance;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RunnableUtil;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class Singleton {

    private static final int TIMES = 1_0000_0000;

    public static void main(String[] args) {
        while (true) {
            long c1 = BenchmarkUtil.multiRun(
                    RunnableUtil.loopRunnable(
                            C1::get,
                            TIMES
                    )
            );
            log.info("c1 -> {}", c1);

            long c2 = BenchmarkUtil.multiRun(
                    RunnableUtil.loopRunnable(
                            C2::get,
                            TIMES
                    )
            );
            log.info("c2 -> {}", c2);

            long c3 = BenchmarkUtil.multiRun(
                    RunnableUtil.loopRunnable(
                            C3::get,
                            TIMES
                    )
            );
            log.info("c3 -> {}", c3);

            Utils.sleepInSeconds(1L);
        }
    }

    private static class C1 {
        private static volatile Bean B;

        public static Bean get() {
            if (B == null) {
                synchronized (C1.class) {
                    if (B == null) {
                        B = new Bean();
                    }
                }
            }

            return B;
        }
    }

    private static class C2 {
        private static final AtomicReference<Bean> ref = new AtomicReference<>();

        public static Bean get() {
            Bean B;
            if ((B = ref.getAcquire()) == null) {
                synchronized (C2.class) {
                    if ((B = ref.getAcquire()) == null) {
                        B = new Bean();
                        ref.setRelease(B);
                    }
                }
            }

            return B;
        }

    }

    private static class C3 {
        private static final Bean B = new Bean();

        public static Bean get() {
            return B;
        }

    }

    private static class Bean {

    }

}
