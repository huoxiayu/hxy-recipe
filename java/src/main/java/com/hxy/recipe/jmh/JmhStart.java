package com.hxy.recipe.jmh;

import org.apache.commons.lang3.StringUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 3, time = 2, timeUnit = TimeUnit.SECONDS)
@Threads(2)
@Fork(2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@CompilerControl(value = CompilerControl.Mode.INLINE)
public class JmhStart {

    @Benchmark
    public void stringDirectAdd() {
        String str = StringUtils.EMPTY;
        for (int i = 0; i < 10; i++) {
            str += i;
        }
        if (str.length() == Integer.MAX_VALUE) {
            System.out.println(str);
        }
    }

    @Benchmark
    public void StringBuilderAdd() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(i);
        }
        String str = sb.toString();
        if (str.length() == Integer.MAX_VALUE) {
            System.out.println(str);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(JmhStart.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

}