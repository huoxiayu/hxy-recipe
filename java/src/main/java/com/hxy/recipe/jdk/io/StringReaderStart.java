package com.hxy.recipe.jdk.io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringReader;

@Slf4j
public class StringReaderStart {

    public static void main(String[] args) {
        try (StringReader sr = new StringReader("hello world")) {
            int c;
            while ((c = sr.read()) != -1) {
                char ch = (char) c;
                log.info("read: {}", ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
