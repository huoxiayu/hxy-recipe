package com.hxy.recipe.serialize;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class Serialize {

    public static void main(String[] args) throws Exception {
        String str = "abc";
        File file = new File("./pb_serialize");
        IOUtils.write(str.getBytes(StandardCharsets.UTF_8), new FileOutputStream(file));
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
        System.out.println("read -> " + new String(bytes));
    }

}
