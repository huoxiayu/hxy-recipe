package com.hxy.recipe.generic;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GenericPOJO {

    private List<String> list;
    private Map<Integer, Map<String, Long>> map;

}
