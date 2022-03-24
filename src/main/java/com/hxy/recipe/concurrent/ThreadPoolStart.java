package com.hxy.recipe.concurrent;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ThreadPoolStart {

    private static final AtomicInteger I = new AtomicInteger(0);
    private static final ThreadFactory THREAD_FACTORY = runnable -> {
        String thName = "hxy-thread-" + I.incrementAndGet();
        log.info("construct thread -> {}", thName);
        return new Thread(runnable, thName);
    };

    private static final int CORE_SIZE = 2;
    private static final int MAX_SIZE = 5;
    private static final int KEEP_ALIVE_TIME_IN_SECONDS = 1;
    private static final int QUEUE_SIZE = 10;

    public static void main(String[] args) throws InterruptedException {
        // Executor executor = threadPoolExecutor();
        Executor executor = forkJoinPool();

        log.info("begin");

        int tasks = CORE_SIZE + QUEUE_SIZE + 1;
        CountDownLatch cdl = new CountDownLatch(tasks);

        for (int i = 1; i <= tasks; i++) {
            final int fi = i;
            log.info("execute -> {}", fi);
            executor.execute(() -> {
                Utils.sleepInSeconds(2L);
                log.info("done -> {}", fi);
                cdl.countDown();
            });
        }

        cdl.await();

        Utils.sleepInSeconds(5L);

        log.info("next round");

        executor.execute(() -> {
            Utils.sleepInSeconds(2L);
            log.info("another job");
        });
    }

    private static Executor threadPoolExecutor() {
        // 1.core-threads是延迟初始化的，提交任务发现未达到core-threads则会new thread
        // 2.队列满了以后才会继续创建线程直到max-threads
        // 3.当线程超过keepAliveTime时，非core-threads如果没有任务处理会消亡
        return new ThreadPoolExecutor(
                CORE_SIZE,
                MAX_SIZE,
                KEEP_ALIVE_TIME_IN_SECONDS,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_SIZE),
                THREAD_FACTORY
        );
    }

    private static Executor forkJoinPool() {
        /**
         * todo: fjp源码
         * parallelStream默认使用common-pool
         * @see ForkJoinPool#common
         *
         * fkp核心是work-stealing
         * @see ForkJoinPool.WorkQueue
         * @see java.util.concurrent.ForkJoinWorkerThread
         */
        return new ForkJoinPool(
                (byte) 0
        );
    }

}
