package com.hxy.algo.loadbalance;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance {

    private final List<ServiceInstance> serviceInstanceList;

    public AbstractLoadBalance(List<ServiceInstance> serviceInstanceList) {
        this.serviceInstanceList = serviceInstanceList;
    }

    @Override
    public List<ServiceInstance> getAllService() {
        return serviceInstanceList;
    }

    @Override
    public abstract ServiceInstance choose(Object key);

}
