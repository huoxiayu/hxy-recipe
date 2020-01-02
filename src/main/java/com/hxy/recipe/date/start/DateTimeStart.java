package com.hxy.recipe.date.start;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class DateTimeStart {

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

    }

}
