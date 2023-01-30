package com.hxy.recipe.hystrix.common;

import com.netflix.hystrix.HystrixCommand;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class BizCommand extends HystrixCommand<String> {

    private static final AtomicInteger CNT = new AtomicInteger(0);

    public BizCommand(Setter setter) {
        super(setter);
    }

    @Override
    protected String run() {
        log.info("current thread: {}, call cnt: {}", Thread.currentThread().getName(), CNT.get());
        final int errorCnt = 3;
        if (CNT.incrementAndGet() < errorCnt) {
            throw new RuntimeException("error occur");
        }

        return "normal-value";
    }

    @Override
    public String getFallback() {
        return "fallback-value";
    }

}
