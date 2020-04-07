package com.hxy.recipe.agent.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

@Slf4j
public class TransmittableThreadLocalStart {

    private static final String VALUE = "'value-in-parent'";
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();
    private static final InheritableThreadLocal<String> INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal<>();
    private static final TransmittableThreadLocal<String> TRANSMITTABLE_THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService mainExecutor = Utils.newExecutors("main-executor");

        Thread thread = new Thread(() -> {
            THREAD_LOCAL.set(VALUE);
            INHERITABLE_THREAD_LOCAL.set(VALUE);
            TRANSMITTABLE_THREAD_LOCAL.set(VALUE);

            Runnable task = () -> {
                log.info("thread {}", Thread.currentThread().getName());
                log.info("getFromThreadLocal {}", THREAD_LOCAL.get());
                log.info("getFromInheritableThreadLocal {}", INHERITABLE_THREAD_LOCAL.get());
                log.info("getFromTransmittableThreadLocal {}", TRANSMITTABLE_THREAD_LOCAL.get());
            };

            // direct call
            task.run();

            // child thread call
            Utils.newExecutors("child-executor").submit(task);

            // other thread call
            mainExecutor.execute(task);

            Utils.sleepInMillis(100);
        }, "boot-thread");

        thread.start();
        thread.join();
    }

}
