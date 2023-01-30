package com.hxy.recipe.kafka.model;

import com.hxy.recipe.util.JsonUtil;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonSerializer implements Serializer<Event> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, Event data) {
        String json = JsonUtil.toJson(data);
        return json.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void close() {
    }

}
