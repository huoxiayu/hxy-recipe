package com.hxy.algo.loadbalance;

import java.util.List;

public class WeightedRoundRobinLoadBalance extends AbstractLoadBalance {

    public WeightedRoundRobinLoadBalance(List<ServiceInstance> serviceInstanceList) {
        super(serviceInstanceList);
    }

    @Override
    public ServiceInstance choose(Object key) {
        return null;
    }

}
