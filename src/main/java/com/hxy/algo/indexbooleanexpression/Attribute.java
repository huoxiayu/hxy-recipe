package com.hxy.algo.indexbooleanexpression;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 属性，包含属性类别和值
 */
@Getter
@AllArgsConstructor
@ToString
public class Attribute {

    private final String value;
    private final AttributeCategory category;

}
