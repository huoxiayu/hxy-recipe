package com.hxy.recipe.template;

import org.stringtemplate.v4.ST;

public class StringTemplateStart {

    public static void main(String[] args) {
        ST hello = new ST("Hello, <name>");
        hello.add("name", "World");
        System.out.println(hello.render());
    }

}
