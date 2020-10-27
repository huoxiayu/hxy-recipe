package com.hxy.recipe.springbootext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class DynamicPropertyAutoConfiguration {

    public DynamicPropertyAutoConfiguration() {
        log.info("enable dynamic property");
    }

    @Bean
    public DynamicPropertyRegistry dynamicPropertyRegistry() {
        return new DynamicPropertyRegistry();
    }

    @Bean
    public DynamicPropertyScanner dynamicPropertyScanner(DynamicPropertyRegistry dynamicPropertyRegistry) {
        return new DynamicPropertyScanner(dynamicPropertyRegistry);
    }

    @Bean
    public DynamicPropertyFetcher systemPropertyFetcher() {
        return new SystemPropertyFetcher();
    }

    @Bean
    public DynamicPropertyEventListener DynamicPropertyEventListener(List<DynamicPropertyFetcher> dynamicPropertyFetcherList,
                                                                     DynamicPropertyRegistry dynamicPropertyRegistry) {
        return new DynamicPropertyEventListener(dynamicPropertyFetcherList, dynamicPropertyRegistry);
    }

}
