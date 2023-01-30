package com.hxy.recipe.convert;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

@Slf4j
public class ModelMapperStart {

    @Data
    private static class A {
        private String x;
        private int y;
        private boolean z;
    }

    @Data
    private static class B {
        private String x;
        private int y;
        private boolean z;
    }

    public static void main(String[] args) {
        ModelMapper modelMapper = new ModelMapper();
        A a = new A();
        a.setX("x");
        a.setY(0);
        a.setZ(true);
        B b = modelMapper.map(a, B.class);
        log.info(b.toString());
    }

}
