package com.hxy.recipe;

import com.hxy.recipe.springbootext.DynamicProperty;
import com.hxy.recipe.springbootext.EnableDynamicProperty;
import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@EnableDynamicProperty
@Slf4j
@Component
public class SimpleBean implements CommandLineRunner {

    @DynamicProperty("foo")
    private String foo = "first foo";

    @Override
    public void run(String... args) {
        int loop = 5;
        while (loop-- > 0) {
            log.info("loop: {}, foo: {}", loop, foo);

            if ((loop & 1) == 0) {
                String value = UUID.randomUUID().toString();
                log.info("set to {}", value);
                System.setProperty("foo", value);
            }

            Utils.sleepInSeconds(10L);
        }
        log.info("end");
    }

}
