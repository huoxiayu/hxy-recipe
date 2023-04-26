package com.hxy.recipe.allocator.buddyallocator;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class MemoryAllocator {

    private final ConcurrentHashMap<Integer, ConcurrentSkipListMap<Long, Memory>> size2StartAddress2Mem;

    public MemoryAllocator(long address, int byteSize) {
        ConcurrentHashMap<Integer, ConcurrentSkipListMap<Long, Memory>> size2StartAddress2Mem = new ConcurrentHashMap<>();
        ConcurrentSkipListMap<Long, Memory> address2Memory = new ConcurrentSkipListMap<>();
        address2Memory.put(address, new Memory(address, byteSize));
        size2StartAddress2Mem.put(byteSize, address2Memory);
        this.size2StartAddress2Mem = size2StartAddress2Mem;
    }

    public Memory allocate(int byteSize) {

    }

    public void free(long address, int byteSize) {

    }

}
