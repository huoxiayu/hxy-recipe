package com.hxy.recipe.concurrent;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

@Slf4j
public class VarHandleStart {

    private static final VarHandle VAR_HANDLE = MethodHandles.arrayElementVarHandle(
            Object[].class
    );
    private static final Object[] ARRAY = new Object[1 << 25];

    public static void main(String[] args) {
        /**
         * @see VarHandle.AccessType
         * @see VarHandle.AccessMode
         */
        VarHandle.loadLoadFence();
        VarHandle.storeStoreFence();

        VarHandle.acquireFence();
        VarHandle.releaseFence();
        VarHandle.fullFence();

        int times = 100;
        int length = ARRAY.length;
        String str = "xxx";
        for (int i = 0; i < times; i++) {
            long setCost = BenchmarkUtil.multiRun(() -> {
                for (int j = 0; j < length; j++) {
                    VAR_HANDLE.set(ARRAY, j, str);
                }
            });

            long setOpaqueCost = BenchmarkUtil.multiRun(() -> {
                for (int j = 0; j < length; j++) {
                    VAR_HANDLE.setOpaque(ARRAY, j, str);
                }
            });

            long setReleaseCost = BenchmarkUtil.multiRun(() -> {
                for (int j = 0; j < length; j++) {
                    VAR_HANDLE.setRelease(ARRAY, j, str);
                }
            });

            long setVolatileCost = BenchmarkUtil.multiRun(() -> {
                for (int j = 0; j < length; j++) {
                    VAR_HANDLE.setVolatile(ARRAY, j, str);
                }
            });

            log.info("setCost cost {} millis", setCost);
            log.info("setOpaqueCost cost {} millis", setOpaqueCost);
            log.info("setReleaseCost cost {} millis", setReleaseCost);
            log.info("setVolatileCost cost {} millis", setVolatileCost);
            log.info("<------------------------------->");

            long getCost = BenchmarkUtil.multiRun(() -> {
                for (int j = 0; j < length; j++) {
                    check(str == VAR_HANDLE.get(ARRAY, j));
                }
            });

            long getOpaqueCost = BenchmarkUtil.multiRun(() -> {
                for (int j = 0; j < length; j++) {
                    check(str == VAR_HANDLE.getOpaque(ARRAY, j));
                }
            });

            long getReleaseCost = BenchmarkUtil.multiRun(() -> {
                for (int j = 0; j < length; j++) {
                    check(str == VAR_HANDLE.getAcquire(ARRAY, j));
                }
            });

            long getVolatileCost = BenchmarkUtil.multiRun(() -> {
                for (int j = 0; j < length; j++) {
                    check(str == VAR_HANDLE.getVolatile(ARRAY, j));
                }
            });

            log.info("getCost cost {} millis", getCost);
            log.info("getOpaqueCost cost {} millis", getOpaqueCost);
            log.info("getReleaseCost cost {} millis", getReleaseCost);
            log.info("getVolatileCost cost {} millis", getVolatileCost);
            log.info("<------------------------------->");

            Utils.sleepInSeconds(1L);
        }
    }

    private static void check(boolean b) {
        if (!b) {
            throw new RuntimeException("check fail");
        }
    }

}


