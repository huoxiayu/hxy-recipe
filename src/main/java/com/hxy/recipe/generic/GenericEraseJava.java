package com.hxy.recipe.generic;

import lombok.SneakyThrows;

/**
 * @see com.hxy.recipe.start.GenericKotlin
 */
public class GenericEraseJava {

    @SneakyThrows
    static <T> T getT(Class<T> clazz)  {
        return clazz.newInstance();
    }

    // 编译报错
    // public static <T extends Number> Class<T> check(T value) {
    //     return value.getClass();
    // }

}


