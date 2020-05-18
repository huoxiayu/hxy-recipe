package com.hxy.recipe.kafka.streaming.start;

import com.hxy.recipe.kafka.KafkaConstants;
import com.hxy.recipe.kafka.model.Event;
import com.hxy.recipe.kafka.model.JsonSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class KafkaStreamingStart {

    private static void streaming() {
        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "hxy-kafka-streaming");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        final StreamsBuilder builder = new StreamsBuilder();
        final KStream<String, Event> source = builder.stream(KafkaConstants.TOPIC);
        final KTable<Windowed<String>, Long> counts = source
            .groupBy((key, value) -> value.getSource() + "-" + value.getEvent())
            .windowedBy(TimeWindows.of(5000L))
            .count();

        counts.toStream().foreach((key, value) -> log.info("key: {}, value: {}", key, value));
        // counts.toStream().to("to-topic", Produced.with(Serdes.String(), Serdes.Long()));

        final KafkaStreams streams = new KafkaStreams(builder.build(), props);
        final CountDownLatch latch = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread("clean-up-thread") {
            @Override
            public void run() {
                latch.countDown();
                streams.close();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (final Throwable e) {
            log.error("error: {}", e);
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        streaming();
    }

}
