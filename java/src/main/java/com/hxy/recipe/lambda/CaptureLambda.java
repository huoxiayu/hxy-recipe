package com.hxy.recipe.lambda;

import java.util.function.Consumer;

/**
 * VM options add:
 * -Djdk.internal.lambda.dumpProxyClasses=/Users/tmp
 */
public class CaptureLambda {

    public static void main(String[] args) {
        String prefix = "Prefix:";
        Consumer<Object> consumer = p -> print(prefix + p);
        consumer.accept("content");
    }

    private static void print(Object obj) {
        System.out.println(obj);
    }

}
