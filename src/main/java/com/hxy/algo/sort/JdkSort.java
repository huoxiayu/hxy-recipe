package com.hxy.algo.sort;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class JdkSort {

    @ToString
    @AllArgsConstructor
    @Data
    private static class Bean {
        private Integer id;
    }

    public static void main(String[] args) {
        List<Bean> beans = new ArrayList<>();
        beans.add(new Bean(null));
        beans.add(new Bean(1));
        beans.add(new Bean(2));

        log.info("beans: {}", beans);
        beans.sort(Comparator.nullsFirst(Comparator.comparing(Bean::getId)));
        log.info("after sort: {}", beans);
    }

}
