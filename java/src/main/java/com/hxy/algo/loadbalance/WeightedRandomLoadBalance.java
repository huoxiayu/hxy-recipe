package com.hxy.algo.loadbalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class WeightedRandomLoadBalance extends AbstractLoadBalance {

    private final int[] weightArray;
    private final int weightSum;

    public WeightedRandomLoadBalance(List<ServiceInstance> serviceInstanceList) {
        super(serviceInstanceList);
        this.weightArray = serviceInstanceList.stream().mapToInt(ServiceInstance::getWeight).toArray();
        for (int i = 1; i < weightArray.length; i++) {
            weightArray[i] = weightArray[i] + weightArray[i - 1];
        }
        this.weightSum = weightArray[weightArray.length - 1];
    }

    @Override
    public ServiceInstance choose(Object key) {
        int rand = ThreadLocalRandom.current().nextInt(weightSum);
        for (int i = 0; i < weightArray.length; i++) {
            if (rand < weightArray[i]) {
                return getAllService().get(i);
            }
        }

        throw new RuntimeException("never go here");
    }

}
