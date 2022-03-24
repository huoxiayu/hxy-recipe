package com.hxy.recipe.concurrent;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CyclicBarrier;

@Slf4j
public class CyclicBarrierStart {

    private static final int CNT = 3;

    public static void main(String[] args) {
        CyclicBarrier cb = new CyclicBarrier(CNT, () -> log.info("trigger"));
        for (int i = 0; i < CNT; i++) {
            new Thread(() -> {
                log.info("before");
                try {
                    cb.await();
                } catch (Exception e) {
                    log.error("error: ", e);
                }
                log.info("after");
            }, "thread-" + (i + 1)).start();

            Utils.sleepInSeconds(1L);
        }
    }

}
