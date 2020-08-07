package com.hxy.recipe.jdk.time;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Slf4j
public class TimeStart {

    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        log.info("localDateTime is: {}", localDateTime);

        LocalDateTime beforeLocalDateTime = localDateTime.minusDays(3L);
        log.info("beforeLocalDateTime is: {}", beforeLocalDateTime);

        LocalDate beforeLocalDate = beforeLocalDateTime.toLocalDate();
        log.info("beforeLocalDate is: {}", beforeLocalDate);

        long epochDay = beforeLocalDate.toEpochDay();
        log.info("epochDay is: {}", epochDay);

        log.info("year is: {}", beforeLocalDate.getYear());
        log.info("month is: {}", beforeLocalDate.getMonth().getValue());
        log.info("day is: {}", beforeLocalDate.getDayOfMonth());

        String formatDay = beforeLocalDate.format(DateTimeFormatter.ofPattern("yyyyMMDD"));
        log.info("formatDay is: {}", formatDay);
        log.info("formatDay is: {}", Integer.parseInt(formatDay));

        LocalDate localDate = LocalDate.now();
        log.info("localDate is: {}", localDate);
        log.info("localDate.atStartOfDay: {}", localDate.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli());

        int date = 20200810;
        LocalDate fromDate = LocalDate.parse(String.valueOf(date), DateTimeFormatter.ofPattern("yyyyMMdd"));
        log.info("fromDate: {}", fromDate);
    }

}
