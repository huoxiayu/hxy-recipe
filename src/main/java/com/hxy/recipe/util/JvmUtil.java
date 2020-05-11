package com.hxy.recipe.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JvmUtil {

    private static final ScheduledExecutorService SCHEDULER = Utils.newSingleScheduledExecutors("jvm-monitor");
    private static final long KB = 1024;
    private static final long MB = 1024 * KB;

    private static final AtomicLong MAX_HEAP_MEMORY_USED = new AtomicLong();
    private static final AtomicLong MAX_NON_HEAP_MEMORY_USED = new AtomicLong();

    public static void monitor(long periodInMillis) {
        SCHEDULER.scheduleAtFixedRate(() -> {
            boolean print = false;
            long heapMemoryUsed = getHeapMemoryUsageInMb();
            if (heapMemoryUsed > MAX_HEAP_MEMORY_USED.get()) {
                MAX_HEAP_MEMORY_USED.set(heapMemoryUsed);
                print = true;
            }
            long nonHeapMemoryUsed = getNonHeapMemoryUsageInMb();
            if (nonHeapMemoryUsed > MAX_NON_HEAP_MEMORY_USED.get()) {
                MAX_NON_HEAP_MEMORY_USED.set(nonHeapMemoryUsed);
                print = true;
            }
            if (print) {
                log.info("heapMemoryUsed: {} mb, nonHeapMemoryUsed: {} mb", heapMemoryUsed, nonHeapMemoryUsed);
            }
        }, 0L, periodInMillis, TimeUnit.MILLISECONDS);
    }

    private static long getHeapMemoryUsageInMb() {
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / MB;
    }

    private static long getNonHeapMemoryUsageInMb() {
        return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed() / MB;
    }

}
