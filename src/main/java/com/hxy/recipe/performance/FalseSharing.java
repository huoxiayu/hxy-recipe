package com.hxy.recipe.performance;

import com.hxy.recipe.util.LogUtil;
import com.hxy.recipe.util.RunnableUtil;
import com.hxy.recipe.util.Utils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * lazySet is faster then set
 *
 * @see AtomicReferenceArray#lazySet
 * @see java.lang.invoke.VarHandle#setRelease
 */
@Slf4j
public class FalseSharing {

    public static void main(String[] args) {
        int len = 128;
        int concurrent = 8;
        int step1 = 1;
        int step2 = 16;
        while (true) {
            run(len, concurrent, step1);
            run(len, concurrent, step2);
            Utils.sleepInSeconds(1L);
            LogUtil.newLine();
        }
    }

    @SneakyThrows
    private static void run(int len, int concurrent, int step) {
        System.gc();

        AtomicReferenceArray<Bean> ara = new AtomicReferenceArray<>(len * step);
        String prefix = UUID.randomUUID().toString();
        ExecutorService executor = Utils.newExecutors(prefix, concurrent);

        int batch = len / concurrent;
        Bean bean = new Bean(1, "x");
        CountDownLatch cdl = new CountDownLatch(concurrent);
        CyclicBarrier cb = new CyclicBarrier(concurrent);
        long start = System.currentTimeMillis();
        for (int i = 0; i < concurrent; i++) {
            int fi = i;
            executor.execute(() -> {
                Runnable run = RunnableUtil.loopRunnable(() -> {
                    for (int j = 0; j < batch; j++) {
                        int idx = step * (j * concurrent + fi);
                        // log.info("set idx -> {}", idx);
                        ara.set(idx, bean);
                        // ara.lazySet(idx, bean);
                    }
                });
                try {
                    cb.await();
                    run.run();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    cdl.countDown();
                }
            });
        }

        cdl.await();
        long cost = System.currentTimeMillis() - start;
        log.info(
                "len -> {}, concurrent -> {}, step -> {}, cost -> {} millis",
                len,
                concurrent,
                step,
                cost
        );

        executor.shutdown();
    }

    @AllArgsConstructor
    static class Bean {
        int a;
        String b;
    }

}
