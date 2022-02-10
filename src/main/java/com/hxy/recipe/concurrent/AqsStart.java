package com.hxy.recipe.concurrent;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class AqsStart {

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock(true);
        BenchmarkUtil.multiRun(() -> {
            boolean tryLock = false;
            try {
                tryLock = lock.tryLock(100, TimeUnit.MILLISECONDS);
                log.info("tryLock: " + tryLock);
                Utils.sleepInSeconds(1L);
            } catch (InterruptedException e) {
                log.error("interrupted: ", e);
            } finally {
                if (tryLock) {
                    lock.unlock();
                }
            }
        });
    }

}
