package com.hxy.recipe.memory;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.util.RamUsageEstimator;

@Slf4j
public class ObjectMemoryStart {

    private static class IntClass {
        private int useless;
    }

    private static class IntegerClass {
        private Integer useless;
    }

    public static void main(String[] args) {
        log.info("size of a object {}", RamUsageEstimator.sizeOf(new Object()));
        log.info("size of a integer {}", RamUsageEstimator.sizeOf(100000));
        log.info("size of a object[] {}", RamUsageEstimator.sizeOf(new Object[10]));
        log.info("size of a int[] {}", RamUsageEstimator.sizeOf(new IntClass[10]));
        log.info("size of a int[] {}", RamUsageEstimator.sizeOf(new IntegerClass[10]));
    }

}
