package com.hxy.recipe.datastructure;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MapStart {

    public static void main(String[] args) throws Exception {
        ConcurrentHashMap<Integer, Integer> cmap = new ConcurrentHashMap<>();
        for (int i = 0; i < 100; i++) {
            cmap.put(i, i * 2);
        }

        Field field = FieldUtils.getDeclaredField(
                ConcurrentHashMap.class, "table", true
        );


        Object table = field.get(cmap);
        log.info("table -> {}", table);
        log.info("cmap -> {}", cmap);

        cmap.clear();

        table = field.get(cmap);
        log.info("table -> {}", table);
        log.info("cmap -> {}", cmap);

        log.info("x");
    }

}
