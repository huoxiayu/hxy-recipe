package com.hxy.recipe.jdk.io;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DataInputStart {

    public static void main(String[] args) {
        oneByteRead();
        log.info("<---------->");
        oneCharRead();
        log.info("<---------->");
        byteArrayRead();
    }

    private static void oneByteRead() {
        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream("hello world".getBytes(StandardCharsets.UTF_8)))) {
            while (dis.available() != 0) {
                char c = (char) dis.readByte();
                log.info("read byte: {}", c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void oneCharRead() {
        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream("哈喽沃德".getBytes(StandardCharsets.UTF_8)))) {
            while (dis.available() != 0) {
                char c = dis.readChar();
                log.info("read char: {}", c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void byteArrayRead() {
        List<Byte> byteList = new ArrayList<>();
        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream("哈喽沃德".getBytes(StandardCharsets.UTF_8)))) {
            while (dis.available() != 0) {
                byteList.add(dis.readByte());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Byte[] bytes = byteList.toArray(Byte[]::new);
        String line = new String(ArrayUtils.toPrimitive(bytes));
        log.info("read line: {}", line);
    }

}
