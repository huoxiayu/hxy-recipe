package com.hxy.algo.bool.index;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

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
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Attribute that = (Attribute) o;
        return Objects.equals(this.value, that.value) && Objects.equals(this.category, that.category);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(value);
        result = 31 * result + Objects.hashCode(category);
        return result;
    }

    @Override
    public String toString() {
        return String.format("Att(%s:%s)", category, value);
    }

}
