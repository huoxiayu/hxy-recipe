package com.hxy.algo.indexbooleanexpression;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 一个布尔条件
 * isInclude=true，attributeList=[age=18, age=30]代表age=18 || age=30
 * isInclude=false，attributeList=[age=18, age=30]代表!(age=18 || age=30)
 */
@Getter
@AllArgsConstructor
public class Clause {

    private final boolean isInclude;
    private final List<Attribute> attributeList;

    @Override
    public String toString() {
        String symbol = isInclude ? "∈" : "∉";
        return attributeList.stream()
            .map(attribute -> attribute.getCategory() + symbol + attribute.getValue())
            .collect(Collectors.joining("(", ",", ")" ));
    }

}
