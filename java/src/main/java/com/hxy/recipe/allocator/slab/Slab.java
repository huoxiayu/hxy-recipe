package com.hxy.recipe.allocator.slab;

import io.netty.util.collection.IntObjectHashMap;
import lombok.ToString;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

@ToString(of = {"startAddress", "byteSize", "slabClassByteSize", "size"})
public class Slab {

    private static final long NANO_SECONDS_2_MICRO_SECONDS = 1000L;
    private static final long RELEASE_TIME_IN_MICRO_SECONDS = 50L;
    private static final int RELEASE_FLAG = -1;

    private final long startAddress;
    private final int byteSize;
    private final int slabClassByteSize;
    private final int size;
    private final int bucketNum;
    private final IntObjectHashMap<ConcurrentLinkedDeque<SlabBlock>> bucketId2Pool;
    private final AtomicInteger bucketIdx = new AtomicInteger(0);
    private final AtomicInteger borrowCount = new AtomicInteger(0);
    private final long createTimeInMicroSeconds = currentMicroSeconds();

    private static long currentMicroSeconds() {
        return System.nanoTime() / NANO_SECONDS_2_MICRO_SECONDS;
    }

    public Slab(Block block, int slabClassByteSize) {
        this.startAddress = block.getStartAddress();
        this.byteSize = block.getByteSize();
        this.slabClassByteSize = slabClassByteSize;
        this.size = block.getByteSize() / this.slabClassByteSize;
        this.bucketNum = Math.min(Runtime.getRuntime().availableProcessors(), this.size);
        this.bucketId2Pool = initBucketId2Pool();
    }

    private int nextBucketIdx() {
        return Math.abs(bucketIdx.getAndIncrement()) % this.bucketNum;
    }

    private IntObjectHashMap<ConcurrentLinkedDeque<SlabBlock>> initBucketId2Pool() {
        IntObjectHashMap<ConcurrentLinkedDeque<SlabBlock>> map = new IntObjectHashMap<>();
        for (int i = 0; i < this.bucketNum; i++) {
            map.put(i, new ConcurrentLinkedDeque<>());
        }
        long startAddress = this.startAddress;
        for (int i = 0; i < this.size; i++) {
            int bucketId = nextBucketIdx();
            ConcurrentLinkedDeque<SlabBlock> pool = map.get(bucketId);
            int offset = i * this.slabClassByteSize;
            pool.offer(new SlabBlock(bucketId, this, startAddress + offset, this.slabClassByteSize));
        }
        return map;
    }

    public boolean tryRelease() {
        return currentMicroSeconds() - this.createTimeInMicroSeconds > RELEASE_TIME_IN_MICRO_SECONDS &&
                this.borrowCount.compareAndSet(0, RELEASE_FLAG);
    }

    public SlabBlock allocateSlabBlock() {
        while (true) {
            int borrowCount = this.borrowCount.get();
            if (borrowCount < 0) {
                return null;
            }
            if (this.borrowCount.compareAndSet(borrowCount, borrowCount + 1)) {
                break;
            }
        }

        int bucketId = nextBucketIdx();
        ConcurrentLinkedDeque<SlabBlock> deque = this.bucketId2Pool.get(bucketId);
        SlabBlock slabBlock = deque.poll();
        if (slabBlock == null) {
            this.borrowCount.decrementAndGet();
        }
        return slabBlock;
    }

    public void releaseSlabBlock(SlabBlock slabBlock) {
        int bucketId = slabBlock.getBucketId();
        ConcurrentLinkedDeque<SlabBlock> deque = this.bucketId2Pool.get(bucketId);
        deque.offer(slabBlock);
        this.borrowCount.decrementAndGet();
    }

}
