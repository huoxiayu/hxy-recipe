package com.hxy.algo.loadbalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalance extends AbstractLoadBalance {

    private final AtomicInteger atomicInteger = new AtomicInteger();

    public RoundRobinLoadBalance(List<ServiceInstance> serviceInstanceList) {
        super(serviceInstanceList);
    }

    @Override
    public ServiceInstance choose(Object key) {
        List<ServiceInstance> serviceInstanceList = getAllService();
        int idx = atomicInteger.getAndIncrement() % serviceInstanceList.size();
        return serviceInstanceList.get(idx);
    }

}
