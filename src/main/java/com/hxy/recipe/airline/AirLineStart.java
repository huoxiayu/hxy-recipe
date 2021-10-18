package com.hxy.recipe.airline;

import io.airlift.airline.Cli;
import io.airlift.airline.Help;

public class AirLineStart {

    public static void main(String[] args) {
        Cli.CliBuilder<Runnable> builder = Cli.<Runnable>builder("hxy")
                .withDefaultCommand(Help.class)
                .withCommands(Help.class, Hxy.class);
        Cli<Runnable> cliParser = builder.build();
        cliParser.parse(args).run();
    }

}
