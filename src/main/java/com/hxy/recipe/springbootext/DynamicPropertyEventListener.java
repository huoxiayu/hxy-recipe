package com.hxy.recipe.springbootext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class DynamicPropertyEventListener {

    private static final long PERIOD_IN_SECONDS = 5L;

    private final List<DynamicPropertyFetcher> dynamicPropertyFetcherList;
    private final DynamicPropertyRegistry dynamicPropertyRegistry;

    public DynamicPropertyEventListener(List<DynamicPropertyFetcher> dynamicPropertyFetcherList,
                                        DynamicPropertyRegistry dynamicPropertyRegistry) {
        this.dynamicPropertyFetcherList = dynamicPropertyFetcherList;
        this.dynamicPropertyRegistry = dynamicPropertyRegistry;
        schedule();
    }

    private void schedule() {
        ThreadFactory factory = runnable -> new Thread(runnable, "dynamic-property-fetcher-scheduler");
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(factory);
        scheduler.scheduleAtFixedRate(() -> flushAllConfig(), 0L, PERIOD_IN_SECONDS, TimeUnit.SECONDS);
    }

    private void flushAllConfig() {
        for (DynamicPropertyFetcher fetcher : dynamicPropertyFetcherList) {
            Map<String, String> configs = fetcher.getAllConfig();
            configs.forEach(dynamicPropertyRegistry::update);
        }
    }

}
