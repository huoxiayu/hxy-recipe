package com.hxy.recipe.allocator.slab;

import lombok.Data;

@Data
public class SlabBlock {

    private final int bucketId;
    private final Slab slab;
    private final long startAddress;
    private final int byteSize;
    private final long endAddress;

    public SlabBlock(int bucketId, Slab slab, long startAddress, int byteSize) {
        this.bucketId = bucketId;
        this.slab = slab;
        this.startAddress = startAddress;
        this.byteSize = byteSize;
        this.endAddress = startAddress + byteSize;
    }

}
