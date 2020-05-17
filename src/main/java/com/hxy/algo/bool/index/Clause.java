package com.hxy.algo.bool.index;

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

    public static final Clause DEFAULT_CLAUSE = new Clause(true, List.of(Attribute.DEFAULT_ATTRIBUTE));

    // 包含 or 排除
    private final boolean isInclude;
    // 包含 or 排除的属性集合
    private final List<Attribute> attributeList;

    public boolean isExclude() {
        return !isInclude;
    }

    @Override
    public String toString() {
        String symbol = isInclude ? "∈" : "∉";
        return attributeList.stream()
            .map(attribute -> attribute.getCategory() + symbol + attribute.getValue())
            .sorted()
            .collect(Collectors.joining("|"));
    }

    public static String clauseList2String(List<Clause> clauseList) {
        return clauseList.stream()
            .map(Clause::toString)
            .sorted()
            .collect(Collectors.joining("&"));
    }

}
