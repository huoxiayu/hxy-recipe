package com.hxy.algo.eclipsecollection;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.set.mutable.primitive.LongHashSet;

@Slf4j
public class HashSetStart {

    public static void main(String[] args) {
        LongHashSet set1 = new LongHashSet();
        set1.addAll(1L, 2L, 3L);
        log.info("set1 {}", set1);

        LongHashSet set2 = new LongHashSet();
        set2.addAll(2L, 3L, 4L);
        log.info("set2 {}", set2);

        set1.retainAll(set2);

        log.info("set1 {}", set1);
        log.info("set2 {}", set2);
    }

}
