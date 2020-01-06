package com.hxy.recipe.cache.caffeine.start;

import com.hxy.recipe.RecipeApplication;
import com.hxy.recipe.cache.caffeine.common.FiveSecondsSlowComputing;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StopWatch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class CaffeineStart {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RecipeApplication.class, args);
        FiveSecondsSlowComputing fiveSecondsSlowComputing = context.getBean(FiveSecondsSlowComputing.class);

        StopWatch stopWatch = new StopWatch();

        stopWatch.start("first not hit cache");
        int firstResult = fiveSecondsSlowComputing.compute(10);
        log.info("firstResult: {}", firstResult);
        stopWatch.stop();

        stopWatch.start("second hit cache");
        int secondResult = fiveSecondsSlowComputing.compute(10);
        log.info("secondResult: {}", secondResult);
        stopWatch.stop();

        stopWatch.start("third hit cache");
        int thirdResult = fiveSecondsSlowComputing.compute(10);
        log.info("thirdResult: {}", thirdResult);
        stopWatch.stop();

        Utils.sleep(10L);

        // expireAfterWrite=10s
        stopWatch.start("wait 10s for cache expire");
        int cacheExpireResult = fiveSecondsSlowComputing.compute(10);
        log.info("cacheExpireResult: {}", cacheExpireResult);
        stopWatch.stop();

        stopWatch.start("hit cache after refresh");
        int hitAfterCacheRefresh = fiveSecondsSlowComputing.compute(10);
        log.info("hitAfterCacheRefresh: {}", hitAfterCacheRefresh);
        stopWatch.stop();

        log.info("stopWatch.prettyPrint(): {}", stopWatch.prettyPrint());

        // wait for expire
        Utils.sleep(10L);

        // initialCapacity=10,maximumSize=10
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 15; i++) {
            final int idx = i;
            executorService.execute(() -> fiveSecondsSlowComputing.compute(idx));
        }

        Utils.sleep(5L);

        for (int i = 0; i < 15; i++) {
            stopWatch.start();
            fiveSecondsSlowComputing.compute(i);
            stopWatch.stop();
            long cost = stopWatch.getLastTaskTimeMillis();
            log.info("compute {} cost {} millis", i, cost);
        }
    }

}
