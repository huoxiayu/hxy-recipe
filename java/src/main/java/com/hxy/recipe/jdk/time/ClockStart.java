package com.hxy.recipe.jdk.time;

import com.hxy.recipe.util.LogUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.ZoneId;

@Slf4j
public class ClockStart {

    public static void main(String[] args) {
        // utc
        Clock systemUTC = Clock.systemUTC();
        log.info("systemUTC zone {}", systemUTC.getZone());
        log.info("systemUTC instant {}", systemUTC.instant());
        log.info("systemUTC millis {}", systemUTC.millis());

        LogUtil.newLine();

        // asia/shanghai
        Clock systemDefaultZone = Clock.systemDefaultZone();
        log.info("systemDefaultZone zone {}", systemDefaultZone.getZone());
        log.info("systemDefaultZone instant {}", systemDefaultZone.instant());
        log.info("systemDefaultZone millis {}", systemDefaultZone.millis());

        LogUtil.newLine();

        log.info("tickMillis instant {}", Clock.tickMillis(ZoneId.systemDefault()).instant());
        log.info("tickMillis millis {}", Clock.tickMillis(ZoneId.systemDefault()).millis());

        LogUtil.newLine();

        log.info("tickSeconds instant {}", Clock.tickSeconds(ZoneId.systemDefault()).instant());
        log.info("tickSeconds millis {}", Clock.tickSeconds(ZoneId.systemDefault()).millis());

        LogUtil.newLine();

        log.info("tickMinutes instant {}", Clock.tickMinutes(ZoneId.systemDefault()).instant());
        log.info("tickMinutes millis {}", Clock.tickMinutes(ZoneId.systemDefault()).millis());
    }

}
