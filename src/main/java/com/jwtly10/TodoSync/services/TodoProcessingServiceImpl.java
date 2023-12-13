package com.jwtly10.TodoSync.services;

import com.jwtly10.TodoSync.exceptions.TodoProcessingException;
import com.jwtly10.TodoSync.models.Todo;
import com.jwtly10.TodoSync.services.Github.GithubService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

public class TodoProcessingServiceImpl implements TodoProcessingService {
    private final GithubService githubService;

    public TodoProcessingServiceImpl(GithubService githubService) {
        this.githubService = githubService;
    }

    /**
     * Process a todo, creating a github issue for it, and updating the todo with the issue number
     *
     * @param todo the todo to process
     * @return true if successful, false otherwise
     */
    @Override
    public boolean processTodo(Todo todo) {
        String fn = todo.getFilepath().substring(todo.getFilepath().lastIndexOf("/") + 1);
        System.out.println("[REPORTING] " + fn + ":" + todo.getLine() + ":    " + todo.getPrefix() + " TODO" + ": " + todo.getTitle());

        String fileToProcess = todo.getFilepath();

        File file = new File(fileToProcess);

        if (!file.exists()) {
            System.out.println("File does not exist: " + fileToProcess);
            return false;
        }
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            String lineToReplace = lines.get(todo.getLine() - 1);
            Optional<Integer> githubIssueNumber = githubService.createIssue(todo).describeConstable();

            if (githubIssueNumber.isEmpty()) {
                System.out.println("Failed to create github issue in line: " + todo.getLine() + " in file: " + fileToProcess);
                return false;
            }
            lines.set(todo.getLine() - 1, lineToReplace.replace("TODO", "TODO(#" + githubIssueNumber.get() + ")"));

            if (lines.get(todo.getLine() - 1).equals(lineToReplace)) {
                throw new TodoProcessingException("Failed to update line " + todo.getLine() + " in file: " + fileToProcess + " But github issue #" + githubIssueNumber.get() + " was created.");
            }

            Files.write(file.toPath(), lines);
        } catch (IOException e) {
            System.out.println("Error reading file: " + fileToProcess);
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
