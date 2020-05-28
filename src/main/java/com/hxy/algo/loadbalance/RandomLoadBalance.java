package com.hxy.algo.loadbalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomLoadBalance extends AbstractLoadBalance {

    public RandomLoadBalance(List<ServiceInstance> serviceInstanceList) {
        super(serviceInstanceList);
    }

    @Override
    public ServiceInstance choose(Object key) {
        List<ServiceInstance> serviceInstanceList = getAllService();
        int idx = ThreadLocalRandom.current().nextInt(serviceInstanceList.size());
        return serviceInstanceList.get(idx);
    }

}
