package com.hxy.recipe.allocator.slab;

import com.google.common.base.Preconditions;
import lombok.ToString;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

@ToString
public class SlabPool {

    private static final boolean DESCENDING = true;

    private final int slabClassByteSize;
    private final ConcurrentSkipListMap<Block, Slab> block2Slab;

    public SlabPool(int slabClassByteSize) {
        this.slabClassByteSize = slabClassByteSize;
        this.block2Slab = new ConcurrentSkipListMap<>();
    }

    public void newSlab(Block block) {
        Slab slab = new Slab(block, this.slabClassByteSize);
        this.block2Slab.put(block, slab);
    }

    public SlabBlock allocateSlab() {
        if (this.block2Slab.size() == 0) {
            return null;
        }

        for (Slab slab : this.block2Slab.values()) {
            SlabBlock slabBlock = slab.allocateSlabBlock();
            if (slabBlock != null) {
                return slabBlock;
            }
        }
        return null;
    }

    public Block tryReleaseAnyBlock() {
        int size = this.block2Slab.size();
        if (size == 0) {
            return null;
        }

        Set<Map.Entry<Block, Slab>> entrySet = DESCENDING ?
                this.block2Slab.descendingMap().entrySet() :
                this.block2Slab.entrySet();
        for (Map.Entry<Block, Slab> entry : entrySet) {
            Slab slab = entry.getValue();
            if (slab.tryRelease()) {
                Block block = entry.getKey();
                Slab removeSlab = this.block2Slab.remove(block);
                Preconditions.checkState(removeSlab != null);
                Preconditions.checkState(removeSlab == slab);
                return block;
            }
        }

        return null;
    }

}
