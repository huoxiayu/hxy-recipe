package com.hxy.recipe.netty;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ImmediateEventExecutor;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @see DefaultPromise#notifyListeners
 */
@Slf4j
public class NestedPromiseStart {

    // 通过将任务提交到线程池解决stack-overflow的问题
    static {
        System.setProperty("io.netty.defaultPromise.maxListenerStackDepth", "2");
    }

    public static void main(String[] args) throws Exception {
        // 这里只是为了演示，使用了ImmediateEventExecutor
        EventExecutor executor = ImmediateEventExecutor.INSTANCE;

        Promise<String> root = new DefaultPromise<>(executor);
        Promise<String> before = root;
        for (int i = 1; i <= 5; i++) {
            Promise<String> p = new DefaultPromise<>(executor);
            before.addListener(new Listener(p));
            before = p;
        }

        log.info("init promise list");

        root.setSuccess("success");

        log.info("end");
    }

    private static class Listener implements GenericFutureListener<Future<String>> {

        private static final AtomicInteger COUNTER = new AtomicInteger(1);

        private final String name;
        private final Promise<String> promise;

        public Listener(Promise<String> promise) {
            this.name = "listener-" + COUNTER.getAndIncrement();
            this.promise = promise;
        }

        @Override
        public void operationComplete(Future<String> future) {
            log.info("trigger {}", name);
            if (null != promise) {
                promise.setSuccess("success");
            }
        }
    }

}
