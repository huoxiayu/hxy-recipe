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
    private static final Object[] ARRAY = new Object[1 << 24];

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
        for (int i = 0; i < times; i++) {
            long setCost = BenchmarkUtil.multiRun(() -> {
                for (int j = 0; j < length; j++) {
                    VAR_HANDLE.set(ARRAY, j, "");
                }
            });

            long setOpaqueCost = BenchmarkUtil.multiRun(() -> {
                for (int j = 0; j < length; j++) {
                    VAR_HANDLE.setOpaque(ARRAY, j, "");
                }
            });

            long setReleaseCost = BenchmarkUtil.multiRun(() -> {
                for (int j = 0; j < length; j++) {
                    VAR_HANDLE.setRelease(ARRAY, j, "");
                }
            });

            long setVolatileCost = BenchmarkUtil.multiRun(() -> {
                for (int j = 0; j < length; j++) {
                    VAR_HANDLE.setVolatile(ARRAY, j, "");
                }
            });

            log.info("setCost cost {} millis", setCost);
            log.info("setOpaqueCost cost {} millis", setOpaqueCost);
            log.info("setReleaseCost cost {} millis", setReleaseCost);
            log.info("setVolatileCost cost {} millis", setVolatileCost);

            Utils.sleepInSeconds(1L);
        }
    }

}


