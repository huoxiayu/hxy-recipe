package com.hxy.recipe.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Event {

    public static final List<String> sourceList = List.of("app1", "app2", "app3");
    public static final List<String> eventList = List.of("view", "click", "download");

    private String source;
    private String event;
    private int times;

    public static Event randomEvent() {
        String source = sourceList.get(ThreadLocalRandom.current().nextInt(sourceList.size()));
        String event = eventList.get(ThreadLocalRandom.current().nextInt(eventList.size()));
        // [1, 10)
        int times = ThreadLocalRandom.current().nextInt(1, 10);
        return new Event(source, event, times);
    }

}
