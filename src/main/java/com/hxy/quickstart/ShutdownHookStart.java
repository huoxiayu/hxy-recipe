package com.hxy.quickstart;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ShutdownHookStart {

    // kill -9将不会执行
    static {
        Thread shutdownHookThread = new Thread(() -> {
            log.info("shutdown...");

            try {
                TimeUnit.SECONDS.sleep(1L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            log.info("shutdown.");
        }, "");

        Runtime.getRuntime().addShutdownHook(shutdownHookThread);
    }

    public static void main(String[] args) throws InterruptedException {
        TimeUnit.SECONDS.sleep(1000000L);
    }

}
