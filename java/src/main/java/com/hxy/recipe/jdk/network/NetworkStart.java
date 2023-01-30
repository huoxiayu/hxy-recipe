package com.hxy.recipe.jdk.network;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class NetworkStart {

    public static void main(String[] args) throws UnknownHostException {
        String canonicalHostName = InetAddress.getLocalHost().getCanonicalHostName();
        log.info("canonicalHostName: {}", canonicalHostName);
    }

}
