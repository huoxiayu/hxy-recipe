package com.hxy.recipe.limit;

import com.google.common.util.concurrent.RateLimiter;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RateLimitStart {

    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(0.2);
        while (true) {
            log.info("acquire -> {}", rateLimiter.tryAcquire());
            Utils.sleepInSeconds(1L);
        }
    }

}
