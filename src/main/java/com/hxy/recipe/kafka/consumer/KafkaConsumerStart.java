package com.hxy.recipe.kafka.consumer;

import com.hxy.recipe.kafka.KafkaConstants;
import com.hxy.recipe.kafka.model.Event;
import com.hxy.recipe.kafka.model.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
public class KafkaConsumerStart {

    private static void consume() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-consumer-group");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        // value default between [group.min.session.timeout.ms = 6000, group.max.session.timeout.ms = 300000]
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "100000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        KafkaConsumer<String, Event> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of(KafkaConstants.TOPIC));
        while (true) {
            ConsumerRecords<String, Event> records = consumer.poll(Duration.ofSeconds(5));
            for (ConsumerRecord<String, Event> record : records) {
                log.info("offset: {}, key: {}, value: {}", record.offset(), record.key(), record.value());
            }
        }
    }

    public static void main(String[] args) {
        KafkaConsumerStart.consume();
    }

}
