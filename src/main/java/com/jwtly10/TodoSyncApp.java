package com.jwtly10;

import com.jwtly10.TodoSync.cmd.Command;
import com.jwtly10.TodoSync.parsers.DirectoryParserImpl;
import com.jwtly10.TodoSync.parsers.TodoParserImpl;
import picocli.CommandLine;

public class TodoSyncApp {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new Command(new DirectoryParserImpl(new TodoParserImpl()))).execute(args);
        System.exit(exitCode);
    }
}
