package com.hxy.recipe.allocator.buddyallocator;

public class BuddyAllocatorStart {

    public static void main(String[] args) {
        System.out.println("begin");

        MemoryAllocator ma = new MemoryAllocator(0L, 1024);

        Memory a512 = ma.allocate(512);
        Memory b512 = ma.allocate(512);

        System.out.println("end");
    }

}
