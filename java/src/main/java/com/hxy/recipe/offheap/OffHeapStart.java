package com.hxy.recipe.offheap;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RunnableUtil;
import com.hxy.recipe.util.Utils;

import java.nio.ByteBuffer;

public class OffHeapStart {

    private static final int SIZE = 128 * 1024 * 1024;
    private static final int TIMES = 10;
    private static final ByteBuffer DIRECT_BYTE_BUFFER = ByteBuffer.allocateDirect(SIZE);
    private static final ByteBuffer HEAP_BYTE_BUFFER = ByteBuffer.allocate(SIZE);

    public static void main(String[] args) {
        while (true) {
            BenchmarkUtil.singleRun(
                    RunnableUtil.loopRunnable(() -> {
                        byte sum = 0;
                        for (int i = 0; i < SIZE; i++) {
                            sum += DIRECT_BYTE_BUFFER.get(i);
                        }
                        if (sum != 0) {
                            throw new IllegalStateException();
                        }
                    }, TIMES)
                    , "off-heap-cost"
            );

            BenchmarkUtil.singleRun(
                    RunnableUtil.loopRunnable(() -> {
                        byte sum = 0;
                        for (int i = 0; i < SIZE; i++) {
                            sum += HEAP_BYTE_BUFFER.get(i);
                        }
                        if (sum != 0) {
                            throw new IllegalStateException();
                        }
                    }, TIMES)
                    , "on-heap-cost"
            );

            Utils.sleepInSeconds(1L);
        }
    }

}
