package com.hxy.recipe.memory;

import jdk.internal.vm.annotation.Contended;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;
import org.openjdk.jol.vm.VirtualMachine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    private static class BoolStrObj {
        private boolean bool;
        private String str;
    }

    private static class BoolStrObjIntObj extends BoolStrObj {
        private int integer;
    }

    private static class BoolStrObjIntBoolBoolBoolObj extends BoolStrObj {
        private boolean bool1;
        private boolean bool2;
        private boolean bool3;
    }

    private static class LongIntObj {
        private long long_;
        private int int_;
    }

    @Contended
    private static class ContentOnClass {
        private int int1;

        private int int2;

        private int int3;
    }

    private static class ContentOnField {
        private int int1;

        @Contended
        private int int2;

        private int int3;
    }

    private static class ContentOnEveryField {
        @Contended
        private int int1;

        @Contended
        private int int2;

        @Contended
        private int int3;
    }

    private static class ContentOnGroup {
        @Contended("group1")
        private int int1;

        @Contended("group1")
        private int int2;

        @Contended("group2")
        private int int3;

        @Contended("group2")
        private int int4;
    }

    public static void main(String[] args) {
        // Using compressed oop with 3-bit shift.
        // Using compressed klass with 3-bit shift.
        // Objects are 8 bytes aligned.
        VirtualMachine vm = VM.current();
        log.info("{}", vm.details());

        printObjectLayout("longArray", new long[]{});

        int[] intArray16 = new int[16];
        printObjectLayout("intArray16", intArray16);

        int[][] intIntArray8x2 = new int[8][2];
        for (int i = 0; i < 8; i++) {
            intIntArray8x2[i] = new int[2];
        }
        printObjectLayout("intIntArray8x2", intIntArray8x2);

        printObjectLayout("int: 0", 0);
        printObjectLayout("long: 0L", 0L);
        printObjectLayout("uuid: String", UUID.randomUUID().toString());

        printObjectLayout("BoolStrObj", new BoolStrObj());
        printObjectLayout("BoolStrObjIntObj", new BoolStrObjIntObj());
        printObjectLayout("BoolStrObjIntBoolBoolBoolObj", new BoolStrObjIntBoolBoolBoolObj());

        // -XX:+CompactFields
        printObjectLayout("LongIntObj", new LongIntObj());

        // -XX:-RestrictContended
        printObjectLayout("ContentOnClass", new ContentOnClass());
        printObjectLayout("ContentOnField", new ContentOnField());
        printObjectLayout("ContentOnEveryField", new ContentOnEveryField());
        printObjectLayout("ContentOnGroup", new ContentOnGroup());

        printGraphLayout("Pair.of(Int, Str)", Pair.of(1L, "str"));

        Map<Integer, Map<Long, String>> int2Long2Str = new HashMap<>();
        int2Long2Str.computeIfAbsent(1, any -> new HashMap<>()).put(100L, "one-zero-zero");
        int2Long2Str.computeIfAbsent(2, any -> new HashMap<>()).put(200L, "two-zero-zero");
        printGraphLayout("int2Long2Str", int2Long2Str);
    }

    private static void printObjectLayout(String prompt, Object object) {
        log.info(" " + prompt + " with " + ClassLayout.parseInstance(object).toPrintable() + "\n");
    }

    private static void printGraphLayout(String prompt, Object object) {
        log.info(" " + prompt + " with " + GraphLayout.parseInstance(object).toPrintable() + "\n");
    }

}
