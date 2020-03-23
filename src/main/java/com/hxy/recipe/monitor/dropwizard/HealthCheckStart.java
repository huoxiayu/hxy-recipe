package com.hxy.recipe.monitor.dropwizard;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class HealthCheckStart extends HealthCheck {

    public static void main(String[] args) {
        HealthCheckRegistry registry = new HealthCheckRegistry();
        registry.register("check", new HealthCheckStart());

        for (int i = 0; i < 10; i++) {
            registry.runHealthChecks().forEach((name, result) -> {
                String stat = result.isHealthy() ? "healthy" : "not healthy";
                log.info("{} is {}, message: {}", name, stat, result.getMessage());
            });
        }
    }

    @Override
    protected Result check() {
        return ThreadLocalRandom.current().nextBoolean() ? Result.healthy("I'm ok") : Result.unhealthy("I feel bad");
    }
}
