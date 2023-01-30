package com.hxy.recipe.jdk.juc.phaser;

import com.hxy.recipe.util.LogUtil;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Phaser;

@Slf4j
public class PhaserStart {

    public static void main(String[] args) {
        List<Task> tasks = List.of(
                new Task("task1"),
                new Task("task2"),
                new Task("task3")
        );

        List.<Runnable>of(
                () -> runLikeCyclicBarrier(tasks),
                () -> runByCommander(tasks)
        ).forEach(runnable -> {
            runnable.run();
            Utils.sleepInSeconds(5L);
            LogUtil.newLine();
        });
    }

    private static void runLikeCyclicBarrier(List<Task> tasks) {
        Phaser phaser = new Phaser();
        tasks.forEach(task -> {
            phaser.register();
            log.info("register -> {} ", task.name);

            new Thread(() -> {
                Utils.sleepInSeconds(1L);

                log.info("arriveAndAwaitAdvance -> {} ", task.name);
                phaser.arriveAndAwaitAdvance();

                task.run();
            }).start();
        });
    }

    private static void runByCommander(List<Task> tasks) {
        Phaser phaser = new Phaser(1);

        tasks.forEach(task -> {
            phaser.register();
            log.info("register -> {} ", task.name);

            new Thread(() -> {
                Utils.sleepInSeconds(1L);

                log.info("arriveAndAwaitAdvance -> {} ", task.name);
                phaser.arriveAndAwaitAdvance();

                task.run();
            }).start();
        });

        log.info("arriveAndDeregister");
        phaser.arriveAndDeregister();
    }

    static class Task implements Runnable {

        private final String name;

        Task(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            log.info("run -> {} ", name);
        }
    }

}
