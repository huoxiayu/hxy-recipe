package com.hxy.recipe.performance;


import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class RandomPerformance {

    private static final long GAMMA = 0x9e3779b97f4a7c15L;

    // for debug
    public static void main(String[] args) {
        /**
         * @see Random#next(int)
         * 线程安全，但性能不高-----cas
         */
        Random random = new Random();
        log.info("" + random.nextInt());

        /**
         * @see ThreadLocalRandom#nextSeed()
         * 线程安全，性能高-----thread-local
         */
        log.info("" + ThreadLocalRandom.current().nextInt());

        long last = 232L;
        log.info("" + (last + GAMMA));
    }

}
