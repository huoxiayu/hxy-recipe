package com.hxy.recipe.performance;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class LoopUnrollStart {

    private static final int LEN = 1 << 28;
    private static final boolean PRINT = false;

    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            log.info("times -> {}", i);
            loop_unroll1();
            System.gc();

            loop_unroll2();
            System.gc();

            loop_unroll4();
            System.gc();

            loop_unroll8();
            System.gc();

            loop_unroll16();
            System.gc();

            Utils.sleepInSeconds(5L);
        }
    }

    private static void loop_unroll1() {
        int[] a = new int[LEN];
        BenchmarkUtil.singleRun(() -> {
            for (int i = 0; i < LEN; i++) {
                a[i] = a[i] + 3;
            }
        }, "loop_unroll1");
        if (PRINT) {
            log.info(Arrays.stream(a).max().toString());
        }
    }

    private static void loop_unroll2() {
        int[] a = new int[LEN];
        BenchmarkUtil.singleRun(() -> {
            for (int i = 0; i < LEN; i += 2) {
                a[i] = a[i] + 3;
                a[i + 1] = a[i + 1] + 3;
            }
        }, "loop_unroll2");
        if (PRINT) {
            log.info(Arrays.stream(a).max().toString());
        }
    }

    private static void loop_unroll4() {
        int[] a = new int[LEN];
        BenchmarkUtil.singleRun(() -> {
            for (int i = 0; i < LEN; i += 4) {
                a[i] = a[i] + 3;
                a[i + 1] = a[i + 1] + 3;
                a[i + 2] = a[i + 2] + 3;
                a[i + 3] = a[i + 3] + 3;
            }
        }, "loop_unroll4");
        if (PRINT) {
            log.info(Arrays.stream(a).max().toString());
        }
    }

    private static void loop_unroll8() {
        int[] a = new int[LEN];
        BenchmarkUtil.singleRun(() -> {
            for (int i = 0; i < LEN; i += 8) {
                a[i] = a[i] + 3;
                a[i + 1] = a[i + 1] + 3;
                a[i + 2] = a[i + 2] + 3;
                a[i + 3] = a[i + 3] + 3;
                a[i + 4] = a[i + 4] + 3;
                a[i + 5] = a[i + 5] + 3;
                a[i + 6] = a[i + 6] + 3;
                a[i + 7] = a[i + 7] + 3;
            }
        }, "loop_unroll8");
        if (PRINT) {
            log.info(Arrays.stream(a).max().toString());
        }
    }

    private static void loop_unroll16() {
        int[] a = new int[LEN];
        BenchmarkUtil.singleRun(() -> {
            for (int i = 0; i < LEN; i += 16) {
                a[i] = a[i] + 3;
                a[i + 1] = a[i + 1] + 3;
                a[i + 2] = a[i + 2] + 3;
                a[i + 3] = a[i + 3] + 3;
                a[i + 4] = a[i + 4] + 3;
                a[i + 5] = a[i + 5] + 3;
                a[i + 6] = a[i + 6] + 3;
                a[i + 7] = a[i + 7] + 3;
                a[i + 8] = a[i + 8] + 3;
                a[i + 9] = a[i + 9] + 3;
                a[i + 10] = a[i + 10] + 3;
                a[i + 11] = a[i + 11] + 3;
                a[i + 12] = a[i + 12] + 3;
                a[i + 13] = a[i + 13] + 3;
                a[i + 14] = a[i + 14] + 3;
                a[i + 15] = a[i + 15] + 3;
            }
        }, "loop_unroll16");
        if (PRINT) {
            log.info(Arrays.stream(a).max().toString());
        }
    }

}
