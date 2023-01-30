package com.hxy.recipe.cli;

import java.util.Arrays;
import java.util.Properties;

/**
 * java -Dx=x1 -Dy=y1 Cli.java a b c
 *
 * 程序参数： a b c
 * VM参数：  -Dx=x1 -Dy=y1
 */
public class CommandLineInterfaceStart {

    public static void main(String[] args) {
        System.out.println("args -> " + Arrays.toString(args));

        Properties properties = System.getProperties();
        System.out.println("properties -> " + properties);
    }

}
