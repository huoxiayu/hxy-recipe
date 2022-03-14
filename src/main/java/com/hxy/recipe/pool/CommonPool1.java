package com.hxy.recipe.pool;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;

public class CommonPool1 {

    public static void main(String[] args) {
        GenericKeyedObjectPool gkop = new GenericKeyedObjectPool(
                null
        );
    }

}
