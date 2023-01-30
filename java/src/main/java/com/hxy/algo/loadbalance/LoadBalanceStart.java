package com.hxy.algo.loadbalance;

import com.hxy.recipe.util.LogUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Slf4j
public class LoadBalanceStart {

    public static class ScrollKey {
        private List<Object> keys;
        private int idx = -1;

        public ScrollKey(List<Object> keys) {
            this.keys = keys;
        }

        public Object next() {
            idx = (idx + 1) % keys.size();
            return keys.get(idx);
        }
    }

    public static void main(String[] args) {
        List<ServiceInstance> services = List.of(
            new ServiceInstance("127.0.0.1", 6666, 1),
            new ServiceInstance("127.0.0.1", 7777, 2),
            new ServiceInstance("127.0.0.1", 8888, 3)
        );

        ScrollKey scrollKey = new ScrollKey(List.of(1, 2));

        run(new RandomLoadBalance(services), scrollKey);
        run(new RoundRobinLoadBalance(services), scrollKey);
        run(new HashLoadBalance(services), scrollKey);
        run(new WeightedRandomLoadBalance(services), scrollKey);
    }

    private static void run(LoadBalance loadBalance, ScrollKey scrollKey) {
        Map<ServiceInstance, AtomicInteger> instanceCnt = new HashMap<>();
        IntStream.rangeClosed(1, 300_0000).forEach(ignore -> {
            ServiceInstance serviceInstance = loadBalance.choose(scrollKey.next());
            instanceCnt.computeIfAbsent(serviceInstance, k -> new AtomicInteger()).getAndIncrement();
        });

        log.info(loadBalance.getClass().getSimpleName() + ":" + instanceCnt);

        LogUtil.newLine();
    }

}
