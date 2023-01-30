package com.hxy.recipe.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException.e: {}", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(Class<T> clazz, String json) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            log.error("IOException.e: {}", e);
            throw new RuntimeException(e);
        }
    }

    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    private static class Bean {
        private String pa;
        private int pb;
        private List<String> pc;
        private InnerBean pd;
    }

    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    private static class InnerBean {
        private String x;
        private int y;
        private double z;
    }

    public static void main(String[] args) {
        Bean bean = new Bean("pa", 1, List.of("1", "2", "3"), new InnerBean("x", 10, 5.0));
        log.info("bean: {}", bean);
        String json = JsonUtil.toJson(bean);
        log.info("to json: {}", json);
        Bean fromJson = JsonUtil.fromJson(Bean.class, json);
        log.info("from json: {}", fromJson);
    }

}
