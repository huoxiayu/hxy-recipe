package com.hxy.recipe.rpc.protostuff;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtoStuffStart {

    public static void main(String[] args) {
        Student student = new Student();
        student.setName("Hello Proto Stuff!");
        student.setAge(3);

        byte[] bytes = ProtoStuffUtil.serialize(student);
        log.info("serialize to: {}", bytes);

        Student readStudent = ProtoStuffUtil.deserialize(bytes, Student.class);
        log.info("deserialize to: {}", readStudent);
    }

    @ToString
    @Data
    private static class Student {
        private String name;
        private int age;
    }

}
