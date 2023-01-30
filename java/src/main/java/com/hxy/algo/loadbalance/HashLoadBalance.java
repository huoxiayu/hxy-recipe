package com.hxy.algo.loadbalance;

import java.util.List;
import java.util.Objects;

public class HashLoadBalance extends AbstractLoadBalance {

    public HashLoadBalance(List<ServiceInstance> serviceInstanceList) {
        super(serviceInstanceList);
    }

    @Override
    public ServiceInstance choose(Object key) {
        List<ServiceInstance> serviceInstanceList = getAllService();
        int idx = Objects.hashCode(key) % serviceInstanceList.size();
        return serviceInstanceList.get(idx);
    }

}
