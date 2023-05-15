package com.hxy.recipe.jdk.juc.future;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ListenableFutureStart {

    private static final int times = 100;
    private static final long maxTimeInMillis = 10L;
    private static final long timeoutInMillis = maxTimeInMillis / 2;
    private static final boolean debug = false;

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < times; i++) {
            System.out.println("times: " + i);
            run();
        }
    }

    public static void run() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(executor);
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        AtomicInteger cnt = new AtomicInteger(0);

        long sumTimeInMillis = 0L;
        for (int i = 0; i < times; i++) {
            long timeInMillis = rand.nextLong(maxTimeInMillis) + 1L;
            sumTimeInMillis += timeInMillis;

            Task task = new Task(timeInMillis);
            cnt.incrementAndGet();
            ListenableFuture<Long> future = listeningExecutorService.submit(task);
            if (i == 0) {
                if (debug) System.out.println("future.getClass(): " + future.getClass().getName());
            }
            future.addListener(cnt::decrementAndGet, listeningExecutorService);
            try {
                long result = future.get(timeoutInMillis, TimeUnit.MILLISECONDS);
                if (debug) System.out.printf("ok, result -> %d, cnt -> %s%n", result, cnt);
            } catch (Exception e) {
                boolean done = future.isDone();
                boolean cancel = future.cancel(true);
                if (debug) System.out.printf("done -> %s%n", done);
                if (debug) System.out.printf("cancel -> %s%n", cancel);
                if (debug) System.out.printf("error -> %s, cnt -> %s%n", e, cnt);
            }
        }

        Thread.sleep(sumTimeInMillis);
        Preconditions.checkState(cnt.intValue() == 0);

        listeningExecutorService.shutdown();
        System.out.println("shutdown");
    }

    private static class Task implements Callable<Long> {

        private final long timeInMillis;

        public Task(long timeInMillis) {
            Preconditions.checkState(timeInMillis > 0L);
            this.timeInMillis = timeInMillis;
        }

        @Override
        public Long call() throws Exception {
            Thread.sleep(this.timeInMillis);
            return this.timeInMillis;
        }
    }

}
