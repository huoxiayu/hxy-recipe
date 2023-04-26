package com.hxy.recipe.allocator;

import com.hxy.recipe.allocator.slab.MemoryAllocator;
import com.hxy.recipe.allocator.slab.SlabBlock;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.LongObjectHashMap;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class MemoryAllocatorStart {

    private static final int CORES = Runtime.getRuntime().availableProcessors();
    private static final LongObjectHashMap<AtomicInteger> MEMORY_DETECTOR = new LongObjectHashMap<>();
    private static final IntObjectHashMap<AtomicReference<SlabBlock>> THREAD_ID_2_SB = new IntObjectHashMap<>();
    private static final int FREE = 0;
    private static final boolean DEBUG = false;

    public static void main(String[] args) {
        MemoryAllocator memoryAllocator = MemoryAllocator.builder()
                .address(0L)
                .byteSize(40 * 1024)
                .blockByteSize(1024)
                .alignSize(64)
                .factor(1.2D)
                .build();
        if (DEBUG) {
            log.info("memoryAllocator -> {}", memoryAllocator);
        }

        for (int x = 1; x <= 100; x++) {
            log.info("{} begin", x);
            run1(memoryAllocator);
            run2(memoryAllocator);
            log.info("{} end", x);
        }
    }

    private static void run1(MemoryAllocator memoryAllocator) {
        int blockByteSize = memoryAllocator.getBlockByteSize();

        int len = 10;
        int[] sizes = new int[len];
        for (int i = 0; i < len; i++) {
            sizes[i] = ThreadLocalRandom.current().nextInt(blockByteSize) + 1;
        }

        sizes = new int[]{258, 941, 15, 173, 410, 90, 471, 574, 555, 628};

        log.info("sizes -> {}", Arrays.toString(sizes));
        for (int size : sizes) {
            SlabBlock sb = memoryAllocator.allocate(size);

            if (DEBUG) {
                log.info("allocate size -> {}, slab-block -> {}", size, sb);
            }

            memoryAllocator.free(sb);

            if (DEBUG) {
                log.info("free size -> {}, slab-block -> {}", size, sb);
                log.info("memoryAllocator -> {}", memoryAllocator);
            }
        }

    }

    private static void run2(MemoryAllocator memoryAllocator) {
        int byteSize = memoryAllocator.getByteSize();
        long address = memoryAllocator.getAddress();
        int blockByteSize = memoryAllocator.getBlockByteSize();

        MEMORY_DETECTOR.clear();
        for (int i = 0; i < byteSize; i++) {
            MEMORY_DETECTOR.put(address + i, new AtomicInteger(0));
        }

        THREAD_ID_2_SB.clear();
        int threadNum = ThreadLocalRandom.current().nextInt(CORES << 2) + 1;
        for (int i = 1; i <= threadNum; i++) {
            THREAD_ID_2_SB.put(i, new AtomicReference<>());
        }

        int times = 100000;
        int step = times / 10;
        List<Thread> threadList = new ArrayList<>();
        for (int i = 1; i <= threadNum; i++) {
            int threadId = i;

            Thread thread = new Thread(() -> {
                ThreadLocalRandom rand = ThreadLocalRandom.current();
                for (int t = 0; t < times; t++) {
                    if (threadId == 1 && t % step == 0) {
                        log.info("times -> {}", t);
                    }

                    int allocateByteSize = rand.nextInt(blockByteSize) + 1;
                    SlabBlock sb = memoryAllocator.allocate(allocateByteSize);

                    int allocatedByteSize = sb.getByteSize();
                    if (allocatedByteSize < allocateByteSize) {
                        exit("allocatedByteSize < allocateByteSize");
                    }

                    AtomicReference<SlabBlock> af = THREAD_ID_2_SB.get(threadId);
                    if (!af.compareAndSet(null, sb)) {
                        exit("!af.compareAndSet(null, sb)");
                    }

                    long startAddress = sb.getStartAddress();
                    for (int x = 0; x < allocatedByteSize; x++) {
                        AtomicInteger cell = MEMORY_DETECTOR.get(startAddress + x);
                        if (!cell.compareAndSet(FREE, threadId)) {
                            int conflictThreadId = cell.get();
                            throw new RuntimeException(String.format(
                                    "repeat allocate! \nthread %s %s is conflict with\nother-thread %s %s",
                                    threadId,
                                    sb,
                                    conflictThreadId,
                                    THREAD_ID_2_SB.get(conflictThreadId).get())
                            );
                        }
                    }
                    for (int x = 0; x < allocatedByteSize; x++) {
                        AtomicInteger cell = MEMORY_DETECTOR.get(startAddress + x);
                        if (!cell.compareAndSet(threadId, FREE)) {
                            int conflictThreadId = cell.get();
                            throw new RuntimeException(String.format(
                                    "repeat allocate! \nthread %s %s is conflict with\nother-thread %s %s",
                                    threadId,
                                    sb,
                                    conflictThreadId,
                                    THREAD_ID_2_SB.get(conflictThreadId).get())
                            );
                        }
                    }

                    if (!af.compareAndSet(sb, null)) {
                        exit("!af.compareAndSet(sb, null)");
                    }

                    memoryAllocator.free(sb);
                }
            });

            thread.start();
            threadList.add(thread);
        }

        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void exit(String prompt) {
        log.error(prompt);
        System.exit(1);
    }

}
