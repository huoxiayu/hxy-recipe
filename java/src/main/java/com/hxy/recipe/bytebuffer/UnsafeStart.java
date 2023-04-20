package com.hxy.recipe.bytebuffer;

import com.hxy.recipe.util.Utils;
import jdk.internal.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * IDEA Perferences -> Compiler -> Java Compiler add below line:
 * --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED
 */
public class UnsafeStart {

    private static final Unsafe unsafe = Utils.getUnsafe();
    private static final int len = 16;
    private static final ByteBuffer bb = ByteBuffer.allocateDirect(len * 4);
    private static final Field f;

    static {
        try {
            f = Buffer.class.getDeclaredField("address");
            f.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        bb.order(ByteOrder.LITTLE_ENDIAN);
    }

    public static void main(String[] args) throws IllegalAccessException {
        long address = (long) f.get(bb);
        for (int i = 0; i < len; i++) {
            int x = Float.floatToRawIntBits(i * 1.0F);
            unsafe.putIntUnaligned(
                    null,
                    address + (i << 2),
                    x,
                    false
            );
        }

        for (int i = 0; i < len; i++) {
            System.out.println(bb.getFloat());
        }

        System.out.println("********************");

        bb.rewind();
        for (int i = 0; i < len; i++) {
            bb.putFloat(i << 2, i * 1.0F);
        }

        for (int i = 0; i < len; i++) {
            System.out.println(bb.getFloat());
        }
    }

}
