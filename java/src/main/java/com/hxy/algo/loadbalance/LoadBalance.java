package com.hxy.algo.loadbalance;

import java.util.List;

public interface LoadBalance {

    List<ServiceInstance> getAllService();

    ServiceInstance choose(Object key);

}
