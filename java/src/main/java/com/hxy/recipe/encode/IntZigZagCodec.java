package com.hxy.recipe.encode;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;

/**
 * vm options: -ea -> enable assert
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IntZigZagCodec {

    public static int zigzagToInt(int n) {
        return (n >>> 1) ^ -(n & 1);
    }

    public static int intToZigzag(int n) {
        return (n << 1) ^ (n >> 31);
    }

    public static void main(String[] args) {
        IntStream.rangeClosed(-8, 7).forEach(IntZigZagCodec::processI);

        processI(Integer.MAX_VALUE);

        processI(Integer.MIN_VALUE);
    }

    private static void processI(int i) {
        log.info("i -> {}, binary -> {}", i, Integer.toBinaryString(i));

        int zigzag = intToZigzag(i);
        log.info("zigzag -> {}, binary -> {}", zigzag, Integer.toBinaryString(zigzag));

        assert zigzagToInt(zigzag) == i;
    }

}
