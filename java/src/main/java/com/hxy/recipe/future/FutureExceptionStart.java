package com.hxy.recipe.future;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class FutureExceptionStart {

    private static class GoException extends RuntimeException {
        public GoException(String msg) {
            super(msg);
        }
    }

    protected static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    public static Object go() {
        log.info("go");
        throw new GoException("go exception");
    }

    public static void main(String[] args) throws Exception {
        long sleepTimeInMillis = 10L;

        invokeAll();

        TimeUnit.SECONDS.sleep(sleepTimeInMillis);

        execute();

        TimeUnit.SECONDS.sleep(sleepTimeInMillis);

        submit();

        TimeUnit.SECONDS.sleep(sleepTimeInMillis);

        EXECUTOR.shutdown();
    }

    private static void invokeAll() throws InterruptedException {
        log.info("invoke all begin");
        List<Callable<Object>> tasks = new ArrayList<>(1);
        tasks.add(() -> go());
        List<Future<Object>> futures = EXECUTOR.invokeAll(tasks);
        for (Future<Object> f : futures) {
            try {
                f.get();
            } catch (Exception e) {
                log.error("err:", e);
            }
        }

        log.info("invoke all end");
    }

    private static void submit() {
        log.info("submit begin");
        Future<Object> future = EXECUTOR.submit(FutureExceptionStart::go);
        try {
            future.get();
        } catch (Exception e) {
            log.error("err:", e);
        }
        log.info("submit end");
    }

    private static void execute() {
        log.info("execute begin");
        try {
            EXECUTOR.execute(FutureExceptionStart::go);
        } catch (Exception e) {
            log.error("err:", e);
        }
        log.info("execute end");
    }

}
