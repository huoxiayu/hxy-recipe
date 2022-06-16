package com.hxy.recipe.exception;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class ExceptionNested {

    public static void main(String[] args) {
        CompletableFuture<Void> f = CompletableFuture.runAsync(() -> {
            try {
                foo();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        try {
            f.get();
        } catch (Exception e) {
            log.error("e -> ", e);
            log.error("e.getCause() -> ", e.getCause());
            log.error("e.getCause().getCause() -> ", e.getCause().getCause());
            log.error("e.getCause().getCause().getCause() -> ", e.getCause().getCause().getCause());
            // NullPointException
            log.error("e.getCause().getCause().getCause().getCause() -> ", e.getCause().getCause().getCause().getCause());
        }
    }

    private static void foo() {
        throw new RuntimeException("foo");
    }

}
