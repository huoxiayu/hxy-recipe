package com.hxy.recipe.kafka.model;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class JsonSerde implements Serde<Event> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public void close() {
    }

    @Override
    public Serializer<Event> serializer() {
        return new JsonSerializer();
    }

    @Override
    public Deserializer<Event> deserializer() {
        return new JsonDeserializer();
    }

}
