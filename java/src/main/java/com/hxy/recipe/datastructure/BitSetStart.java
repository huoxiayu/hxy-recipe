package com.hxy.recipe.datastructure;

import lombok.extern.slf4j.Slf4j;

import java.util.BitSet;

@Slf4j
public class BitSetStart {

    public static void main(String[] args) {
        BitSet bitSet = new BitSet();
        log.info("bitSet.size(): {}", bitSet.size());   // 64

        bitSet.set(1);
        bitSet.set(1);
        bitSet.set(2);
        log.info("bitSet.size(): {}", bitSet.size());   // 64

        for (int i = 0; i < 64; i++) {
            bitSet.set(i);
        }
        log.info("bitSet.size(): {}", bitSet.size());   // 64

        bitSet.set(64);
        log.info("bitSet.size(): {}", bitSet.size());   // 128
    }

}
