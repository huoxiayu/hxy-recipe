package com.hxy.recipe.serialize;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Serialize {

    public static void main(String[] args) throws Exception {
        String str = "abc";
        File file = new File("./pb_serialize");
        IOUtils.write(str.getBytes(StandardCharsets.UTF_8), new FileOutputStream(file));
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
        System.out.println("read -> " + new String(bytes));

        List<byte[][]> list = new ArrayList<>();
        byte[][] bb = {
                new byte[]{0, 0},
                new byte[]{1, 1},
                new byte[]{2, 2},
                new byte[]{3, 3},
        };
        list.add(bb);
        list.add(bb);
        list.add(bb);

        System.out.println("write");
        list.forEach(Serialize::print);
        System.out.println("<=================>");

        file = new File("./java_serialize");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(list);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        List<byte[][]> deserializeList = (List<byte[][]>) ois.readObject();
        deserializeList.forEach(Serialize::print);
    }

    private static void print(byte[][] bb) {
        for (byte[] b : bb) {
            System.out.println(Arrays.toString(b));
        }
        System.out.println("----------------");
    }

}
