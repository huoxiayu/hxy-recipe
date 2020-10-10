package com.hxy.recipe.string;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class StringStart {

    public static void main(String[] args) {
        String java = "java";
        String newJavaIntern = new String("java").intern();
        String newJava = new String("java");

        log.info("{}", java == newJavaIntern);
        log.info("{}", java == newJava);
        log.info("{}", newJavaIntern == newJava);

        log.info("{}", java.equals(newJavaIntern));
        log.info("{}", java.equals(newJava));
        log.info("{}", newJavaIntern.equals(newJava));

        Object object = getValueForString(java);
        // value is byte[] in jdk11
        log.info("{}, {}", object.getClass().getName());

        log.info("{}", getValueForString(java) == getValueForString(newJavaIntern));
        log.info("{}", getValueForString(java) == getValueForString(newJava));
        log.info("{}", getValueForString(newJavaIntern) == getValueForString(newJava));
    }

    private static Object getValueForString(String string) {
        try {
            Field field = String.class.getDeclaredField("value");
            field.setAccessible(true);
            return field.get(string);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
