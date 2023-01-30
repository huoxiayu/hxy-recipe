package com.hxy.recipe.airline;

import io.airlift.airline.Command;
import io.airlift.airline.Option;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Command(name = "hxy", description = "hxy is a cli tool, do some useless things")
public class Hxy implements Runnable {

    @Option(name = {"-h", "--host"}, title = "host", description = "host(default:localhost:8080)")
    private String host = "localhost:8080";

    @Option(name = {"-u", "--user"}, title = "user", description = "user name")
    private String user;

    @Option(name = {"-p", "--password"}, title = "password", description = "password")
    private String password;

    @Override
    public void run() {
        System.out.println("host: " + host);
        System.out.println("user: " + user);
        System.out.println("password: " + password);
    }

}
