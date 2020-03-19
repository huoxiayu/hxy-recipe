package com.hxy.recipe.juc.atomic;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RunnableUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * win10-8core
 * single longAdder cost 16 millis, value 1000000
 * single atomicLong cost 15 millis, value 1000000
 * single lock cost 31 millis, value 1000000
 * single sync cost 0 millis, value 1000000
 * multi longAdder cost 32 millis, value 8000000
 * multi atomicLong cost 140 millis, value 8000000
 * multi lock cost 219 millis, value 8000000
 * multi sync cost 266 millis, value 8000000
 */
@Slf4j
public class LongAdderAndAtomicLongAndSynchronizedAndLock {

    public static void main(String[] args) {
        single();
        multi();
    }

    private static void single() {
        LongAdder longAdder = new LongAdder();
        long longAdderCost = BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(longAdder::increment));
        log.info("single longAdder cost {} millis, value {}", longAdderCost, longAdder.sum());

        AtomicLong atomicLong = new AtomicLong();
        long atomicCost = BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(atomicLong::incrementAndGet));
        log.info("single atomicLong cost {} millis, value {}", atomicCost, atomicLong.get());

        Cnt lockCnt = new Cnt();
        long lockCost = BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(lockCnt::lockInc));
        log.info("single lock cost {} millis, value {}", lockCost, lockCnt.getCnt());

        Cnt syncCnt = new Cnt();
        long syncCost = BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(syncCnt::syncInc));
        log.info("single sync cost {} millis, value {}", syncCost, syncCnt.getCnt());
    }

    private static void multi() {
        LongAdder longAdder = new LongAdder();
        long longAdderCost = BenchmarkUtil.multiRun(RunnableUtil.loopRunnable(longAdder::increment));
        log.info("multi longAdder cost {} millis, value {}", longAdderCost, longAdder.sum());

        AtomicLong atomicLong = new AtomicLong();
        long atomicCost = BenchmarkUtil.multiRun(RunnableUtil.loopRunnable(atomicLong::incrementAndGet));
        log.info("multi atomicLong cost {} millis, value {}", atomicCost, atomicLong.get());

        Cnt lockCnt = new Cnt();
        long lockCost = BenchmarkUtil.multiRun(RunnableUtil.loopRunnable(lockCnt::lockInc));
        log.info("multi lock cost {} millis, value {}", lockCost, lockCnt.getCnt());

        Cnt syncCnt = new Cnt();
        long syncCost = BenchmarkUtil.multiRun(RunnableUtil.loopRunnable(syncCnt::syncInc));
        log.info("multi sync cost {} millis, value {}", syncCost, syncCnt.getCnt());
    }

    private static class Cnt {
        private final Lock lock = new ReentrantLock();
        private final Object syncLock = new Object();

        @Getter
        private long cnt = 0L;

        public void syncInc() {
            synchronized (syncLock) {
                cnt++;
            }
        }

        public void lockInc() {
            lock.lock();
            cnt++;
            lock.unlock();
        }
    }

}
