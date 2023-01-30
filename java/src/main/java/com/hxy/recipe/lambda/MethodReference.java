package com.hxy.recipe.lambda;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

@Slf4j
public class MethodReference {

    public static void main(String[] args) {
        Function<Integer, Integer> plusOne = i -> i + 1;
        log.info("{}", plusOne.apply(5));
    }

}
