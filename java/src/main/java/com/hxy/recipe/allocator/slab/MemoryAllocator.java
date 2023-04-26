package com.hxy.recipe.allocator.slab;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@ToString
public class MemoryAllocator {

    private static final int ALLOCATE_RETRY_TIMES = 3;

    @Getter
    private final long address;
    @Getter
    private final int byteSize;
    private final int alignSize;
    @Getter
    private final int blockByteSize;
    private final int blockNum;
    private final ConcurrentLinkedQueue<Block> freeBlockList;
    private final double factor;
    private final TreeSet<Integer> slabClassByteSizeSet;
    private final IntObjectHashMap<SlabPool> slabClassByteSize2SlabPool;

    private MemoryAllocator(long address, int byteSize, int blockByteSize, int alignSize, double factor) {
        Preconditions.checkState(address >= 0 && byteSize > 0 && blockByteSize > 0 && alignSize > 0);
        Preconditions.checkState(factor > 0D && factor <= 2D);
        Preconditions.checkState(address % alignSize == 0);
        Preconditions.checkState(byteSize >= blockByteSize && byteSize % blockByteSize == 0 && byteSize % alignSize == 0);
        Preconditions.checkState(blockByteSize > alignSize && blockByteSize % alignSize == 0);
        this.address = address;
        this.byteSize = byteSize;
        this.blockByteSize = blockByteSize;
        this.alignSize = alignSize;
        this.blockNum = byteSize / blockByteSize;
        this.freeBlockList = initBlockList();
        this.factor = factor;
        this.slabClassByteSizeSet = initSlabClassByteSizeSet();
        this.slabClassByteSize2SlabPool = initSlabClassByteSize2SlabPool();
    }

    private ConcurrentLinkedQueue<Block> initBlockList() {
        ConcurrentLinkedQueue<Block> blockList = new ConcurrentLinkedQueue<>();
        long startAddress = this.address;
        for (int i = 0; i < this.blockNum; i++) {
            blockList.add(new Block(startAddress, this.blockByteSize));
            startAddress += this.blockByteSize;
        }
        return blockList;
    }

    private int alignSize(int size, int alignSize) {
        return (size / alignSize + (size % alignSize == 0 ? 0 : 1)) * alignSize;
    }

    private TreeSet<Integer> initSlabClassByteSizeSet() {
        TreeSet<Integer> slabClassSet = new TreeSet<>();
        int slabClassByteSize = this.alignSize;
        while (slabClassByteSize <= this.blockByteSize) {
            slabClassSet.add(slabClassByteSize);
            int nextSlabClass = Math.min((int) Math.ceil(slabClassByteSize * this.factor), slabClassByteSize + 1);
            slabClassByteSize = alignSize(nextSlabClass, this.alignSize);
        }
        return slabClassSet;
    }

    private IntObjectHashMap<SlabPool> initSlabClassByteSize2SlabPool() {
        IntObjectHashMap<SlabPool> slabClassByteSize2SlabPool = new IntObjectHashMap<>();
        for (int slabClassByteSize : this.slabClassByteSizeSet) {
            slabClassByteSize2SlabPool.put(slabClassByteSize, new SlabPool(slabClassByteSize));
        }
        return slabClassByteSize2SlabPool;
    }

    private SlabPool getSlabPool(int byteSize) {
        if (byteSize <= 0 || byteSize > this.blockByteSize) {
            throw new IllegalStateException(String.format(
                    "illegal byte-size: %s, expected size <= blockByteSize: %s",
                    byteSize,
                    this.blockByteSize
            ));
        }
        Integer slabClassByteSize = this.slabClassByteSizeSet.ceiling(byteSize);
        Preconditions.checkState(slabClassByteSize != null);
        return this.slabClassByteSize2SlabPool.get(slabClassByteSize);
    }

    private boolean tryReleaseAnyBlock() {
        for (SlabPool slabPool : this.slabClassByteSize2SlabPool.values()) {
            Block block = slabPool.tryReleaseAnyBlock();
            if (block != null) {
                this.freeBlockList.offer(block);
                return true;
            }
        }

        return false;
    }

    public SlabBlock allocate(int byteSize) {
        while (true) {
            SlabBlock slabBlock = doAllocate(byteSize);
            if (slabBlock != null) {
                return slabBlock;
            }
        }
    }

    private SlabBlock doAllocate(int byteSize) {
        SlabPool slabPool = getSlabPool(byteSize);

        for (int i = 0; i < ALLOCATE_RETRY_TIMES; i++) {
            SlabBlock result = slabPool.allocateSlab();
            if (result != null) {
                return result;
            }

            Block freeBlock = freeBlockList.poll();
            if (freeBlock != null) {
                slabPool.newSlab(freeBlock);
            } else {
                if (!tryReleaseAnyBlock()) {
                    Thread.yield();
                }
            }
        }

        return null;
    }

    public void free(SlabBlock slabBlock) {
        Slab slab = slabBlock.getSlab();
        slab.releaseSlabBlock(slabBlock);
    }

    public static class MemoryAllocatorBuilder {

        private long address;
        private int byteSize;
        private int alignSize;
        private int blockByteSize;
        private double factor;

        MemoryAllocatorBuilder() {
        }

        public MemoryAllocatorBuilder address(final long address) {
            this.address = address;
            return this;
        }

        public MemoryAllocatorBuilder byteSize(final int byteSize) {
            this.byteSize = byteSize;
            return this;
        }

        public MemoryAllocatorBuilder alignSize(final int alignSize) {
            this.alignSize = alignSize;
            return this;
        }

        public MemoryAllocatorBuilder blockByteSize(final int blockByteSize) {
            this.blockByteSize = blockByteSize;
            return this;
        }

        public MemoryAllocatorBuilder factor(final double factor) {
            this.factor = factor;
            return this;
        }

        public MemoryAllocator build() {
            return new MemoryAllocator(
                    this.address,
                    this.byteSize,
                    this.blockByteSize,
                    this.alignSize,
                    this.factor
            );
        }
    }

    public static MemoryAllocatorBuilder builder() {
        return new MemoryAllocatorBuilder();
    }

}