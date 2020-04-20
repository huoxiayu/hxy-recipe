package com.hxy.algo.indexbooleanexpression;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * 流量画像，包含一组属性
 */
@Getter
@ToString
public class Assignment {

    private final List<Attribute> attributeList;

    public Assignment(Attribute... attributes) {
        this.attributeList = List.of(attributes);
    }

}
