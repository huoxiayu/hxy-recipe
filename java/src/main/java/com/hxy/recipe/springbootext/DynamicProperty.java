package com.hxy.recipe.springbootext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicProperty {

    String value();

    DynamicPropertyParser parser() default DynamicPropertyParser.STRING_PARSER;

}
