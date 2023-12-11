package com.jwtly10.TodoSync.cmd;

import com.jwtly10.TodoSync.config.ConfigReader;
import com.jwtly10.TodoSync.models.Todo;
import com.jwtly10.TodoSync.parsers.DirectoryParser;
import com.jwtly10.TodoSync.services.Github.GithubService;
import com.jwtly10.TodoSync.services.TodoProcessingService;
import com.jwtly10.TodoSync.services.TodoProcessingServiceImpl;
import picocli.CommandLine;

import java.nio.file.Paths;
import java.util.List;

@CommandLine.Command(
        name = "TodoSync",
        description = "A command line tool to sync TODOs from your codebase to github issues",
        mixinStandardHelpOptions = true,
        version = "0.1.0")
public class Command implements Runnable {

    private final DirectoryParser directoryParser;

    @CommandLine.Parameters(
            index = "0",
            description = "The directory to parse for TODOs",
            defaultValue = ".")
    private String directory;

    @CommandLine.Option(
            names = {"-v", "--verbose"}, description = "Enable verbose mode.")
    private boolean verbose;

    public Command(DirectoryParser directoryParser) {
        this.directoryParser = directoryParser;
    }

    @Override
    public void run() {
        if (directory.equals(".")) {
            directory = Paths.get("").toAbsolutePath().toString();
            System.out.println("No directory specified, using current directory: " + directory);
        }

        System.out.println("Processing directory: " + directory);


        if (verbose) {
            System.out.println("Verbose mode enabled");
        }

        List<Todo> res = directoryParser.parseDir(directory);
        for (Todo todo : res) {
            System.out.println(todo.getTitle());
        }

        TodoProcessingService todoProcessingService = new TodoProcessingServiceImpl(new GithubService(ConfigReader.getUserProperty("github.api.token")));

        todoProcessingService.processTodo(res.get(0));
    }
}
