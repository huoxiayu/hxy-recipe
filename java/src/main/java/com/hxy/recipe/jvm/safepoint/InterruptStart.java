package com.hxy.recipe.jvm.safepoint;

import java.time.Duration;
import java.time.Instant;

/**
 * Thread.interrupted()的实现依赖safe-point
 * -XX:+UseCountedLoopSafepoints
 * -Xlog:jit+compilation=debug:file=jit_compile%t.log:uptime,level,tags:filecount=10,filesize=100M
 */
public class InterruptStart {

    static double algorithm(int n) {
        double bestSoFar = 0;
        for (int i = 0; i < n; ++i) {
            if (Thread.interrupted()) {
                System.out.println("broken by interrupted");
                break;
            }

            bestSoFar = Math.pow(i, 0.3);
        }
        return bestSoFar;
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            Instant start = Instant.now();
            double bestSoFar = algorithm(10_0000_0000);
            double durationInMillis = Duration.between(start, Instant.now()).toMillis();
            System.out.println("after " + durationInMillis + " ms, the result is " + bestSoFar);
        };

        Thread t = new Thread(task);
        t.start();
        Thread.sleep(1);
        System.out.println("1ms");
        t.interrupt();

        Thread.sleep(3000);

        t = new Thread(task);
        t.start();
        Thread.sleep(10);
        System.out.println("10ms");
        t.interrupt();

        Thread.sleep(3000);

        t = new Thread(task);
        t.start();
        Thread.sleep(100);
        System.out.println("100ms");
        t.interrupt();

        Thread.sleep(3000);

        t = new Thread(task);
        t.start();
        Thread.sleep(1000);
        System.out.println("1000ms");

        t.interrupt();
    }

}
