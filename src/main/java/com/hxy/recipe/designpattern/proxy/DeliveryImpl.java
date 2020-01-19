package com.hxy.recipe.designpattern.proxy;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeliveryImpl implements DeliveryService {

    @Override
    public void delivery() {
        Utils.randomSleepInMillis();
        log.info("delivery impl");
    }

}
