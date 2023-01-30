package com.hxy.recipe.unsafe;

import com.hxy.recipe.util.Utils;
import jdk.internal.misc.Unsafe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * IDEA vm options add below line:
 * --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED
 */
@Slf4j
public class UnsafeStart {

    public static void main(String[] args) {
        // unsafe();

        unsafeArray();
    }

    @SneakyThrows
    private static void unsafe() {
        Unsafe unsafe = Utils.getUnsafe();

        // big-endian
        log.info("unsafe.isBigEndian() {}", unsafe.isBigEndian());

        // long-array-base-offset = 16
        int longArrayBaseOffset = unsafe.arrayBaseOffset(long[].class);
        log.info("longArrayBaseOffset: {}", longArrayBaseOffset);

        // visit long array byte by byte
        long[] longArray = {1L, 2L, 3L, 4L};
        log.info("print long array");
        for (int i = 0; i < longArray.length * 8; i++) {
            System.out.print(unsafe.getByte(longArray, longArrayBaseOffset + i));
        }
        System.out.println();

        long getLong = unsafe.getLong(longArray, longArrayBaseOffset);
        log.info("getLong: {}", getLong);

        // 小端 则 低位低地址
        byte getByte = unsafe.getByte(longArray, longArrayBaseOffset);
        log.info("getByte: {}", getByte);

        byte offsetPlus3 = unsafe.getByte(longArray, longArrayBaseOffset + 3);
        log.info("getByte with offset+3: {}", offsetPlus3);

        long offsetPlus8 = longArrayBaseOffset + 8L;
        log.info("getLong with offset+8: {}", unsafe.getLong(longArray, offsetPlus8));

        // 10000000 2000|0000 3000|0000 40000000
        // 00000000-00000000-00000000-00000000-11000000-00000000-00000000-00000000
        // 2 ** 32 + 2 ** 33 = 12884901888
        long offsetPlus12 = longArrayBaseOffset + 12L;
        long offsetPlus12Value = unsafe.getLong(longArray, offsetPlus12);
        log.info("getLong with offset+12: {}", offsetPlus12Value);
        log.info("offsetPlus12Value to binary: {}", Long.toBinaryString(offsetPlus12Value));

        // no exception throw, unsafe.allocateInstance不走构造函数
        Object o = unsafe.allocateInstance(A.class);
        log.info("o: {}", o);

        // throw exception
        new A();
    }

    private static void unsafeArray() {
        ArrayItem[] arrayItems = new ArrayItem[4];
        arrayItems[0] = new ArrayItem(1, true, "first");
        arrayItems[1] = new ArrayItem(2, false, "second");
        arrayItems[2] = null;
        arrayItems[3] = null;
        log.info("arrayItems -> {}", Arrays.toString(arrayItems));

        unsafeArrayForeach(arrayItems);
    }

    private static void unsafeArrayForeach(ArrayItem[] arrayItems) {
        Unsafe unsafe = Utils.getUnsafe();

        int arrayBaseOffset = unsafe.arrayBaseOffset(ArrayItem[].class);
        int scale = unsafe.arrayIndexScale(ArrayItem[].class);
        int aShift = 31 - Integer.numberOfLeadingZeros(scale);
        log.info("arrayBaseOffset -> {}", arrayBaseOffset);
        log.info("scale -> {}", scale);
        log.info("aShift -> {}", aShift);

        for (int i = 0; i < arrayItems.length; i++) {
            long shift = (long) i << aShift;
            long offset = arrayBaseOffset + shift;
            Object obj = unsafe.getObjectAcquire(arrayItems, offset);
            log.info("shift -> {}, obj -> {}", shift, obj);
        }

        long integerOffset = unsafe.objectFieldOffset(ArrayItem.class, "integer");
        long boolOffset = unsafe.objectFieldOffset(ArrayItem.class, "bool");
        long strOffset = unsafe.objectFieldOffset(ArrayItem.class, "str");
        log.info("integerOffSet -> {}", integerOffset);
        log.info("boolOffset -> {}", boolOffset);
        log.info("strOffset -> {}", strOffset);

        int int1 = unsafe.getInt(arrayItems, arrayBaseOffset + ((long) 0 << aShift) + integerOffset);
        int int2 = unsafe.getInt(arrayItems, arrayBaseOffset + ((long) 1 << aShift) + integerOffset);
        log.info("int1 -> {}, int2 -> {}", int1, int2);

        boolean bool1 = unsafe.getBoolean(arrayItems, arrayBaseOffset + ((long) 0 << aShift) + boolOffset);
        boolean bool2 = unsafe.getBoolean(arrayItems, arrayBaseOffset + ((long) 1 << aShift) + boolOffset);
        log.info("bool1 -> {}, bool2 -> {}", bool1, bool2);
    }

    private static class A {
        private A() {
            throw new RuntimeException("not support A");
        }

        @Override
        public String toString() {
            return "A:" + super.toString();
        }
    }

    @Data
    @AllArgsConstructor
    private static class ArrayItem {

        private final int integer;
        private final boolean bool;
        private final String str;

    }

}
