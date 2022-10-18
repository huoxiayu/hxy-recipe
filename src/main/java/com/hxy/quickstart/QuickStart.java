package com.hxy.quickstart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class QuickStart {

    public static void main(String[] args) {
        List<Bean> beanList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            beanList.add(new Bean(i, i));
        }

        System.out.println(beanList);

        List<Bean> subList = beanList.subList(5, 10);
        System.out.println(subList);
        System.out.println(beanList.size());

        subList.clear();
        System.out.println(beanList);
        System.out.println(beanList.size());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Bean {
        private int a;
        private long b;
    }

}
