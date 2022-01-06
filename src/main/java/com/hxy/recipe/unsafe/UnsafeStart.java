package com.hxy.recipe.unsafe;

import lombok.extern.slf4j.Slf4j;
import jdk.internal.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * IDEA vm options add below line:
 * --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED
 */
@Slf4j
public class UnsafeStart {

    private static class A {
        private A() {
            throw new RuntimeException("not support A");
        }

        @Override
        public String toString() {
            return "A:" + super.toString();
        }
    }

    public static void main(String[] args) throws InstantiationException {
        Unsafe unsafe = getUnsafe();
        log.info("unsafe {}", unsafe);

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

    // get unsafe by reflect
    private static Unsafe getUnsafe() {
        try {
            return Unsafe.getUnsafe();
        } catch (Exception e) {
            log.error("direct get unsafe fail:", e);
            try {
                Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                return (Unsafe) field.get(null);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
