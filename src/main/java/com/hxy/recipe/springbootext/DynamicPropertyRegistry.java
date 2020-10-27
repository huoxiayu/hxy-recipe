package com.hxy.recipe.springbootext;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
public class DynamicPropertyRegistry {

    private final Map<String, Consumer<String>> name2PropertyUpdater = new HashMap<>();

    void registerDynamicProperty(Object bean, Field field, DynamicProperty dynamicProperty) {
        String name = dynamicProperty.value();
        if (name2PropertyUpdater.containsKey(name)) {
            throw new IllegalStateException("duplicate.dynamic.property:" + name);
        }

        DynamicPropertyParser parser = dynamicProperty.parser();
        name2PropertyUpdater.put(name, (String value) -> {
            try {
                field.setAccessible(true);
                field.set(bean, parser.parse(value));
            } catch (Exception e) {
                log.error("unexpected exception when update dynamic property {}", name);
            }
        });
        log.info("register dynamic property {}", name);
    }

    public void update(String key, String value) {
        Consumer<String> updater = name2PropertyUpdater.get(key);
        if (updater != null) {
            log.info("update key {} -> value {}", key, value);
            updater.accept(value);
        }
    }

}
