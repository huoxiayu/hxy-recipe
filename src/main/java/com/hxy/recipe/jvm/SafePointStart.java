package com.hxy.recipe.jvm;

import java.util.UUID;

public class SafePointStart {

    private static final boolean FLAG = true;

    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                System.out.println(UUID.randomUUID());
                byte[] b = new byte[10240];
                System.out.println(b);
            }
        }).start();

        if (FLAG) {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {

            }
        } else {
            while (true) {

            }
        }
    }

}

