package com.hxy.recipe.springbootext;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

import java.lang.reflect.Field;

@Slf4j
public class DynamicPropertyScanner extends InstantiationAwareBeanPostProcessorAdapter {

    private final DynamicPropertyRegistry dynamicPropertyRegistry;

    public DynamicPropertyScanner(DynamicPropertyRegistry dynamicPropertyRegistry) {
        this.dynamicPropertyRegistry = dynamicPropertyRegistry;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        registerDynamicProperty(bean, beanName);
        return super.postProcessAfterInitialization(bean, beanName);
    }

    private void registerDynamicProperty(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        for (Field field : beanClass.getDeclaredFields()) {
            DynamicProperty dynamicProperty = field.getAnnotation(DynamicProperty.class);
            if (dynamicProperty != null) {
                dynamicPropertyRegistry.registerDynamicProperty(bean, field, dynamicProperty);
                log.info("{} register dynamic property {}", beanName, field);
            }
        }
    }

}
