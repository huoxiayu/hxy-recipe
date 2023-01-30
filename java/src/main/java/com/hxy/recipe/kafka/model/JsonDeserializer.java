package com.hxy.recipe.kafka.model;

import com.hxy.recipe.util.JsonUtil;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class JsonDeserializer implements Deserializer<Event> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public Event deserialize(String topic, byte[] data) {
        String json = new String(data);
        return JsonUtil.fromJson(Event.class, json);
    }

    @Override
    public void close() {
    }

}
