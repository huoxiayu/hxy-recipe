package com.hxy.recipe.jdk;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class FileReadStart {

    public static void main(String[] args) {
        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(FileReadStart.class.getResourceAsStream("/input")))) {
                String line;
                while ((line = br.readLine()) != null) {
                    log.info("read line: {}", line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
