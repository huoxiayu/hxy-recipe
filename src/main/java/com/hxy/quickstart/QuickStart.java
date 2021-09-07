package com.hxy.quickstart;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class QuickStart {

    public static void main(String[] args) {
        String client_id = "some_client_id";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = String.valueOf(ThreadLocalRandom.current().nextLong(100));
        String secure_key = "some_secure_key";

        List<String> keys = new ArrayList<>();
        keys.add(client_id);
        keys.add(timestamp);
        keys.add(nonce);
        keys.add(secure_key);

        Collections.sort(keys);

        String joinKey = String.join("", keys);
        String sign = DigestUtils.sha1Hex(joinKey);
        System.out.println(sign);






        log.info("", ExceptionUtils.getStackTrace(new Exception()));

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
