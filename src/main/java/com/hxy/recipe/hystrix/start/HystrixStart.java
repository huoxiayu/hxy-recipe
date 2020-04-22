package com.hxy.recipe.hystrix.start;

import com.hxy.recipe.hystrix.common.BizCommand;
import com.hxy.recipe.util.Utils;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HystrixStart {

    public static void main(String[] args) {
        log.info("main thread: {}", Thread.currentThread().getName());
        HystrixCommand.Setter setter = HystrixCommand.Setter
            .withGroupKey(HystrixCommandGroupKey.Factory.asKey("biz-command"))
            .andCommandKey(HystrixCommandKey.Factory.asKey("singleRun"))
            .andCommandPropertiesDefaults(
                HystrixCommandProperties.Setter().withCircuitBreakerEnabled(true)
                    .withCircuitBreakerRequestVolumeThreshold(1)
                    .withCircuitBreakerSleepWindowInMilliseconds(2000)
                    .withCircuitBreakerErrorThresholdPercentage(50)
                    .withExecutionIsolationThreadInterruptOnTimeout(true)
                    .withExecutionTimeoutEnabled(true)
            )
            .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(1));

        for (int i = 0; i < 10; i++) {
            BizCommand bizCommand = new BizCommand(setter);
            String result = bizCommand.execute();
            log.info("result {} for {} times", result, i);

            Utils.sleepInSeconds(1L);
        }

    }

}
