package com.hxy.recipe.allocator.buddyallocator;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class Memory implements Comparable<Memory> {

    private final long startAddress;
    private final int byteSize;
    private final long endAddress;

    public Memory(long startAddress, int byteSize) {
        Preconditions.checkState(startAddress > 0L);
        Preconditions.checkState(byteSize > 0);
        this.startAddress = startAddress;
        this.byteSize = byteSize;
        this.endAddress = startAddress + byteSize;
    }

    @Override
    public int compareTo(@NotNull Memory other) {
        return Long.compare(this.startAddress, other.startAddress);
    }

}
