package com.hxy.recipe.concurrent;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 主线程等待其他线程的计算结果
 */
@Slf4j
public class ThreadSynchronization {

    static String shareResult = null;

    public static void main(String[] args) throws Exception {
        process();
    }

    @Data
    private static class Result {
        private volatile String result;

        @Override
        public String toString() {
            return result;
        }
    }

    private static void process() throws Exception {
        int cnt = 1;

        // 1、线程池
        log.info("case {}", cnt++);
        ExecutorService threadPool = Executors.newSingleThreadExecutor(ThreadSynchronization::newThread);
        Future<String> threadPoolTask = threadPool.submit(ThreadSynchronization::compute);
        log.info(threadPoolTask.get());

        // 2、CountDownLatch
        log.info("case {}", cnt++);
        Result countDownLatchResult = new Result();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        newThread(() -> {
            countDownLatchResult.setResult(compute());
            countDownLatch.countDown();
        }).start();

        countDownLatch.await();
        log.info(countDownLatchResult.toString());

        // 3、Semaphore
        log.info("case {}", cnt++);
        Result semaphoreResult = new Result();
        Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();
        newThread(() -> {
            semaphoreResult.setResult(compute());
            semaphore.release();
        }).start();

        semaphore.acquire();
        log.info(semaphoreResult.toString());
        semaphore.release();

        // 4、CyclicBarrier
        log.info("case {}", cnt++);
        Result cyclicBarrierResult = new Result();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        newThread(() -> {
            cyclicBarrierResult.setResult(compute());
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                log.error("unexpected exception: {}", e);
            }
        }).start();

        cyclicBarrier.await();
        log.info(cyclicBarrierResult.toString());

        // 5、volatile
        log.info("case {}", cnt++);
        Result volatileResult = new Result();
        newThread(() -> volatileResult.setResult(compute())).start();
        while (volatileResult.getResult() == null) {
            // spin
        }
        log.info(volatileResult.toString());

        // 6、AtomicReference
        log.info("case {}", cnt++);
        AtomicReference<String> atomicResult = new AtomicReference<>();
        newThread(() -> atomicResult.set(compute())).start();
        while (atomicResult.get() == null) {
            // spin
        }
        log.info(atomicResult.get());

        // 7、wait & notify
        log.info("case {}", cnt++);
        Result syncResult = new Result();
        newThread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                log.error("unexpected exception: {}", e);
            }

            synchronized (syncResult) {
                syncResult.setResult(compute());
                syncResult.notify();
            }
        }).start();

        synchronized (syncResult) {
            while (syncResult.getResult() == null) {
                syncResult.wait();
            }
        }

        log.info(syncResult.toString());

        // 8、LockSupport
        log.info("case {}", cnt++);
        Result lockSupportResult = new Result();
        Thread mainThread = Thread.currentThread();
        newThread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                log.error("unexpected exception: {}", e);
            }

            lockSupportResult.setResult(compute());
            LockSupport.unpark(mainThread);
        }).start();

        if (lockSupportResult.getResult() == null) {
            LockSupport.park();
        }

        log.info(lockSupportResult.toString());

        // 9、CompletableFuture
        log.info("case {}", cnt++);
        CompletableFuture<String> future = CompletableFuture.supplyAsync(
                ThreadSynchronization::compute, threadPool
        );
        log.info(future.get());

        // 10、join
        log.info("case {}", cnt++);
        Thread subThread = newThread(() -> shareResult = compute());
        subThread.start();
        subThread.join();
        log.info(shareResult);

        // 11、BlockingQueue
        log.info("case {}", cnt++);
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);
        newThread(() -> blockingQueue.add(compute())).start();
        log.info(blockingQueue.take());

        // 12、ReentrantLock & Condition
        log.info("case {}", cnt++);
        Result lockConditionResult = new Result();
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        newThread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                log.error("unexpected exception: {}", e);
            }

            lock.lock();
            try {
                lockConditionResult.setResult(compute());
                condition.signal();
            } finally {
                lock.unlock();
            }

        }).start();

        try {
            lock.lock();
            while (lockConditionResult.getResult() == null) {
                condition.await();
            }
        } finally {
            lock.unlock();
        }

        log.info(lockConditionResult.toString());
    }

    private static Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread;
    }

    private static String compute() {
        return String.valueOf(doCompute(36));
    }

    private static int doCompute(int n) {
        return n < 2 ? 1 : doCompute(n - 1) + doCompute(n - 2);
    }

}
