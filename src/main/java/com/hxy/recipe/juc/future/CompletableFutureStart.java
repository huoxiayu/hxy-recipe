package com.hxy.recipe.juc.future;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
public class CompletableFutureStart {

    public static void main(String[] args) {
        Supplier<String> slowCall = () -> {
            Utils.sleep(10L);
            return UUID.randomUUID().toString();
        };

        log.info("begin f1");
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(slowCall)
            .orTimeout(5L, TimeUnit.SECONDS);

        try {
            String result = f1.get();
            log.info("result is: {}", result);
        } catch (Exception ex) {
            log.error("error: {}", ex);

            Utils.sleep(10L);

            log.info("f1 again");
            try {
                String resultAgain = f1.get();
                log.info("resultAgain is: {}", resultAgain);
            } catch (Exception againEx) {
                log.error("againEx: {}", againEx);
            }
        }

        log.info("begin f2");
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(slowCall).completeOnTimeout("empty", 5L, TimeUnit.SECONDS);

        try {
            String result = f2.get();
            log.info("result is: {}", result);
        } catch (Exception e) {
            log.error("error: {}", e);
        }
    }

}
