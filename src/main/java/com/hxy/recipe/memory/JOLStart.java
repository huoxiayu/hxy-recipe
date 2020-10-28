package com.hxy.recipe.memory;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * jol（java object layout）工具实践
 * 1、java -XX:+PrintCommandLineFlags
 * 打印默认JVM参数，可以看到：
 * -XX:+UseCompressedClassPointers -XX:+UseCompressedOops
 * class-point占4个字节，oops占4个字节
 * 关闭UseCompressedClassPointers后，class-point占8个字节
 * 关闭UseCompressedOops后，oops占8个字节
 * 关闭用：-XX:-UseCompressedClassPointers -XX:-UseCompressedOops
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
        int[] oneDimArray = new int[16];
        printObjectLayout(oneDimArray);

        int[][] twoDimArray = new int[8][2];
        for (int i = 0; i < 8; i++) {
            twoDimArray[i] = new int[2];
        }
        printObjectLayout(twoDimArray);
        printObjectLayout(twoDimArray[0][0]);

        printObjectLayout(0);
        printObjectLayout(0L);

        printObjectLayout(new A());
        printObjectLayout(new B());
        printObjectLayout(new C());
    }

    private static void printObjectLayout(Object object) {
        log.info(ClassLayout.parseInstance(object).toPrintable() + "\n");
    }

}
