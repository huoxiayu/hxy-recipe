package com.hxy.quickstart;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

@Slf4j
public class QuickStart {

    public static void main(String[] args) {
        StopWatch sw = new StopWatch();

        sw.start();
        Utils.sleepInMillis(100L);
        sw.stop();

        sw.start();
        Utils.sleepInMillis(200L);
        sw.stop();

        log.info("last task time millis {}", sw.getLastTaskTimeMillis());
        log.info("total millis {}", sw.getTotalTimeMillis());
    }

}
