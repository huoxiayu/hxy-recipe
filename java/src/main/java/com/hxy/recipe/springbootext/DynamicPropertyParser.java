package com.hxy.recipe.springbootext;

public enum DynamicPropertyParser {

    STRING_PARSER {
        @Override
        Object parse(String value) {
            return value;
        }
    };

    abstract Object parse(String value);

}
