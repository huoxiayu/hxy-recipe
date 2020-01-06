package com.hxy.recipe.cache.caffeine.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class FiveSecondsSlowComputing {

    @Cacheable(value = "slow-computing-cache")
    public int compute(int input) {
        log.info("begin compute for input {}", input);

        try {
            TimeUnit.SECONDS.sleep(5L);
        } catch (InterruptedException ignored) {

        }

        return input + 1;
    }

}
