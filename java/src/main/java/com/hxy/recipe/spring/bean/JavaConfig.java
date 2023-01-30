package com.hxy.recipe.spring.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaConfig {

    @Bean
    public JavaConfigBean javaConfigBean() {
        return new JavaConfigBean();
    }

}
