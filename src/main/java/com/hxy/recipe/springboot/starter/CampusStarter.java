package com.hxy.recipe.springboot.starter;

import com.hxy.recipe.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "campus", value = "enabled", havingValue = "true", matchIfMissing = true)
@PropertySource("classpath:campus.yml")
@ConditionalOnClass(value = {School.class, Class.class, Student.class})
@EnableConfigurationProperties(CampusProperties.class)
public class CampusStarter {

    public CampusStarter() {
        log.info("load CampusStarter");
    }

    @Bean
    public School campusAutoConfig(CampusProperties campusProperties) {
        log.info("campusProperties: {}", campusProperties);
        School school = new School();
        school.setSchoolName(campusProperties.getSchoolName());
        school.setClazz(campusProperties.getClazz());
        String print = JsonUtil.toJson(school);
        log.info("auto config bean -> {}", print);
        return school;
    }

}
