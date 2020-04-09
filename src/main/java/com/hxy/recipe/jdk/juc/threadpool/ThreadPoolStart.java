package com.hxy.recipe.jdk.juc.threadpool;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class ThreadPoolStart {

    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        // swallow exception
        ((ScheduledThreadPoolExecutor) scheduledExecutorService).setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());

        // print normally
        scheduledExecutorService.execute(() -> {
            Utils.sleep(1L);
            log.info("execute before shutdown");
        });

        // shutdown
        scheduledExecutorService.shutdown();

        // no error and no print
        scheduledExecutorService.execute(() -> {
            Utils.sleep(1L);
            log.info("after shutdown");
        });

        while (!scheduledExecutorService.isTerminated()) {
            Utils.sleepInMillis(500L);
            log.info("stop...");
        }

        log.info("stopped");
    }

}
