package com.hxy.recipe.compute;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RunnableUtil;
import com.hxy.recipe.util.Utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

/**
 * java -XX:+PrintFlagsFinal -version
 * 查看JVM默认参数&值
 *
 * 使用G1：
 * -Xms128m
 * -Xmx128m
 * -XX:+UseG1GC
 * -XX:MaxGCPauseMillis=5000
 *
 * 使用并行GC：
 * -XX:+UseParallelGC
 *
 * 使用Epsilon：
 * -XX:+UnlockExperimentalVMOptions
 * -XX:+UseEpsilonGC
 *
 * 输出JIT编译过的方法
 * -XX:+PrintCompilation
 *
 * 打印编译过程信息
 * -XX:+PrintCompilation
 *
 * 打印内联方法
 * -XX:+PrintInlining
 *
 * 打印CodeCache
 * -XX:+PrintCodeCache
 *
 * 开启Graal替换C2
 * -XX:+UnlockExperimentalVMOptions
 * -XX:+UseJVMCICompiler
 *
 * 调整内联的层数
 * -XX:MaxInlineLevel=9
 * -XX:MaxRecursiveInlineLevel=1
 *
 * 如果热点方法字节码大小超过此值，则无法内联
 * -XX:FreqInlineSize=325
 *
 * 如果非热点方法字节码大小超过此值，则无法内联
 * -XX:MaxInlineSize=35
 *
 * 如果
 * -XX:+UnlockDiagnosticVMOptions
 * -XX:+LogTouchedMethods
 */
public class GCThroughput {

    private static final int RANGE = 1000_0000;
    private static final int MOD = 100;

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            run();
        }

        Utils.sleepInSeconds(3L);
        System.gc();
        Utils.sleepInSeconds(3L);

        run();

        Utils.sleepInSeconds(999999L);
    }

    public static void run() {
        ConcurrentHashMap<Integer, LongAdder> int2Adder = new ConcurrentHashMap<>();
        Runnable runnable = RunnableUtil.loopRunnable(() -> {
            IntStream.range(0, RANGE).parallel().forEach(i -> {
                int2Adder.computeIfAbsent(i % MOD, any -> new LongAdder()).add(i);
            });
        }, 10);
        BenchmarkUtil.singleRun(runnable, "compute");
    }

}
