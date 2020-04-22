package com.hxy.recipe.monitor.dropwizard;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.ExponentiallyDecayingReservoir;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.hxy.recipe.util.Utils;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MetricsStart {

    public static void main(String[] args) {
        MetricRegistry registry = new MetricRegistry();

        ConsoleReporter.forRegistry(registry)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build()
            .start(2, TimeUnit.SECONDS);

        timer(registry);

        Utils.sleepInSeconds(5L);
    }

    private static void gauge(MetricRegistry registry) {
        AtomicInteger gaugeCnt = new AtomicInteger();
        Gauge<Integer> gauge = gaugeCnt::incrementAndGet;
        registry.register(MetricRegistry.name(MetricsStart.class, "gauge"), gauge);
    }

    private static void counter(MetricRegistry registry) {
        Counter counter = new Counter();
        Utils.SCHEDULER.scheduleAtFixedRate(counter::inc, 0L, 1L, TimeUnit.MILLISECONDS);
        registry.register(MetricRegistry.name(MetricsStart.class, "counter"), counter);
    }

    private static void meter(MetricRegistry registry) {
        Meter meter = new Meter();
        Utils.SCHEDULER.scheduleAtFixedRate(meter::mark, 0L, 5L, TimeUnit.MILLISECONDS);
        registry.register(MetricRegistry.name(MetricsStart.class, "meter"), meter);
    }

    private static void histogram(MetricRegistry registry) {
        Histogram histogram = new Histogram(new ExponentiallyDecayingReservoir());
        Runnable updater = () -> histogram.update(ThreadLocalRandom.current().nextInt(100));
        Utils.SCHEDULER.scheduleAtFixedRate(updater, 0L, 10L, TimeUnit.MILLISECONDS);
        registry.register(MetricRegistry.name(MetricsStart.class, "histogram"), histogram);
    }

    // timer is meter + histogram
    private static void timer(MetricRegistry registry) {
        Timer timer = registry.timer(MetricRegistry.name(MetricsStart.class, "timer"), Timer::new);
        for (int i = 0; i < 100; i++) {
            Timer.Context context = timer.time();
            Utils.sleepInMillis(ThreadLocalRandom.current().nextInt(100));
            context.stop();
        }
    }

}
