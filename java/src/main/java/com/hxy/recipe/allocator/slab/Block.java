package com.hxy.recipe.allocator.slab;

import lombok.Data;
import lombok.NonNull;

@Data
public class Block implements Comparable<Block> {

    private final long startAddress;
    private final int byteSize;
    private final long endAddress;

    public Block(long startAddress, int byteSize) {
        this.startAddress = startAddress;
        this.byteSize = byteSize;
        this.endAddress = startAddress + byteSize;
    }

    @Override
    public int compareTo(@NonNull Block other) {
        return Long.compare(this.startAddress, other.startAddress);
    }

}
