package com.hxy.jni;

import com.sun.jna.platform.unix.LibC;

import java.util.List;

public class JNAStart {

    public static void main(String[] args) {
        List.of("SHELL", "USER", "HOME").forEach(k -> {
            String v = LibC.INSTANCE.getenv(k);
            System.out.println(k + " -> " + v);
        });
    }

}
