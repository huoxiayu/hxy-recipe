package com.hxy.recipe.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class StreamStart {

    @Data
    @AllArgsConstructor
    private static class Bean {
        private String key;
        private List<String> values;
    }

    public static void main(String[] args) {
        collect2Map();
        peek();
    }

    private static void collect2Map() {
        List<Bean> beans = List.of(
            new Bean("key1", List.of("value1", "values11")),
            new Bean("key1", List.of("value111", "values1111")),
            new Bean("key2", List.of("value2")),
            new Bean("key2", List.of("value2")),
            new Bean("key3", List.of("value3"))
        );
        log.info("{}", beans);

        Map<String, List<String>> key2Values =
            beans.stream().collect(Collectors.toMap(Bean::getKey, Bean::getValues, (values1, values2) -> {
                List<String> merge = new ArrayList<>(values1);
                merge.addAll(values2);
                return merge;
            }));
        log.info("{}", key2Values);
    }

    private static void peek() {
        List<Bean> beans = List.of(new Bean("k1", List.of("v1")));
        log.info("{}", beans);

        List<Bean> result = beans.stream().peek(b -> b.setKey(b.getKey() + ".key")).collect(Collectors.toList());
        log.info("{}", result);
    }

}
