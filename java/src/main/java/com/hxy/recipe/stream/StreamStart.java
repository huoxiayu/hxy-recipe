package com.hxy.recipe.stream;

import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.RunnableUtil;
import com.hxy.recipe.util.Utils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class StreamStart {

    private static final int LEN = 1000_0000;
    private static final int TIMES = 10;

    @Data
    private static class Bean {
        private String p1;
        private int p2;
        private long p3;
        private boolean p4;
        private double p5;
        private int v;

        public Bean() {
            ThreadLocalRandom rand = ThreadLocalRandom.current();
            String uuid = UUID.randomUUID().toString();
            this.p1 = uuid.substring(rand.nextInt(uuid.length()));
            this.p2 = rand.nextInt();
            this.p3 = rand.nextLong();
            this.p4 = rand.nextBoolean();
            this.p5 = rand.nextDouble();
            this.v = rand.nextInt();
        }

        private long sum() {
            return p1.hashCode() + p2 + p3 + (p4 ? 1 : 0) + Math.round(p5) + v;
        }
    }

    public static void main(String[] args) {
        int size = 1 << 25;
        Bean[] array = new Bean[size];
        for (int i = 0; i < size; i++) {
            array[i] = new Bean();
        }

        RunnableUtil.loopRunnable(() -> {
            BenchmarkUtil.singleRun(
                    () -> {
                        long sum = Arrays.stream(array).mapToLong(Bean::sum).sum();
                        log.info("sum -> {}", sum);
                    },
                    "serial-cost"
            );

            BenchmarkUtil.singleRun(
                    () -> {
                        long sum = Arrays.stream(array).parallel().mapToLong(Bean::sum).sum();
                        log.info("sum -> {}", sum);
                    },
                    "parallel-cost"
            );

        }, 100).run();

        System.out.println(Spliterator.DISTINCT);
        System.out.println(Spliterator.SORTED);
        System.out.println(Spliterator.ORDERED);
        System.out.println(Spliterator.SIZED);
        System.out.println(Spliterator.NONNULL);
        System.out.println(Spliterator.IMMUTABLE);
        System.out.println(Spliterator.CONCURRENT);
        System.out.println(Spliterator.SUBSIZED);

        Stream.of(1, 2).filter(i -> i % 2 == 0).forEach(System.out::println);

        // performance();
    }

    private static void performance() {
        List<Bean> beans = new ArrayList<>(LEN);
        for (int i = 0; i < LEN; i++) {
            beans.add(new Bean());
        }

        log.info("init beans");

        Runnable loop = RunnableUtil.loopRunnable(() -> loop(beans), TIMES);

        Runnable stream = RunnableUtil.loopRunnable(() -> stream(beans), TIMES);

        loop.run();
        log.info("loop warmup");

        stream.run();
        log.info("stream warmup");

        Utils.sleepInSeconds(30L);

        // loop cost 2035 millis
        BenchmarkUtil.singleRun(loop, "loop");

        // stream cost 3513 millis
        BenchmarkUtil.singleRun(stream, "stream");
    }

    private static void stream(List<Bean> beans) {
        IntStream intStream = beans.stream()
                .filter(b1 -> b1.p1.length() >= 3)
                .filter(b2 -> b2.p2 % 31 >= 0)
                .filter(b3 -> b3.p3 % 71L >= 0L)
                .filter(b4 -> b4.p4)
                .filter(b5 -> b5.p5 > 1e-5)
                .mapToInt(x -> x.v);
        int sum = intStream
                .sum();
        log.info("sum -> {}", sum);
    }

    private static void loop(List<Bean> beans) {
        int sum = 0;
        for (Bean bean : beans) {
            if (bean.p1.length() >= 3
                    && bean.p2 % 31 >= 0
                    && bean.p3 % 71L >= 0L
                    && bean.p4 && bean.p5 > 1e-5) {
                sum += bean.v;
            }
        }

        log.info("sum -> {}", sum);
    }

}
