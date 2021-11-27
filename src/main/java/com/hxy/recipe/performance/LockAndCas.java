package com.hxy.recipe.performance;

import com.hxy.recipe.util.BenchmarkUtil;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * warm up 1
 * warm up 2
 * warm up 3
 * addCost 0
 * syncCost 409
 * lockCost 14828
 * casCost 4670
 */
public class LockAndCas {

    private static final int TIMES = 10_0000_0000;
    private static final Object LOCK = new Object();

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            System.out.println("warm up " + (i + 1));
            run(false);
        }

        try {
            TimeUnit.SECONDS.sleep(3L);
        } catch (InterruptedException ignore) {
        }

        run(true);
    }

    private static void run(boolean flag) {
        long addCost = BenchmarkUtil.singleRun(() -> {
            int cnt = 0;
            for (int i = 0; i < TIMES; i++) {
                cnt++;
            }
            Assert.isTrue(cnt == TIMES, "should eq");
        });

        long syncCost = BenchmarkUtil.singleRun(() -> {
            int cnt = 0;
            for (int i = 0; i < TIMES; i++) {
                synchronized (LOCK) {
                    cnt++;
                }
            }
            Assert.isTrue(cnt == TIMES, "should eq");
        });

        long lockCost = BenchmarkUtil.singleRun(() -> {
            int cnt = 0;
            ReentrantLock lock = new ReentrantLock();
            for (int i = 0; i < TIMES; i++) {
                lock.lock();
                cnt++;
                lock.unlock();
            }
            Assert.isTrue(cnt == TIMES, "should eq");
        });

        long casCost = BenchmarkUtil.singleRun(() -> {
            AtomicInteger cnt = new AtomicInteger();
            for (int i = 0; i < TIMES; i++) {
                cnt.incrementAndGet();
            }
            Assert.isTrue(cnt.get() == TIMES, "should eq");
        });

        if (flag) {
            System.out.println("addCost " + addCost);
            System.out.println("syncCost " + syncCost);
            System.out.println("lockCost " + lockCost);
            System.out.println("casCost " + casCost);
        }
    }

}
