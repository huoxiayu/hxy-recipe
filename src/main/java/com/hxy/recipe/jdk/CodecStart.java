package com.hxy.recipe.jdk;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class CodecStart {

    public static void main(String[] args) {
        List<String> list = List.of("哈", "喽", "沃", "德");
        list.forEach(str -> log.info("bytes for {} -> {}", str, str.getBytes()));
        log.info("<---------->");
        list.forEach(str -> log.info("bytes for {} -> {}", str, str.getBytes(StandardCharsets.UTF_8)));
    }

}
