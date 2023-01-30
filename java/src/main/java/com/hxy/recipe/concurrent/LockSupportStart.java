package com.hxy.recipe.concurrent;

import com.hxy.recipe.util.Utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;

/**
 * unpark后再park就不会park了，wait/notify则不同
 * output:
 * ** thread1 start
 * ** thread2 start
 * ** thread2 unpark thread1
 * ** thread2 end
 * ** thread1 park
 * ** thread1 end
 * ** all end
 */
public class LockSupportStart {

    public static void main(String[] args) throws Exception {
        // park_unpark();

        wait_notify();
    }

    private static void park_unpark() throws Exception {
        CountDownLatch cdl = new CountDownLatch(2);

        Thread thread1 = new Thread(() -> {
            System.out.println("thread1 start");

            Utils.sleepInSeconds(5L);

            System.out.println("thread1 park");

            LockSupport.park();

            cdl.countDown();

            System.out.println("thread1 end");
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 start");

            // Utils.sleepInSeconds(10L);

            LockSupport.unpark(thread1);

            System.out.println("thread2 unpark thread1");

            cdl.countDown();

            System.out.println("thread2 end");
        });
        thread2.start();

        cdl.await();

        System.out.println("all end");
    }

    private static void wait_notify() throws Exception {
        CountDownLatch cdl = new CountDownLatch(2);

        Object lock = new Object();

        Thread thread1 = new Thread(() -> {
            System.out.println("thread1 start");

            Utils.sleepInSeconds(5L);

            System.out.println("thread1 wait");

            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            cdl.countDown();

            System.out.println("thread1 end");
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 start");

            Utils.sleepInSeconds(10L);

            synchronized (lock) {
                lock.notify();
            }


            System.out.println("thread2 notify thread1");

            cdl.countDown();

            System.out.println("thread2 end");
        });
        thread2.start();

        cdl.await();

        System.out.println("all end");
    }

}
