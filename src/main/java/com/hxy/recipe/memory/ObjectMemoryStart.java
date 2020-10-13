package com.hxy.recipe.memory;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * jol（java object layout）工具实践
 */
@Slf4j
public class ObjectMemoryStart {

    private static class A {

    }

    private static class B {
        private long useless;
    }

    private static class C {
        private boolean useless;
    }

    public static void main(String[] args) {
        A a = new A();
        log.info("a -> {}", ClassLayout.parseInstance(a).toPrintable());

        B b = new B();
        log.info("b -> {}", ClassLayout.parseInstance(b).toPrintable());

        C c = new C();
        log.info("c -> {}", ClassLayout.parseInstance(c).toPrintable());
    }

}
