package com.hxy.recipe.jdk.juc.future;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 此处协程应该发挥作用！
 * -Djava.util.concurrent.ForkJoinPool.common.parallelism=1
 */
@Slf4j
public class CompletableFutureStart {

    public static void main(String[] args) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            Utils.sleepInSeconds(5L);
            log.info("first");
        });

        // with this line, second and third will be printed in main thread
        // comment this line, in fjp thread
        // Utils.sleepInSeconds(10L);

        future.thenRun(() -> log.info("second"))
                .whenComplete((ret, e) -> log.info("third"));

        Utils.sleepInSeconds(10L);

        // example1();
        // example2();
        example3();
    }

    private static void example1() {
        Supplier<String> slowCall = () -> {
            Utils.sleepInSeconds(10L);
            return UUID.randomUUID().toString();
        };

        log.info("begin f1");
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(slowCall)
                .orTimeout(5L, TimeUnit.SECONDS);

        try {
            String result = f1.get();
            log.info("result is: {}", result);
        } catch (Exception ex) {
            log.error("error: ", ex);

            Utils.sleepInSeconds(10L);

            log.info("f1 again");
            try {
                String resultAgain = f1.get();
                log.info("resultAgain is: {}", resultAgain);
            } catch (Exception againEx) {
                log.error("againEx: ", againEx);
            }
        }

        log.info("begin f2");
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(slowCall).completeOnTimeout("empty", 5L, TimeUnit.SECONDS);

        try {
            String result = f2.get();
            log.info("result is: {}", result);
        } catch (Exception e) {
            log.error("error: ", e);
        }
    }

    private static void example2() {
        long start = System.currentTimeMillis();
        List.of(
                List.of(1001, 1002, 1003, 1004),
                List.of(2001, 2002, 2003, 2004)
        ).parallelStream()
                .forEach(list -> {
                    log.info("list {} begin", list);
                    list.parallelStream().forEach(item -> {
                        log.info("item {} begin", item);
                        Utils.sleepInMillis(item);
                        log.info("item {} end", item);
                    });
                    log.info("list {} end", list);
                });

        long cost = System.currentTimeMillis() - start;
        log.info("process cost {} millis", cost);
    }

    private static void example3() {
        FutureTask<Integer> futureInt = new FutureTask<>(() -> {
            Utils.sleepInSeconds(5L);
            log.info("future done");
            return 666;
        });

        Utils.newExecutors("future-runner").execute(futureInt);

        List.of(1, 2, 3, 4)
                .parallelStream()
                .forEach(i -> {
                    try {
                        log.info("num {} -> get {}", i, futureInt.get());
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}
