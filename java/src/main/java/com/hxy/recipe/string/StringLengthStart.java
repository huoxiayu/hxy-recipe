package com.hxy.recipe.string;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Latin-1:
 * ISO 8859-1，又称`Latin-1`或“西欧语言”，是国际标准化组织内ISO/IEC 8859的第一个8位字符集。
 * 它以ASCII为基础，在空置的0xA0-0xFF的范围内，加入96个字母及符号，藉以供使用附加符号的拉丁字母语言使用。
 *
 * UTF-16:
 * 使用两个字节表示Unicode编码中的codepoint
 */
@Slf4j
public class StringLengthStart {

    @SneakyThrows
    public static void main(String[] args) {
        Field value = String.class.getDeclaredField("value");
        value.setAccessible(true);
        Field coder = String.class.getDeclaredField("coder");
        coder.setAccessible(true);

        for (String str : List.of("abc", "你我他")) {
            System.out.println("string-content: " + str);
            System.out.println("string-length: " + str.length());
            System.out.println("string-inner-bytes-length: " + ((byte[]) (value.get(str))).length);
            System.out.println("string-inner-coder: " + ((byte) (coder.get(str))));
        }
    }

}
