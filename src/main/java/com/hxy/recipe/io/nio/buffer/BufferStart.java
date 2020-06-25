package com.hxy.recipe.io.nio.buffer;

import com.hxy.recipe.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

@Slf4j
public class BufferStart {

    public static void main(String[] args) {
        char[] charArray = new char[]{'a', 'b', 'c', 'd', 'e'};
        CharBuffer buffer = CharBuffer.wrap(charArray);
        log.info("read only {}", buffer.isReadOnly());
        log.info("direct {}", buffer.isDirect());
        log.info("has array {}", buffer.hasArray());

        ByteBuffer directBuffer = ByteBuffer.allocateDirect(1024);
        log.info("read only {}", directBuffer.isReadOnly());
        log.info("direct {}", directBuffer.isDirect());
        log.info("has array {}", directBuffer.hasArray());

        log.info("capacity {}, limit {}", buffer.capacity(), buffer.limit());
        log.info("buffer {}", buffer);

        buffer.limit(3);
        log.info("capacity {}, limit {}", buffer.capacity(), buffer.limit());
        log.info("buffer {}", buffer);

        buffer.put(0, 'o');
        buffer.put(1, 'p');
        buffer.put(2, 'q');
        log.info("buffer {}", buffer);

        log.info("buffer.position() {}", buffer.position());
        buffer.position(2);
        log.info("buffer.position() {}", buffer.position());

        buffer.put('x');
        log.info("buffer {}", buffer);

        buffer.position(0);
        log.info("buffer {}", buffer);

        Utils.assertEx(() -> buffer.put(3, 'r'), IndexOutOfBoundsException.class);

        ByteBuffer typeBuffer = ByteBuffer.allocate(1024);
        typeBuffer.putDouble(3.14D);
        typeBuffer.putLong(666L);
        typeBuffer.flip();
        log.info("getDouble {}", typeBuffer.getDouble());
        log.info("getLong {}", typeBuffer.getLong());
    }

}
