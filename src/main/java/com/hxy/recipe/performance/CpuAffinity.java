package com.hxy.recipe.performance;

import net.openhft.affinity.AffinityLock;
import net.openhft.affinity.AffinityStrategies;
import net.openhft.affinity.AffinityThreadFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

// cpu亲和性（绑核）
public class CpuAffinity {

    private static final long SECONDS = 5L;
    private static final long DURATION = TimeUnit.SECONDS.toMillis(SECONDS);
    private static final int PROCESSORS = 4;

    public static void main(String[] args) {
        IntStream.rangeClosed(1, PROCESSORS)
                .forEach(i -> new Thread(() -> {
                    cpuBoundCompute();
                    cpuBoundCompute();
                    cpuBoundCompute();
                    System.out.println("Thread-" + i + "-end");
                }).start());

        int cpuId = 1;
        try (AffinityLock affinityLock = AffinityLock.acquireLock(cpuId)) {
            System.out.println("cpuId -> " + affinityLock.cpuId());
            System.out.println("affinity-cnt -> " + cpuBoundCompute());
        }

        System.out.println("non-affinity-cnt -> " + cpuBoundCompute());

        ExecutorService normalExecutor = Executors.newFixedThreadPool(PROCESSORS);
        executorPerformance(normalExecutor);

        ExecutorService affinityExecutor = Executors.newFixedThreadPool(
                PROCESSORS,
                new AffinityThreadFactory(
                        "affinity-thread-factory-",
                        AffinityStrategies.DIFFERENT_CORE
                )
        );
        executorPerformance(affinityExecutor);

        System.out.println("affinity-thread-pool-cnt -> " + executorPerformance(affinityExecutor));
        System.out.println("non-affinity-thread-pool-cnt -> " + executorPerformance(normalExecutor));

        System.exit(0);
    }

    private static long cpuBoundCompute() {
        long start = System.currentTimeMillis();
        long cnt = 0L;
        do {
            for (int i = 0; i < 10000; i++) {
                cnt++;
            }
        } while (System.currentTimeMillis() - start < DURATION);
        return cnt / 1_0000_0000L;
    }

    private static long executorPerformance(Executor executor) {
        AtomicLong cnt = new AtomicLong();
        CompletableFuture.allOf(IntStream.rangeClosed(1, PROCESSORS)
                .mapToObj(i -> CompletableFuture.runAsync(
                        () -> cnt.addAndGet(cpuBoundCompute()),
                        executor
                ))
                .toArray(CompletableFuture[]::new))
                .join();
        return cnt.get();
    }

}
