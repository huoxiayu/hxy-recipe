package com.hxy.algo.bool.index;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流量画像，包含一组属性
 */
@Getter
public class Assignment {

    private final List<Attribute> attributeList;

    public Assignment(Attribute... attributes) {
        this.attributeList = Arrays.stream(attributes).collect(Collectors.toList());
        this.attributeList.add(Attribute.DEFAULT_ATTRIBUTE);
    }

    @Override
    public String toString() {
        return String.format("Assignment(%s)", attributeList.toString());
    }

}
