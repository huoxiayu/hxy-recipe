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

import java.util.List;
import java.util.Properties;

@Slf4j
public class KafkaProducerStart {

    private static void produce() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        KafkaProducer<String, Event> producer = new KafkaProducer<>(props);

        int idx = 0;
        List<Event> eventList = List.of(
            new Event("app", "event", 1, System.currentTimeMillis()),
            new Event("app", "event", 2, System.currentTimeMillis()),
            new Event("app", "event", 3, System.currentTimeMillis()),
            new Event("app", "event", 4, System.currentTimeMillis())
        );
        boolean random = Boolean.valueOf(System.getProperty("random"));
        while (true) {
            Event event = random ? Event.randomEvent() : eventList.get((idx++) % eventList.size());
            log.info("send msg {}", event);
            producer.send(new ProducerRecord<>(KafkaConstants.TOPIC, event));
            producer.flush();
            Utils.sleepInSeconds(Long.parseLong(System.getProperty("timeInSeconds")));
        }
    }

    public static void main(String[] args) {
        System.setProperty("timeInSeconds", "1");
        System.setProperty("random", "true");
        KafkaProducerStart.produce();
    }

}
