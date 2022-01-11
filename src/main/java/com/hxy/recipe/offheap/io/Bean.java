package com.hxy.recipe.offheap.io;

import lombok.Data;

@Data
public class Bean {

    private int outerInt;
    private long outerLong;
    private boolean outerBool;
    private double outerDouble;
    private String outerString;
    private InnerBean innerBean;

}
