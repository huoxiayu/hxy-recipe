package com.hxy.recipe.agent.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import com.hxy.recipe.util.RunnableUtil;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@Slf4j
public class TransmittableThreadLocalStart {

    private static final String VALUE = "'value-in-parent'";
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();
    private static final InheritableThreadLocal<String> INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal<>();
    private static final TransmittableThreadLocal<String> TRANSMITTABLE_THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService brotherThreadWithoutInit = Utils.newSingleExecutors("brother-thread-without-init");

        ExecutorService brotherNoTtlWrapper = Utils.newSingleExecutors("brother-thread-no-ttl-wrapper");
        brotherNoTtlWrapper.submit(() -> {
        }).get();

        ExecutorService brotherWithTtlWrapper = Utils.newSingleExecutors("brother-thread-with-ttl-wrapper");
        brotherWithTtlWrapper.submit(() -> {
        }).get();

        Thread thread = new Thread(() -> {
            THREAD_LOCAL.set(VALUE);
            INHERITABLE_THREAD_LOCAL.set(VALUE);
            TRANSMITTABLE_THREAD_LOCAL.set(VALUE);

            log.info("set value");

            Runnable task = () -> {
                log.info("getFromThreadLocal {}", THREAD_LOCAL.get());
                log.info("getFromInheritableThreadLocal {}", INHERITABLE_THREAD_LOCAL.get());
                log.info("getFromTransmittableThreadLocal {}", TRANSMITTABLE_THREAD_LOCAL.get());
            };

            // direct call
            task.run();

            // child thread call
            Thread child = new Thread(task, "child-thread");
            child.start();
            RunnableUtil.runWithoutEx(child::join);

            // brotherThreadWithoutInit
            RunnableUtil.runWithoutEx(() -> brotherThreadWithoutInit.submit(task).get());

            // brotherNoTtlWrapper
            RunnableUtil.runWithoutEx(() -> brotherNoTtlWrapper.submit(task).get());

            // brotherThreadWithoutInit
            RunnableUtil.runWithoutEx(() -> brotherWithTtlWrapper.submit(TtlRunnable.get(task)).get());
        }, "parent-thread");

        thread.start();
        thread.join();
    }

}
