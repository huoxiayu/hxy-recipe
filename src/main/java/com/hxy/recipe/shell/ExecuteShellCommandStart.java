package com.hxy.recipe.shell;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
public class ExecuteShellCommandStart {

    public static void main(String[] args) throws Exception {
        String url = "https://www.baidu.com";
        String file = UUID.randomUUID().toString();
        String downloadCmd = String.format("wget %s -O %s", url, file);
        log.info("downloadCmd -> {}", downloadCmd);

        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(downloadCmd);
        log.info("pid -> pr.pid()");
        pr.waitFor();

        BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = buf.readLine()) != null) {
            sb.append(line);
        }
        log.info("sb -> {}", sb);

        Path path = Paths.get(file);
        try (InputStream is = Files.newInputStream(path)) {
            String md5 = DigestUtils.md5Hex(is);
            log.info("md5 -> {}", md5);
        }

        Files.delete(path);
        log.info("delete -> {}", path);
    }

}
