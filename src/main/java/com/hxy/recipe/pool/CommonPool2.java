package com.hxy.recipe.pool;

import org.apache.commons.pool2.impl.GenericKeyedObjectPool;

public class CommonPool2 {

    public static void main(String[] args) {
        GenericKeyedObjectPool gkop = new GenericKeyedObjectPool(
                null
        );
    }

}
