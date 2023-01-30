package com.hxy.recipe.jdk.juc.volatileM;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VolatileStart {

    // no volatile no end
    static boolean RUNNING = true;

    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println("start");
            while (RUNNING) {
            }
            System.out.println("end");
        }, "running-").start();

        Utils.sleepInSeconds(1L);
        System.out.println("set");
        RUNNING = false;
    }

}
