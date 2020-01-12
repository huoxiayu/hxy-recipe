package com.hxy.recipe.jdk;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class FileStart {

    public static void main(String[] args) throws IOException {
        File file = new File(".");

        String fileName = file.getName();
        log.info("fileName: {}", fileName);

        String absolutePath = file.getAbsolutePath();
        log.info("absolutePath: {}", absolutePath);

        String canonicalPath = file.getCanonicalPath();
        log.info("canonicalPath: {}", canonicalPath);

        String[] lists = file.list();
        log.info("lists: {}", Arrays.toString(lists));

        String[] filterLists = file.list((dir, name) -> name.contains("."));
        log.info("filterLists: {}", Arrays.toString(filterLists));
    }

}
