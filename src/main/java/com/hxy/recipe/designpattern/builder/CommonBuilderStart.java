package com.hxy.recipe.designpattern.builder;

import lombok.Data;

public class CommonBuilderStart {

    @Data
    private static class Student {
        private String name;
        private int score;
    }

    public static void main(String[] args) {
        Student student = Builder.newBuilder(new Student())
            .with(Student::setName, "hxy")
            .with(Student::setScore, 100)
            .build();
        System.out.println(student);
    }

}
