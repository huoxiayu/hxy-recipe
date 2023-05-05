package com.hxy.quickstart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

public class LombokStart {

    @Data
    @ToString(onlyExplicitlyIncluded = true)
    @AllArgsConstructor
    private static class Bean {
        @ToString.Include
        private int a;
        private double b;
        private String c;
    }

    public static void main(String[] args) {
        Bean b = new Bean(1, 1.11D, "111111");
        System.out.println(b);
    }

}
