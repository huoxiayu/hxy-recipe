package com.hxy.recipe.kafka.producer;

import com.hxy.recipe.kafka.KafkaConstants;
import com.hxy.recipe.kafka.model.Event;
import com.hxy.recipe.kafka.model.JsonSerializer;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

@Slf4j
public class KafkaProducerStart {

    private static void produce() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        KafkaProducer<String, Event> producer = new KafkaProducer<>(props);
        while (true) {
            Event event = Event.randomEvent();
            log.info("send msg {}", event);
            producer.send(new ProducerRecord<>(KafkaConstants.TOPIC, event));
            producer.flush();
            Utils.sleepInSeconds(1L);
        }
    }

    public static void main(String[] args) {
        KafkaProducerStart.produce();
    }

}
