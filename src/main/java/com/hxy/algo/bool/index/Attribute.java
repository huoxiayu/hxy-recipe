package com.hxy.algo.bool.index;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 属性，包含属性类别和值
 */
@Getter
@AllArgsConstructor
public class Attribute {

    public static final Attribute DEFAULT_ATTRIBUTE = new Attribute("Z", AttributeCategory.Z);

    private final String value;
    private final AttributeCategory category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attribute attribute = (Attribute) o;

        if (!value.equals(attribute.value)) return false;
        return category == attribute.category;
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + category.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("Att(%s:%s)", category, value);
    }

}
