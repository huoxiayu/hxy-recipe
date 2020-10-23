package com.hxy.recipe.memory;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * jol（java object layout）工具实践
 */
@Slf4j
public class JOLStart {

    private static class A {
        private boolean uselessBoolean;
        private String uselessString;
    }

    private static class B extends A {
        private int uselessInteger;
    }

    private static class C extends B {
        private String uselessString;
    }

    public static void main(String[] args) {
        printObjectLayout(new A());
        printObjectLayout(new B());
        printObjectLayout(new C());
    }

    private static void printObjectLayout(Object object) {
        log.info(ClassLayout.parseInstance(object).toPrintable() + "\n");
    }

}
