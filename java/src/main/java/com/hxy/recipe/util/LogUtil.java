package com.hxy.recipe.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LogUtil {

    public static void newLine() {
        log.info("<-------------------------------------------------->");
    }

    public static void newLine(String prompt) {
        log.info("<-------------------------------------------------->");
        log.info(prompt);
    }

}
