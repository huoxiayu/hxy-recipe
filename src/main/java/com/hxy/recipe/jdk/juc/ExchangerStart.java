package com.hxy.recipe.jdk.juc;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Exchanger;

@Slf4j
public class ExchangerStart {

    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();

        new Thread(() -> {
            try {
                Utils.sleepInSeconds(1L);
                log.info("sending -> x");
                log.info("get -> {}", exchanger.exchange("x"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "thread-x").start();

        new Thread(() -> {
            try {
                Utils.sleepInSeconds(5L);
                log.info("sending -> y");
                log.info("get -> {}", exchanger.exchange("y"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "thread-y").start();
    }

}
