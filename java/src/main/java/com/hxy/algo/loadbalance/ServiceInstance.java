package com.hxy.algo.loadbalance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class ServiceInstance {

    private final String host;
    private final int port;
    private final int weight;

}
