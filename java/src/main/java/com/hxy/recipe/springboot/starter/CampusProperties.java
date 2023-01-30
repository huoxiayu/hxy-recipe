package com.hxy.recipe.springboot.starter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@Data
@ConfigurationProperties(prefix = "campus")
@PropertySource("classpath:campus.yml")
public class CampusProperties {

    public CampusProperties() {
        log.info("load CampusProperties");
    }

    private String schoolName;
    private Class clazz;

}
