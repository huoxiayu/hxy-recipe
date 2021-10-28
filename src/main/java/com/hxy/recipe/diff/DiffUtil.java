package com.hxy.recipe.diff;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.myers.MyersDiff;
import com.github.difflib.patch.Patch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiPredicate;

@Slf4j
public class DiffUtil {

    @AllArgsConstructor
    @Data
    private static class Bean {
        private String strVal1;
        private String strVal2;
        private int intVal1;
        private int intVal2;
        private long longVal1;
        private long longVal2;
        private double doubleVal1;
        private double doubleVal2;
        private boolean boolVal1;
        private boolean boolVal2;
    }

    public static void main(String[] args) {
        log.info("diff1 -> {}", diff(List.of(), List.of()));
        log.info("diff2 -> {}", diff(List.of(newBean()), List.of(newBean())));
    }

    private static Bean newBean() {
        return new Bean(
                "str",
                UUID.randomUUID().toString(),
                1,
                ThreadLocalRandom.current().nextInt(),
                1L,
                ThreadLocalRandom.current().nextLong(),
                1.0D,
                ThreadLocalRandom.current().nextDouble(),
                true,
                ThreadLocalRandom.current().nextBoolean()
        );
    }

    private static <T> Patch<T> diff(List<T> left,
                                     List<T> right) {
        return diff(left, right, null);
    }

    private static <T> Patch<T> diff(List<T> left,
                                     List<T> right,
                                     BiPredicate<T, T> equalizer) {
        MyersDiff<T> diff = equalizer == null ? new MyersDiff<>() : new MyersDiff<>(equalizer);
        return DiffUtils.diff(left, right, diff);
    }

}
