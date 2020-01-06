package com.hxy.recipe.mybatis.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Student {

    private int id;
    private String name;
    private String hobby;
    private String country;
    private boolean offline;

}
