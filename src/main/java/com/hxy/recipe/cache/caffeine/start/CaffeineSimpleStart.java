package com.hxy.recipe.cache.caffeine.start;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.hxy.recipe.util.BenchmarkUtil;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * good references：
 * https://en.wikipedia.org/wiki/Count%E2%80%93min_sketch
 * http://highscalability.com/blog/2016/1/25/design-of-a-modern-cache.html
 * http://highscalability.com/blog/2019/2/25/design-of-a-modern-cachepart-deux.html
 * https://arxiv.org/pdf/1512.00727.pdf
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * @see com.github.benmanes.caffeine.cache.LocalCacheFactory#newBoundedLocalCache
 * 通过builder中不同的参数，构造不同的Node，保证最小开销
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * fast path（tips：可以参考golang的Mutex设计，将fast path单独抽成一个函数，方便内联）
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * 大量使用了UnSafe中的getRelease、getRelaxed、lazySet
 * @see com.github.benmanes.caffeine.cache.Buffer
 * @see com.github.benmanes.caffeine.cache.BoundedBuffer.RingBuffer
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * casWriteCounter(tail, tail + OFFSET)减少伪共享
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * @see com.github.benmanes.caffeine.cache.StripedBuffer
 * thread-local queue减少冲突
 * @see com.github.benmanes.caffeine.cache.BoundedLocalCache#writeBuffer()
 * mpsc-queue
 * @see com.github.benmanes.caffeine.cache.MpscGrowableArrayQueue
 * time-wheel
 * @see com.github.benmanes.caffeine.cache.TimerWheel
 */
@Slf4j
public class CaffeineSimpleStart {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        LoadingCache<Integer, Integer> intIntCache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(Duration.ofSeconds(1L))
                .executor(Utils.newExecutors("sync-caffeine-thread-groups-"))
                .build(key -> {
                    log.info("print something to see thread name");
                    Utils.sleepInSeconds(1L);
                    return key;
                });

        for (int i = 0; i < 5; i++) {
            long cost = BenchmarkUtil.singleRun(() -> {
                /**
                 * @see com.github.benmanes.caffeine.cache.BoundedLocalCache
                 * @see com.github.benmanes.caffeine.cache.FrequencySketch
                 * Count-Min Sketch算法
                 */
                int v = intIntCache.get(1);
                log.info("v -> {}", v);
            });

            log.info("cost -> {}", cost);

            Utils.sleepInMillis(500L);
        }

        // caffeine同时支持async api
        AsyncLoadingCache<Integer, Integer> cache = Caffeine.newBuilder()
                .executor(Utils.newExecutors("async-caffeine-thread-groups-"))
                .buildAsync(key -> {
            log.info("print something to see thread name");
            return key;
        });
        CompletableFuture<Integer> completableFuture = cache.get(1);
        completableFuture.get();
    }

}
