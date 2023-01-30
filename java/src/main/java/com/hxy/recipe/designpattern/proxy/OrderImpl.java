package com.hxy.recipe.designpattern.proxy;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderImpl implements OrderService {

    @Override
    public void order() {
        Utils.randomSleepInMillis();
        log.info("order impl");
    }

}
