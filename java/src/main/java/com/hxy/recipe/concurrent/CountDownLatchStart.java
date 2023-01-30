package com.hxy.recipe.concurrent;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class CountDownLatchStart {

    private static final int CNT = 3;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch cb = new CountDownLatch(CNT);
        for (int i = 0; i < CNT; i++) {
            new Thread(() -> {
                log.info("before");
                cb.countDown();
                log.info("after");
            }, "thread-" + (i + 1)).start();
        }

        log.info("await before");
        cb.await();
        log.info("await after");
    }

}
