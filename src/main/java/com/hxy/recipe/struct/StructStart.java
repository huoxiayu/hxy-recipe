package com.hxy.recipe.struct;

import com.hxy.recipe.util.Utils;
import jdk.internal.misc.Unsafe;

/**
 * IDEA Perferences -> Compiler -> Java Compiler add below line:
 * --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED
 */
public class StructStart {

    public static void main(String[] args) {
        byte[] bytes = new byte[1024];
        for (int i = 0; i < 1024; i++) {
            bytes[i] = (byte) i;
        }

        Unsafe unsafe = Utils.getUnsafe();
        int arrayBaseOffset = unsafe.arrayBaseOffset(byte[].class);

        long l1 = unsafe.getLong(bytes, arrayBaseOffset);
        System.out.println(l1);
        System.out.println(Long.toBinaryString(l1));

        long l2 = unsafe.getLong(bytes, arrayBaseOffset + 8);
        System.out.println(l2);
        System.out.println(Long.toBinaryString(l2));
    }

}
