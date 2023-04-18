package com.hxy.recipe.bytebuffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

public class ByteBufferStart {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.print("false: ");
            run(false);
            System.out.print("true: ");
            run(true);
        }
    }

    private static void run(boolean parallel) {
        int len = 256 * 1024 * 1024;
        int byteSize = len * 4;

        ByteBuffer bb = ByteBuffer.allocateDirect(byteSize);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        int max = 4;
        int batch = len / max;

        IntConsumer runner = i -> {
            for (int x = 0; x < batch; x++) {
                int idx = i * batch + x;
                bb.putFloat(idx << 2, idx * 1.0F);
            }
        };

        long s = System.currentTimeMillis();
        IntStream intStream = IntStream.range(0, max);
        if (parallel) {
            intStream.parallel().forEach(runner);
        } else {
            intStream.forEach(runner);
        }

        System.out.println(System.currentTimeMillis() - s);
        for (int i = 0; i < len; i++) {
            float f = bb.getFloat();
            if (Float.compare(f, i * 1.0F) != 0) {
                throw new RuntimeException();
            }
        }
    }

}
