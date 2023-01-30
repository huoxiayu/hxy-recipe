package com.hxy.recipe.offheap.io;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RunnableUtil;
import com.hxy.recipe.util.Utils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class OffHeapPerformance {

    private static final File FILE = new File("tmp.log");
    private static final String MODE = "rw";
    private static final int REPEAT_TIMES = 10;
    private static final int SIZE = 128 * 1024 * 1024;
    private static final byte[] BYTE_ARRAY = new byte[SIZE];
    private static final ByteBuffer DIRECT_BYTE_BUFFER = ByteBuffer.allocateDirect(SIZE);
    private static final ByteBuffer HEAP_BYTE_BUFFER = ByteBuffer.allocate(SIZE);
    private static byte SUM = 0;

    public static void main(String[] args) {
        init();

        performance();

        Utils.sleepInSeconds(10L);

        performance();
    }

    private static void init() {
        log.info("init start");
        for (int i = 0; i < SIZE; i++) {
            SUM += i;
            byte bt = (byte) i;
            BYTE_ARRAY[i] = bt;
            DIRECT_BYTE_BUFFER.put(bt);
            HEAP_BYTE_BUFFER.put(bt);
        }
        log.info("init end");
    }

    @SneakyThrows
    private static void performance() {
        BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> {
            byte sum = 0;
            for (int i = 0; i < SIZE; i++) {
                sum += BYTE_ARRAY[i];
            }
            Assert.isTrue(sum == SUM, "no eq");
        }, REPEAT_TIMES), "sum-array");

        BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> {
            byte sum = 0;
            for (int i = 0; i < SIZE; i++) {
                sum += DIRECT_BYTE_BUFFER.get(i);
            }
            Assert.isTrue(sum == SUM, "no eq");
        }, REPEAT_TIMES), "sum-direct-byte-buffer");

        BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> {
            byte sum = 0;
            for (int i = 0; i < SIZE; i++) {
                sum += HEAP_BYTE_BUFFER.get(i);
            }
            Assert.isTrue(sum == SUM, "no eq");
        }, REPEAT_TIMES), "sum-heap-byte-buffer");

        RandomAccessFile wf = new RandomAccessFile(FILE, MODE);
        wf.setLength(SIZE);
        wf.write(BYTE_ARRAY);
        wf.close();
        log.info("write to file");

        RandomAccessFile rf = new RandomAccessFile(FILE, MODE);
        MappedByteBuffer mbb = rf.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, SIZE);

        BenchmarkUtil.singleRun(RunnableUtil.loopRunnable(() -> {
            byte sum = 0;
            for (int i = 0; i < SIZE; i++) {
                sum += mbb.get(i);
            }
            Assert.isTrue(sum == SUM, "no eq");
        }, REPEAT_TIMES), "sum-mapped-byte-buffer");

        rf.close();
    }

}
